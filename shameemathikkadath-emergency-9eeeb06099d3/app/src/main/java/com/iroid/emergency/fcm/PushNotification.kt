package com.iroid.emergency.fcm


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iroid.emergency.R
import com.iroid.emergency.main.home.HomeActivity
import java.util.*


class PushNotification : FirebaseMessagingService() {
    private lateinit var intent: Intent
    private fun startAlert() {
        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.emer)
        mp.setOnCompletionListener { mp ->
            mp.stop()
            mp.release()
        }
        mp.start()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(p0: RemoteMessage) {
        Log.e("12456", "onMessageReceived: ")
//        startAlert()
        super.onMessageReceived(p0)
        val sound =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.emer)
        Log.e("#55566","$packageName")

        val channelId = getString(R.string.app_name)
        val resultIntent = Intent(this, HomeActivity::class.java)
// Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }


//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_MUTABLE
//        )

        val title = p0.notification!!.title
        val dec = p0.notification!!.body
        val contentView = RemoteViews(packageName,R.layout.custom_notification_layout)
        contentView.setTextViewText(R.id.title, title)
        contentView.setTextViewText(R.id.text, dec)


        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.default_dot)
                .setContentTitle(title)
                .setContentText(dec)
                .setSound(sound, AudioManager.STREAM_NOTIFICATION
                )
                .setAutoCancel(true)
                .setColor(getColor(R.color.red_color))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
               //.setContent(contentView)
              //  .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(contentView)
                .setCustomBigContentView(contentView)

        var notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            channel.setSound(sound, att)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
            }
        }
        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }



    fun generateRandom(): Int {
        val random = Random()
        return random.nextInt(9999 - 1000) + 1000
    }


}


