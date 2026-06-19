package com.nrltipping.app

import android.app.Application
import com.google.firebase.FirebaseApp

class NrlTippingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
