package com.alamin.trackuser

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "LocationService"
class LocationService:Service() {


    private val serviceScope = CoroutineScope(SupervisorJob()+Dispatchers.IO)
    private lateinit var locationClient: LocationClient



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()

            ACTION_STOP -> end()
        }
        return START_NOT_STICKY
    }

    private fun end() {
        stopForeground(true)
        stopSelf()
    }

    private fun start() {
        val notification = Notification.Builder(this,"location")
            .setContentTitle("Location Tracking")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText(" Location : Null")
            .setOngoing(true)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdate(10000L)
            .catch {

            }
            .onEach {
                val lat = it.latitude.toString()
                val long = it.longitude.toString()
                Log.d(TAG, "start: $lat $long")
                val updateNotification = notification.setContentText("Latitude $lat Longitude $long")
                notificationManager.notify(1,updateNotification.build())
            }.launchIn(serviceScope)


        startForeground(1,notification.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object{
        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"
    }
}