package com.example.diary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
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

        //get requestCode from extra
        final int requestCode = intent.getIntExtra("RequestCode", -1);

        //show the notification
        notificationManager.notify(requestCode, notification);

        //repeat alarm for API >= 19
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (requestCode != -1) {
                final AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
                intentForAlarmReceiver.putExtra("RequestCode", requestCode);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                        intentForAlarmReceiver, 0);
                final Calendar calendar = Calendar.getInstance();
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, pendingIntent);
            } else {
                Toast toastForMissedCode = Toast.makeText(context,
                        context.getString(R.string.error_message_for_missed_requestCode), Toast.LENGTH_SHORT);
                toastForMissedCode.show();
            }
        }
    }
}

