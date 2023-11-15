package com.iroid.patrickstore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.iroid.patrickstore.preference.AppPreferences
import com.onesignal.OneSignal

class App : MultiDexApplication() {
     val ONESIGNAL_APP_ID = "7ef11ad1-680b-48a4-929b-bb11d2226986"
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MultiDex.install(this)
        // Logging set to help debug issues, remove before releasing your app.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
//
////        // OneSignal Initialization
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

    companion object {
        lateinit var context: Context

    }
}
