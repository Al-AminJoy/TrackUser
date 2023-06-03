package com.alamin.trackuser

import android.location.Location
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface LocationClient {

    fun getLocationUpdate(interval:Long):Flow<Location>

    class LocationException(message:String):Exception()

}