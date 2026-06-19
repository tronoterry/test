package com.adshield.redditblock

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var countText: TextView
    private lateinit var enableSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        countText = findViewById(R.id.countText)
        enableSwitch = findViewById(R.id.enableSwitch)

        findViewById<Button>(R.id.openSettingsButton).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        enableSwitch.isChecked = Prefs.isBlockingEnabled(this)
        enableSwitch.setOnCheckedChangeListener { _, checked ->
            Prefs.setBlockingEnabled(this, checked)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        statusText.setText(
            if (isAccessibilityServiceEnabled()) R.string.status_active else R.string.status_inactive
        )
        countText.text = getString(R.string.ads_skipped_count, Prefs.getSkippedCount(this))
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponent = "$packageName/${RedditAdBlockerService::class.java.name}"
        val enabledServices = Settings.Secure.getString(
            contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.split(":").any { it.equals(expectedComponent, ignoreCase = true) }
    }
}
