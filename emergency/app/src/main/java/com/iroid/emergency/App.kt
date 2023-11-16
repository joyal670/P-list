package com.iroid.emergency

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.utils.createNotificationChannel


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MultiDex.install(this)
        this.createNotificationChannel()

    }


}
