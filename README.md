# AdShield for Reddit

An Android app that hides ads in the **official** Reddit app. It does **not**
repackage or modify Reddit's APK (that would violate Reddit's Terms of Service
and copyright, and isn't something this project does). Instead it's a small
companion app that uses Android's **Accessibility Service** API to watch the
Reddit app's on-screen content and automatically scroll the feed past any post
labeled `Ad`, `Promoted`, or `Sponsored` — the same labels visible in the
screenshot this app was built from.

## How it works

1. `RedditAdBlockerService` (an `AccessibilityService`) is restricted to only
   ever look at the `com.reddit.frontpage` package (the official Reddit app).
2. On every content/scroll change in Reddit, it walks the visible accessibility
   tree looking for a text node that says `Ad`, `Promoted`, or `Sponsored`.
3. When found, it walks up to that post's card (the node whose parent is the
   scrollable feed list), reads its on-screen bounds, and dispatches a quick
   swipe gesture that scrolls the feed past exactly that card's height — so
   you never see the ad.
4. A counter of skipped ads and an on/off switch are stored in
   `SharedPreferences` and shown in `MainActivity`.

No data is collected, stored remotely, or transmitted anywhere — everything
happens on-device.

## Project layout

```
app/src/main/java/com/adshield/redditblock/
  RedditAdBlockerService.kt   - the accessibility service / ad detection + skip logic
  MainActivity.kt             - settings screen (enable toggle, skipped count, link to Settings)
  Prefs.kt                    - small SharedPreferences wrapper shared by both
app/src/main/res/xml/accessibility_service_config.xml - service config (scoped to Reddit's package)
app/src/main/AndroidManifest.xml
```

## ⚠️ Important: this sandbox could not compile the APK

This environment's network policy blocks `dl.google.com` / `maven.google.com`,
which is where the Android SDK and the Android Gradle Plugin are hosted, and
there is no Android SDK installed here. That means **I could write and
sanity-check the project structure, but I could not actually run a Gradle
build or produce a signed/installable `.apk` in this sandbox.** I generated
the Gradle wrapper (`gradlew`) locally so the project is ready to build the
moment it's opened somewhere with normal internet access and an Android SDK.

## How to build the APK yourself

**Option A — Android Studio (easiest)**
1. Install [Android Studio](https://developer.android.com/studio).
2. Open this repository's root folder as a project.
3. Let Gradle sync (it will download the Android SDK platform/build tools
   and dependencies automatically).
4. Build → Build Bundle(s) / APK(s) → Build APK(s).
5. The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

**Option B — command line**, with the Android SDK installed and
`ANDROID_HOME` set:
```
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`.

## Installing on your phone

1. Copy `app-debug.apk` to your phone (USB, `adb push`, Drive, email — your choice).
2. On the phone: Settings → Security → allow "Install unknown apps" for whichever
   app you used to open the APK file.
3. Tap the APK to install it.
4. Open **AdShield for Reddit**, tap **Open Accessibility Settings**, find
   "AdShield for Reddit" in the list, and turn it on. Android will warn you
   that accessibility services can read screen content — that's expected and
   required for this to detect ad labels in Reddit's feed.
5. Open the Reddit app and scroll — sponsored posts should be skipped
   automatically. The skipped-ad counter updates in the AdShield app.

## Limitations / notes

- This relies on Reddit's own `Ad` / `Promoted` / `Sponsored` text label
  staying in the UI. If Reddit changes that label or hides it from
  accessibility trees, detection would need to be updated to match.
- It is heuristic-based (text matching + view hierarchy walk), not a
  guaranteed 100% blocker — some unusual ad layouts may slip through.
- Because this uses an Accessibility Service rather than modifying Reddit's
  APK, it can't be published to Google Play (Play policy prohibits
  accessibility services used for ad-blocking), so it's meant for personal
  sideloading only.
