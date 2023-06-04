package com.alamin.trackuser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION),1000)

        val targetSdkVersion: Int = applicationContext.applicationInfo.targetSdkVersion

        Toast.makeText(this, ""+targetSdkVersion, Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "REQUESTED_NOTIFICATION")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),1001)
        }

    }

    fun endService(view: View) {
        Intent(this,LocationService::class.java).also {
            it.action = LocationService.ACTION_STOP
            startService(it)
        }
    }


    fun startService(view: View) {
       Intent(this,LocationService::class.java).also {
            it.action = LocationService.ACTION_START
            startService(it)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

}