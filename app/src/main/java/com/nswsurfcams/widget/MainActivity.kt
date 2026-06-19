package com.nswsurfcams.widget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nswsurfcams.widget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.camGrid.layoutManager = GridLayoutManager(this, 2)
        binding.camGrid.adapter = SurfCamAdapter(SurfCamRepository.cams) { cam ->
            startActivity(
                Intent(this, FullscreenPlayerActivity::class.java).apply {
                    putExtra(FullscreenPlayerActivity.EXTRA_CAM_NAME, cam.name)
                    putExtra(FullscreenPlayerActivity.EXTRA_STREAM_URL, cam.streamUrl)
                },
            )
        }
    }
}
