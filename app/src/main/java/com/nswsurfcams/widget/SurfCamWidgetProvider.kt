package com.nswsurfcams.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SurfCamWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
            val adapterIntent = Intent(context, SurfCamWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                // Each widget instance needs a distinct Intent so Android doesn't
                // reuse a cached RemoteViewsFactory across widgets.
                data = android.net.Uri.parse("surfcamwidget://widget/$widgetId")
            }

            val views = RemoteViews(context.packageName, R.layout.widget_surf_cams).apply {
                setRemoteAdapter(R.id.widget_grid, adapterIntent)
                setEmptyView(R.id.widget_grid, R.id.widget_empty)

                // One template PendingIntent for the whole collection; each item supplies
                // its own cam id/stream url via a fill-in Intent (see SurfCamRemoteViewsFactory).
                val clickIntent = Intent(context, FullscreenPlayerActivity::class.java)
                val clickPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
                setPendingIntentTemplate(R.id.widget_grid, clickPendingIntent)
            }

            appWidgetManager.updateAppWidget(widgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_grid)
        }
    }
}
