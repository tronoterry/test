package com.nswsurfcams.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.nswsurfcams.widget.databinding.ItemSurfCamBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory

class SurfCamAdapter(
    private val cams: List<SurfCam>,
    private val onCamClicked: (SurfCam) -> Unit,
) : RecyclerView.Adapter<SurfCamAdapter.CamViewHolder>() {

    inner class CamViewHolder(val binding: ItemSurfCamBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CamViewHolder {
        val binding = ItemSurfCamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CamViewHolder, position: Int) {
        val cam = cams[position]
        holder.binding.camName.text = cam.name
        holder.binding.camThumbnail.setImageResource(R.drawable.ic_surf_placeholder)
        holder.binding.root.setOnClickListener { onCamClicked(cam) }

        holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            val bitmap = withContext(Dispatchers.IO) { downloadBitmap(cam.thumbnailUrl) }
            if (bitmap != null) {
                holder.binding.camThumbnail.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = cams.size

    private fun downloadBitmap(url: String) = try {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = 5_000
            readTimeout = 5_000
        }
        connection.inputStream.use { BitmapFactory.decodeStream(it) }
    } catch (e: Exception) {
        null
    }
}
