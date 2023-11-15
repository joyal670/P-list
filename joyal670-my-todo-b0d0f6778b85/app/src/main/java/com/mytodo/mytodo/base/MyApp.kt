package com.mytodo.mytodo.base

import android.app.Application
import com.mytodo.mytodo.utils.AppPreferences
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApp : Application()
{
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        Realm.init(this)
        val config = RealmConfiguration.Builder().allowWritesOnUiThread(true).deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }
}