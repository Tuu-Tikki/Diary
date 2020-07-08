package com.example.diary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
        //toast message
        Toast toast = Toast.makeText(context, context.getString(R.string.text_of_notification) , Toast.LENGTH_LONG);
        toast.show();

        // this String is necessary for the constructor NotificationCompat, so the notifications work with API 26+
        final String CHANNEL_ID = "DIARY_ALARM_CHANNEL";

        //Class to notify the user of events that happen
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //create a notification channel for API 26+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.channel_description));
            notificationManager.createNotificationChannel(channel);
        }

        //making notification with certain parameters. PRIORITY_HIGH is for show the notification over the working app
        //and VISIBILITY_PUBLIC show full notification on lock-screen
        //setAutoCancel(true) automatically delete a notification after an user taps it
        //setDefaults(int defaults) set the sound for notification (API 11-26)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.text_of_notification))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);

        Notification notification = builder.build();

        //try to make work "automatically delete a notification after an user taps it"
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //show the notification
        notificationManager.notify(1, notification);

        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            AddAlarm.setAlarm(context,);
        }*/
    }
}

