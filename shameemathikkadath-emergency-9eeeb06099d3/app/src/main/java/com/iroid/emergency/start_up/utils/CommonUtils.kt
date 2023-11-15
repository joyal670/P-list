package com.iroid.emergency.start_up.utils

import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.SystemClock
import java.io.IOException
import java.util.*

class CommonUtils {
    companion object{
        fun getAddress(lat: Double, lng: Double,context: Context): String {

            val geocoder: Geocoder
            var addresses: List<Address>? = null
            var address: String? = null
            geocoder = Geocoder(context, Locale.getDefault())
            try {
                addresses = geocoder.getFromLocation(
                    lat,
                    lng,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addresses != null && addresses.size > 0) {
                address = addresses[0].getAddressLine(0)
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName
                val premises = addresses[0].premises
                val countryCode = addresses[0].countryCode
                val subAdminArea = addresses[0].subAdminArea
                val thoroughfare = addresses[0].thoroughfare
            }


            return address!!
        }

        fun roundBg(): Int {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(197), rnd.nextInt(143), rnd.nextInt(193))
        }

        var mLastClickTime=0L
        fun isOpenRecently():Boolean{
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return true
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            return false
        }

    }

}
