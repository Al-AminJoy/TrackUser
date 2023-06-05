package com.alamin.trackuser

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

private const val TAG = "DefaultLocationClient"
class DefaultLocationClient(private val context:Context,private val fusedLocationProviderClient: FusedLocationProviderClient):LocationClient {


    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

      return isGpsEnabled && isNetworkEnabled
    }

    @SuppressLint("MissingPermission")

    override fun getLocationUpdate(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()){
                throw LocationClient.LocationException("Missing Permission")
            }

            if (!isLocationEnabled()){
                Log.d(TAG, "getLocationUpdate: ")
                throw LocationClient.LocationException("GPS Disabled")
            }else{
                Log.d(TAG, "getLocationUpdate: Enabled")

            }

            val request = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)

            val  locationCallBack = object : LocationCallback(){
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    if (!isLocationEnabled()){
                        Log.d(TAG, "getLocationUpdate: ")
                        throw LocationClient.LocationException("GPS Disabled")
                    }else{
                        Log.d(TAG, "getLocationUpdate: Enabled")

                    }
                    result.locations.lastOrNull()?.let {
                        if (!isLocationEnabled()){
                            Log.d(TAG, "getLocationUpdate: ")
                            throw LocationClient.LocationException("GPS Disabled")
                        }else{
                            Log.d(TAG, "getLocationUpdate: Enabled Inside")

                        }
                        launch {
                            if (!isLocationEnabled()){
                                Log.d(TAG, "getLocationUpdate: ")
                                throw LocationClient.LocationException("GPS Disabled")
                            }
                            send(it)
                        }
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallBack,
                Looper.getMainLooper()
            )

            awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallBack) }

        }
    }
}