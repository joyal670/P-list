package com.property.propertyagent.cloud_messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.property.propertyagent.R
import com.property.propertyagent.start_up.auth.activity.AuthActivity


/*
const val channelId = "com.property.property_push_notify"
const val channelName = "com.property.property_notify"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var remoteViews: RemoteViews
    lateinit var builder: NotificationCompat.Builder

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.e("TAG", "onMessageReceived: " + remoteMessage.data)
            sendNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.e("TAG", "Refreshed token: $token")
    }

    private fun sendNotification(title: String, message: String) {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = channelId
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_app_foreground)
            .setContent(getRemoteView(title, message))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        remoteViews = RemoteViews("com.property.propertyagent", R.layout.push_notification_layout)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.app_logo, R.drawable.ic_launcher_foreground)
        return remoteViews
    }

}*/
