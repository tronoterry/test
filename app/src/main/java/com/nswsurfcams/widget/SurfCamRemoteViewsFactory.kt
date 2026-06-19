package com.nswsurfcams.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.net.HttpURLConnection
import java.net.URL

/**
 * Feeds thumbnails into the widget's collection (GridView). Android calls
 * [getViewAt] on a background binder thread per the RemoteViewsFactory contract,
 * so a synchronous network fetch here is safe and won't block the UI thread.
 */
class SurfCamRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var cams: List<SurfCam> = emptyList()
    private val thumbnailCache = LruCache<String, Bitmap>(SurfCamRepository.cams.size.coerceAtLeast(1))

    override fun onCreate() {
        cams = SurfCamRepository.cams
    }

    override fun onDataSetChanged() {
        cams = SurfCamRepository.cams
    }

    override fun onDestroy() {
        thumbnailCache.evictAll()
    }

    override fun getCount(): Int = cams.size

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewAt(position: Int): RemoteViews {
        val cam = cams[position]
        val views = RemoteViews(context.packageName, R.layout.widget_cam_item)
        views.setTextViewText(R.id.cam_name, cam.name)

        val bitmap = loadThumbnail(cam)
        if (bitmap != null) {
            views.setImageViewBitmap(R.id.cam_thumbnail, bitmap)
        } else {
            views.setImageViewResource(R.id.cam_thumbnail, R.drawable.ic_surf_placeholder)
        }

        val fillInIntent = Intent().apply {
            putExtra(FullscreenPlayerActivity.EXTRA_CAM_NAME, cam.name)
            putExtra(FullscreenPlayerActivity.EXTRA_STREAM_URL, cam.streamUrl)
        }
        views.setOnClickFillInIntent(R.id.cam_thumbnail, fillInIntent)

        return views
    }

    private fun loadThumbnail(cam: SurfCam): Bitmap? {
        thumbnailCache.get(cam.id)?.let { return it }
        return try {
            val connection = (URL(cam.thumbnailUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5_000
                readTimeout = 5_000
            }
            connection.inputStream.use { stream ->
                BitmapFactory.decodeStream(stream)?.also { thumbnailCache.put(cam.id, it) }
            }
        } catch (e: Exception) {
            null
        }
    }
}
