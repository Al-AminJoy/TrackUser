package com.alamin.trackuser

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocationService:Service() {



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}