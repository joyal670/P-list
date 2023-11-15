package com.proteinium.proteiniumdietapp

import android.app.Application
import com.proteinium.proteiniumdietapp.preference.AppPreferences

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }


}