package com.iroid.jeetmeet.base

import android.app.Application
import com.google.firebase.FirebaseApp
import com.iroid.jeetmeet.utils.AppPreferences

class MyApp :Application()
{

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        FirebaseApp.initializeApp(this)
    }

}