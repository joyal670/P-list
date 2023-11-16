package com.mytodo.mytodo.notification


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mytodo.mytodo.R
import com.mytodo.mytodo.ui.main.home.activity.DashboardActivity
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.Realm
import io.realm.RealmQuery
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("TAG", "Alarm received")

        val realm: Realm? = Realm.getDefaultInstance()
        val sdf = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val sdf1 = SimpleDateFormat("h:ma", Locale.getDefault())
        val currentDateString = sdf.format(Date())
        val currentTimeString = sdf1.format(Date())
        Log.e("TAG", "initData: currentTimeString-----" + currentTimeString)
        val inputFormat: DateFormat = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val outputFormat: DateFormat = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val startDateStr = currentDateString
        val date: Date?
        date = inputFormat.parse(startDateStr)
        val startDateStrNewFormat: String = outputFormat.format(date)

        val query: RealmQuery<TaskModel> = realm!!.where(TaskModel::class.java).equalTo("endDate", startDateStrNewFormat).and().equalTo("endTime",currentTimeString)
        if(query.count() > 0)
        {
            sendNotification(context, intent.getStringExtra("data"))
        }

        Log.e("TAG", "scheduleAlarm: query---- "+query )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(mcontext: Context, messageBody: String?)
    {
        val intent = Intent(mcontext, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            mcontext, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            mcontext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                mcontext.getString(R.string.default_notification_channel_id),
                "Rewards Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.vibrationPattern = longArrayOf(0, 500, 200, 500)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder = Notification.Builder(
            mcontext,
            mcontext.getString(R.string.default_notification_channel_id)
        )
            .setContentTitle(mcontext.getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.app_icon1)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}