package com.iroid.emergency.service

import com.iroid.emergency.db.LocationEntity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iroid.emergency.R
import com.iroid.emergency.main.home.LocationRepository
import com.iroid.emergency.modal.common.Location_new
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.utils.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executors

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {


        if (intent.action == ACTION_PROCESS_UPDATES) {
            LocationAvailability.extractLocationAvailability(intent)?.let {
                if (!PermissionUtils.isLocationPermissionGranted(context)) {
                    Log.w(TAG, "Background location permissions have been revoked!")
                    context.cancelAllNotifications()
                    context.showLocationUnavailableNotification(
                        context.getString(R.string.location_background_permission_revoked),
                        context.getString(R.string.location_rationale)
                    )
                }

                if (!it.isLocationAvailable) {
                    Log.w(TAG, "Location is currently unavailable!")
                }

                if (!context.isLocationEnabled()) {
                    AppPreferences.isLocationEnabled=false
                    context.cancelAllNotifications()
                    context.showLocationUnavailableNotification(
                        context.getString(R.string.location_service_disabled),
                        context.getString(R.string.enable_location_services_text)
                    )
                }
            }

            LocationResult.extractResult(intent)?.let { locationResult ->
                try {

                    locationResult.locations.map {
                        AppPreferences.userLat = it.latitude.toString()
                        AppPreferences.userLan = it.longitude.toString()
                        val db = Firebase.firestore
                        db.collection("Location").document(AppPreferences.userMobile!!)
                            .set(Location_new(it.latitude,
                                it.longitude,AppPreferences.userMobile!!))
                            .addOnSuccessListener { documentReference ->

                            }
                            .addOnFailureListener { e ->

                            }
                    }





//                    if (locations.isNotEmpty()) {
//                        LocationRepository.getInstance(context, Executors.newSingleThreadExecutor())
//                            .addLocations(locations)


                }catch (ex:Exception){

                }

            }
        }
    }


    // Note: This function's implementation is only for debugging purposes. If you are going to do
    // this in a production app, you should instead track the state of all your activities in a
    // process via android.app.Application.ActivityLifecycleCallbacks's
    // unregisterActivityLifecycleCallbacks(). For more information, check out the link:
    // https://developer.android.com/reference/android/app/Application.html#unregisterActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks
    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        appProcesses.forEach { appProcess ->
            if (appProcess.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == context.packageName
            ) {
                return true
            }
        }
        return false
    }

    companion object {
        private val TAG = LocationUpdatesBroadcastReceiver::class.simpleName

        const val ACTION_PROCESS_UPDATES =
            "com.iroid.emergency.action.PROCESS_UPDATES"
    }
}
