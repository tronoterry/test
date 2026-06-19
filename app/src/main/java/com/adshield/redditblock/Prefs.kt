package com.adshield.redditblock

import android.content.Context

private const val PREFS_NAME = "ad_shield_prefs"
private const val KEY_ENABLED = "enabled"
private const val KEY_SKIPPED_COUNT = "skipped_count"

/** Shared preference keys used by both the MainActivity UI and the background accessibility service. */
object Prefs {
    fun isBlockingEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, true)

    fun setBlockingEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun getSkippedCount(context: Context): Int =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_SKIPPED_COUNT, 0)

    fun incrementSkippedCount(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_SKIPPED_COUNT, prefs.getInt(KEY_SKIPPED_COUNT, 0) + 1).apply()
    }
}
