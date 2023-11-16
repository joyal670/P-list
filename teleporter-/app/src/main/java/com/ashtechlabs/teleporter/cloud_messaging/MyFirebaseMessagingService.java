package com.ashtechlabs.teleporter.cloud_messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.declared_orders.DeclaredOrdersActivity;
import com.ashtechlabs.teleporter.ui.Dashboard_Main;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.notifications.CargoNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.DriverDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.notifications.CourierNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications.WarehouseNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.notifications.TruckingNotificationActivity;
import com.ashtechlabs.teleporter.ui.pricing.PricingActivity;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. WarehouseNotification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("service_type"), remoteMessage.getData().get("notification_type"), remoteMessage.getData().get("token"));
        // sendNotification(remoteMessage.getData().get("data"));

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message WarehouseNotification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param serviceType
     */
    private void sendNotification(String messageBody, String serviceType, String notificationType, String token) {

        Class aClass = Dashboard_Main.class;
        Intent intent = null;

        if (notificationType.equals("declareOrder")) {

            intent = new Intent(this, DeclaredOrdersActivity.class);
            intent.putExtra("mode", serviceType);
            intent.putExtra("token", token);

        } else  if (notificationType.equals("notification")) {

            if (serviceType.equals("0")) {
                aClass = CourierNotificationActivity.class;
            } else if (serviceType.equals("1")) {
                aClass = TruckingNotificationActivity.class;
            } else if (serviceType.equals("2")) {
                aClass = CargoNotificationActivity.class;
            } else if (serviceType.equals("3")) {
                aClass = WarehouseNotificationActivity.class;
            }

            intent = new Intent(this, aClass);

        } else  if (notificationType.equals("rateCardExpiry")) {

            intent = new Intent(this, PricingActivity.class);
            intent.putExtra("mode", serviceType);
            intent.putExtra("token", token);

        } else {
            if (serviceType.equals("0")) {
                aClass = DriverDashBoardActivity.class;
            } else if (serviceType.equals("1")) {
                aClass = TruckingDashBoardActivity.class;
            } else if (serviceType.equals("2")) {
                aClass = CargoDashBoardActivity.class;
            } else if (serviceType.equals("3")) {
                aClass = WareHouseDashBoardActivity.class;
            }

            intent = new Intent(this, aClass);
        }



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "teleporter_channel_01";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "teleporter_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_mark_start)
                .setContentTitle("Teleporter")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        notificationManager.notify(0 /* ID of notification */, builder.build());
    }
}