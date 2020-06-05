package com.krraju.vise.notificationservice;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationService extends FirebaseMessagingService {

    private PendingIntent contentIntent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        remoteMessage.getData();
        Intent intent = null;
        if(remoteMessage.getNotification().getTitle().equals("Announcements")){
            intent = new Intent(this, HomeScreen.class);
        }else if(remoteMessage.getNotification().getTitle().equals("Notes")){
            intent = new Intent(this, NotesActivity.class);
        }
         contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0,intent,0);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void showNotification(String title, String message){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    "V ISE", "Notification", NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableVibration(true);
            channel.enableLights(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "V ISE")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(111,builder.build());
    }
}
