package com.example.diary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //toast message
        Toast toast = Toast.makeText(context, context.getString(R.string.text_of_notification) , Toast.LENGTH_LONG);
        toast.show();

        // this String is necessary for the constructor NotificationCompat, so the notifications work with API 26+
        final String CHANNEL_ID = "DIARY_ALARM_CHANNEL";

        //Sound for notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //make a sound with MediaPlayer
        MediaPlayer mp = MediaPlayer.create(context, soundUri);
        if (mp != null) {
            mp.start();
            //mp.release();
        }

        //A class to encapsulate a collection of attributes describing information about an audio stream
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();
        }

        // Create an explicit intent for an Activity in your app
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        //making notification with certain parameters. PRIORITY_HIGH is for show the notification over the working app
        //and VISIBILITY_PUBLIC show full notification on lock-screen
        //setAutoCancel(true) automatically delete a notification after an user taps it
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.text_of_notification))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(soundUri)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);

        //Class to notify the user of events that happen
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //create a notification channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.channel_description));

            //add a sound
            channel.setSound(soundUri, audioAttributes);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        //try to make sound
        notification.sound = soundUri;
        notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.audioAttributes = audioAttributes;
        }

        //get requestCode from extra
        final int requestCode = intent.getIntExtra("RequestCode", -1);

        //show the notification
        if (notificationManager != null) {
            notificationManager.notify(requestCode, notification);
        }

        //repeat alarm for API >= 19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (requestCode != -1) {
                final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
                intentForAlarmReceiver.putExtra("RequestCode", requestCode);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                        intentForAlarmReceiver, 0);
                final Calendar calendar = Calendar.getInstance();
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, pendingIntent);
                }
            } else {
                Toast toastForMissedCode = Toast.makeText(context,
                        context.getString(R.string.error_message_for_missed_requestCode), Toast.LENGTH_SHORT);
                toastForMissedCode.show();
            }
        }
    }
}

