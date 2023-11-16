package com.ncomfortsagent.base

import android.app.Application
import com.ncomfortsagent.utils.AppPreferences

class MyApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}