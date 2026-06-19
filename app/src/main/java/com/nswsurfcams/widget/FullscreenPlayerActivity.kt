package com.nswsurfcams.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.nswsurfcams.widget.databinding.ActivityFullscreenPlayerBinding

class FullscreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityFullscreenPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemBars()

        val camName = intent.getStringExtra(EXTRA_CAM_NAME).orEmpty()
        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL).orEmpty()

        binding.camNameLabel.text = camName
        binding.closeButton.setOnClickListener { finish() }

        if (streamUrl.isNotEmpty()) {
            startPlayback(streamUrl)
        }
    }

    private fun startPlayback(streamUrl: String) {
        val exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer
        exoPlayer.setMediaItem(MediaItem.fromUri(streamUrl))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        player = exoPlayer
    }

    private fun hideSystemBars() {
        val controller = WindowInsetsControllerCompat(window, binding.root)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    companion object {
        const val EXTRA_CAM_NAME = "extra_cam_name"
        const val EXTRA_STREAM_URL = "extra_stream_url"
    }
}
