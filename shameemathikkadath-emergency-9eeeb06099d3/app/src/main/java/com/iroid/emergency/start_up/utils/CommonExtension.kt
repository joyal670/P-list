package com.iroid.emergency.start_up.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.iroid.emergency.preference.ConstantPreference.PASS_BUNDLE
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal

fun Context.nextActivity(cls: Class<*>?, bundle: Bundle) {
    if (bundle.isEmpty) {
        startActivity(Intent(this, cls))
    } else {
        val intent = Intent(this, cls)
        intent.putExtra(PASS_BUNDLE, bundle)
        startActivity(intent)
    }

}
fun Activity.netDialog(lifecycle: Lifecycle){
    NoInternetDialogSignal.Builder(
        this,
        lifecycle
    ).apply {
        dialogProperties.apply {
            connectionCallback = object : ConnectionCallback { // Optional
                override fun hasActiveConnection(hasActiveConnection: Boolean) {
                    // ...
                }
            }

            cancelable = false // Optional
            noInternetConnectionTitle = "No Internet" // Optional
            noInternetConnectionMessage =
                "Check your Internet connection and try again." // Optional
            showInternetOnButtons = true // Optional
            pleaseTurnOnText = "Please turn on" // Optional
            wifiOnButtonText = "Wifi" // Optional
            mobileDataOnButtonText = "Mobile data" // Optional

            onAirplaneModeTitle = "No Internet" // Optional
            onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
            pleaseTurnOffText = "Please turn off" // Optional
            airplaneModeOffButtonText = "Airplane mode" // Optional
            showAirplaneModeOffButtons = true // Optional
        }
    }.build()

}
