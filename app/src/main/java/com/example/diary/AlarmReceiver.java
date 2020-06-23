package com.example.diary;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //toast message (it works)
        Toast toast = Toast.makeText(context, "Alarm!", Toast.LENGTH_LONG);
        toast.show();

        //notification - It works!

        // this String is necessary for the constructor NotificationCompat, so the notifications work with API 26+
        final String CHANNEL_ID = "CHANNEL_ID";

        //making notification with certain parameters. PRIORITY_HIGH is for show the notification over the working app
        //and VISIBILITY_PUBLIC show full notification on lock-screen
        //setAutoCancel(true) automatically delete a notification after an user taps it
        //setDefaults(int defaults) set the sound for notification (API 11-26)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title")
                .setContentText("Alarm!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);

        //try to make work "automatically delete a notification after an user taps it"
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //HIGH_PRIORITY needs vibrate permission. Here this feature is disabled.
        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[] {0});

        //show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1,notification);
    }
}

