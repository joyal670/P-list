package com.property.propertyagent.cloud_messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity


import java.util.*


class PushNotification : FirebaseMessagingService() {
    private lateinit var intent:Intent
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("p_message", Gson().toJson(p0.notification))
        Log.e("p_message", Gson().toJson(p0.data))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(p0.data["type"]=="rate_food"){
             intent = Intent(applicationContext, HomeActivity::class.java)
            intent.putExtra("type",p0.data["type"])
        }else{
            intent = Intent(applicationContext, HomeOwnerActivity::class.java)
            intent.putExtra("type","")
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel: CharSequence = getString(R.string.app_name)
            val descChannel: String = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                getString(R.string.app_name),
                nameChannel,
                importance
            )
            channel.description = descChannel
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.setShowBadge(true)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val title = p0.notification!!.title
        val dec = p0.notification!!.body
        val type = p0.data["type"]
        Log.e("147", "onMessageReceived: "+title )
        Log.e("147", "onMessageReceived: "+dec )
        Log.e("147", "onMessageReceived: "+type )

        val notification =
            NotificationCompat.Builder(this, getString(R.string.app_name))
                .setChannelId(getString(R.string.app_name))
                .setContentTitle(title)
                .setContentText(dec)
                .setTicker(getString(R.string.app_name))
                .setSmallIcon(R.drawable.notification_icon)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(longArrayOf(500, 500))
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        if(type.equals("Promotional")){
            val image= p0.data["image"]
            Glide.with(this).asBitmap().load(image)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                    ) {
                        val s =
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                        s.setSummaryText(title)
                        notification.setStyle(s)
                        val mNotificationManager =
                            NotificationManagerCompat.from(this@PushNotification)
                        val notificationCompat: Notification = notification.build()
                        mNotificationManager.notify(199, notificationCompat)
                    }
                })
        }else{
            Glide.with(this).asBitmap().load(R.drawable.notification_icon)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                    ) {
                        notification.setLargeIcon(resource)
                        val mNotificationManager =
                            NotificationManagerCompat.from(this@PushNotification)
                        val notificationCompat: Notification = notification.build()
                        mNotificationManager.notify(generateRandom(), notificationCompat)
                    }
                })
        }

    }

    fun generateRandom(): Int {
        val random = Random()
        return random.nextInt(9999 - 1000) + 1000
    }

}


