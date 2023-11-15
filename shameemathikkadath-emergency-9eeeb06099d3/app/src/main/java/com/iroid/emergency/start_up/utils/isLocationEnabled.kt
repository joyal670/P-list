package com.iroid.emergency.start_up.utils

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

fun Context.isLocationEnabled(): Boolean {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}
