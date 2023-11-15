package com.property.propertyuser.base

import android.app.Application
import com.property.propertyuser.preference.AppPreferences

/*@HiltAndroidApp*/
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }

}