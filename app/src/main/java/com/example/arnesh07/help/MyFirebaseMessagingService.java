package com.example.arnesh07.help;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
public String ID="Channel ID";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //send notification.
        Log.v("FCMMessage","Received");
        Map<String,String> payload=remoteMessage.getData();
        sendNotification(payload);


    }
    public void sendNotification(Map<String,String> payload){
        String latitude=payload.get("latitude");
        String longitude=payload.get("longitude");
        String message=payload.get("name")  +" is there!Reach to him now!";
        Log.v("LatInPayload",latitude);
        Log.v("LongInPayload",longitude);
        //Uri gmmIntentUri = Uri.parse("geo:20.175227,72.8647202");
        String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + message + ")";
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        mapIntent.setPackage("com.google.android.apps.maps");

        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);
        createNotificationChannel();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, ID)
                .setSmallIcon(R.mipmap.help_round)
                .setContentTitle("Help!")
                .setContentText(payload.get("name") + " is in need of help!Help Now!")
                .setBadgeIconType(R.mipmap.help_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(703, mBuilder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel 2";
            String description = "Notification Channel 2";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
