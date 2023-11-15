package com.property.propertyuser.firebase_message


/*
const val channelId="com.property.propertyuser_push_notify_new"
const val channelName="com.property.propertyuser_notify"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var remoteViews: RemoteViews
    lateinit var builder: NotificationCompat.Builder
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("onmesagereceived",remoteMessage.toString())
        if(remoteMessage.notification !=null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }

    fun getRemoteView(title: String,message: String):RemoteViews{
        Log.e("onremoteview","inside")
        remoteViews=
            RemoteViews("com.property.propertyuser",R.layout.push_notification_layout)
        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.message,message)
        remoteViews.setImageViewResource(R.id.app_logo,R.drawable.ic_launcher_foreground)

        return remoteViews
    }

    fun generateNotification(title:String,message:String){
        val intent= Intent(this,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra("action_msg", "some message for toast")
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        Log.e("generate","inside")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setShowBadge(false)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(applicationContext,
                channelId)
                .setContent(getRemoteView(title,message))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_foreground))
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground,"dd",snoozePendingIntent)
        } else {

            builder = NotificationCompat.Builder(applicationContext,
                channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContent(getRemoteView(title,message))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_foreground))
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground,"dd",snoozePendingIntent)
        }
        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(LongArray(0))
        notificationManager.notify(1234, builder.build())

    }
}*/
