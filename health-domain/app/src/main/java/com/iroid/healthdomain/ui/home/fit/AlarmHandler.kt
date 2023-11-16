package com.iroid.healthdomain.ui.home.fit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*


class AlarmHandler(private val context: Context) {

    companion object {
        private const val TAG = "AlarmHandler"
    }


    fun setAlarmManager() {
        val intent: Intent = Intent(context, ActivityTracker::class.java)
        val sender: PendingIntent
        sender = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                2, Intent(context, javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                2, Intent(context, javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val alarmManager: AlarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager.let {
            val triggerAfter: Long = 1 * 60 * 1000
            val triggerEvery: Long = 30 * 60 * 1000
            val cal: Calendar = Calendar.getInstance()


            it.setRepeating(AlarmManager.RTC_WAKEUP, 1000, 1000, sender)
        }

    }

    fun cancelAlarmManager() {
        val intent: Intent = Intent(context, ActivityTracker::class.java)

        val sender: PendingIntent
        sender = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                2, Intent(context, javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                2, Intent(context, javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val alarmManager: AlarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager.let {
            it.cancel(sender)
        }
    }


}
