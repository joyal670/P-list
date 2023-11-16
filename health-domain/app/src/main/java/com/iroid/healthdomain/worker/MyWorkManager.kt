package com.iroid.healthdomain.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.iroid.healthdomain.R


class MyWorkManager(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{
        const val TASK_DESC = "task_desc"
        private const val TAG = "MyWorkManager"
    }

    override fun doWork(): Result {

        val taskDesc = inputData.getString(TASK_DESC)

        //setting output data
        val data: Data = Data.Builder()
                .putString(TASK_DESC, "The conclusion of the task")
                .build()

        displayNotification("My Worker", taskDesc)
        return Result.success(data)

    }

    /*
    * The method is doing nothing but only generating
    * a simple notification
    * If you are confused about it
    * you should check the Android Notification Tutorial
    * */
    private fun displayNotification(title: String, task: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("healthdomain", "healthdomain", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: Notification.Builder = if (
                Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder(context, "healthdomain")
                    .setContentTitle(title)
                    .setContentText(task)
                    .setSmallIcon(R.mipmap.ic_launcher)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.notify(1, notification.build())
    }

}