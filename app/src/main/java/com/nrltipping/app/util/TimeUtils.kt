package com.nrltipping.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {
    fun currentSeason(): Int = Calendar.getInstance().get(Calendar.YEAR)

    fun isLocked(lockTime: Date?, now: Date = Date()): Boolean {
        if (lockTime == null) return false
        return !now.before(lockTime)
    }

    fun formatCountdown(target: Date, now: Date = Date()): String {
        val millis = target.time - now.time
        if (millis <= 0) return "Locked"
        val totalSeconds = millis / 1000
        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return when {
            days > 0 -> "%dd %dh %dm".format(days, hours, minutes)
            hours > 0 -> "%dh %dm %ds".format(hours, minutes, seconds)
            else -> "%dm %ds".format(minutes, seconds)
        }
    }

    fun formatDateTime(date: Date?): String {
        if (date == null) return "TBC"
        val fmt = SimpleDateFormat("EEE d MMM, h:mm a", Locale.getDefault())
        return fmt.format(date)
    }
}
