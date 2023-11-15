package com.property.propertyagent.base

import android.app.Application
import com.property.propertyagent.utils.AppPreferences

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}