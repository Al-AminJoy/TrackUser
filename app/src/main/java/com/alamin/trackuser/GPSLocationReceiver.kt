package com.alamin.trackuser

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log

private const val TAG = "GPSLocationReceiver"
class GPSLocationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action!!.equals(LocationManager.PROVIDERS_CHANGED_ACTION)){
            Log.d(TAG, "onReceive:")

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


            if (!isGpsEnabled || !isNetworkEnabled) {
                Intent(context, LocationService::class.java)
                    .also {
                       it.action = LocationService.ACTION_STOP
                        context.startService(it)
                    }
            }
        }
    }
}