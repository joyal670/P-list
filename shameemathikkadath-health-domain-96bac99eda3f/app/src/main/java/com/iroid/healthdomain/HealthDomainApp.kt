package com.iroid.healthdomain

import android.app.Application
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.home.fit.AlarmHandler
import com.iroid.healthdomain.ui.preference.AppPreferences

class HealthDomainApp : Application() {

    protected lateinit var userPreferences: UserPreferences
    private lateinit var alarmHandler: AlarmHandler


    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        userPreferences = UserPreferences(context = applicationContext)

        alarmHandler = AlarmHandler(context = this)
        alarmHandler.cancelAlarmManager()
        alarmHandler.setAlarmManager()
    }

}