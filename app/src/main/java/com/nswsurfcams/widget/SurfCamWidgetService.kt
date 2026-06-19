package com.nswsurfcams.widget

import android.content.Intent
import android.widget.RemoteViewsService

class SurfCamWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
        SurfCamRemoteViewsFactory(applicationContext)
}
