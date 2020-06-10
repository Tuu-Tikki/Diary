package com.example.diary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "channel_id";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, MainActivity.class);
        context.startActivity(myIntent);
    }
}

