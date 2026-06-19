package com.adshield.redditblock

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Watches the official Reddit app's on-screen content for sponsored-post markers
 * ("Ad" / "Promoted" / "Sponsored" next to a post author, as Reddit itself labels ads)
 * and swipes the feed past that post's card so the ad is never shown to the user.
 *
 * This never modifies Reddit's app or APK - it only reads accessibility node text
 * inside Reddit (as any accessibility service can) and issues normal swipe gestures.
 */
class RedditAdBlockerService : AccessibilityService() {

    companion object {
        private const val REDDIT_PACKAGE = "com.reddit.frontpage"
        private const val ACTION_COOLDOWN_MS = 800L
        private const val SIGNATURE_TTL_MS = 30_000L
        private val AD_MARKERS = setOf("Ad", "Promoted", "Sponsored")
    }

    private var lastActionAt = 0L
    private val recentlySkipped = LinkedHashMap<String, Long>()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName?.toString() != REDDIT_PACKAGE) return
        if (!Prefs.isBlockingEnabled(this)) return

        val now = System.currentTimeMillis()
        if (now - lastActionAt < ACTION_COOLDOWN_MS) return

        val root = rootInActiveWindow ?: return
        try {
            val match = findAdCard(root) ?: return
            val (cardNode, cardRect) = match
            try {
                val signature = "${cardRect.left},${cardRect.top},${cardRect.right},${cardRect.bottom}"
                val lastSeen = recentlySkipped[signature]
                if (lastSeen == null || now - lastSeen > SIGNATURE_TTL_MS) {
                    pruneSignatures(now)
                    recentlySkipped[signature] = now
                    if (skipPastCard(cardRect)) {
                        lastActionAt = now
                        Prefs.incrementSkippedCount(this)
                    }
                }
            } finally {
                cardNode.recycle()
            }
        } finally {
            root.recycle()
        }
    }

    override fun onInterrupt() {}

    private fun pruneSignatures(now: Long) {
        val it = recentlySkipped.entries.iterator()
        while (it.hasNext()) {
            if (now - it.next().value > SIGNATURE_TTL_MS) it.remove()
        }
    }

    /** Finds the first ad-marker text node in the tree and returns its enclosing post-card node + screen bounds. */
    private fun findAdCard(root: AccessibilityNodeInfo): Pair<AccessibilityNodeInfo, Rect>? {
        val markerNode = findMarkerNode(root) ?: return null
        val cardNode = findCardAncestor(markerNode) ?: markerNode
        val rect = Rect()
        cardNode.getBoundsInScreen(rect)
        if (rect.height() <= 0) {
            cardNode.recycle()
            return null
        }
        return cardNode to rect
    }

    private fun findMarkerNode(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val text = node.text?.toString()?.trim()
        if (text != null && AD_MARKERS.contains(text)) {
            return node
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findMarkerNode(child)
            if (found != null) {
                if (found !== child) child.recycle()
                return found
            }
            child.recycle()
        }
        return null
    }

    /** Walks up from the marker node to the post card: the ancestor that is a direct child of the scrollable feed. */
    private fun findCardAncestor(marker: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        var current = marker
        while (true) {
            val parent = current.parent ?: return null
            if (parent.isScrollable) {
                parent.recycle()
                return current
            }
            if (current !== marker) current.recycle()
            current = parent
        }
    }

    /** Swipes the feed up by roughly the height of the ad card so it scrolls out of view instantly. */
    private fun skipPastCard(cardRect: Rect): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return false
        val metrics = resources.displayMetrics
        val centerX = metrics.widthPixels / 2f
        val startY = cardRect.bottom.toFloat().coerceAtMost(metrics.heightPixels - 10f)
        val travel = (cardRect.height() + 40).toFloat()
        val endY = (startY - travel).coerceAtLeast(50f)
        if (endY >= startY) return false

        val path = Path().apply {
            moveTo(centerX, startY)
            lineTo(centerX, endY)
        }
        val stroke = GestureDescription.StrokeDescription(path, 0, 120)
        val gesture = GestureDescription.Builder().addStroke(stroke).build()
        return dispatchGesture(gesture, null, null)
    }
}
