package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AlarmAndNotification extends AppCompatActivity {
    List<AlarmEvents> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_and_notification);

        //customized toolbar
        Toolbar alarmToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(alarmToolbar);

        //the return arrow on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //the button to go to add new alarm activity
        ImageButton imageButton = findViewById(R.id.new_alarm);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmAndNotification.this, AddAlarm.class));
            }
        });

        //the button below to go to add new alarm activity
        Button button = findViewById(R.id.new_alarm_below);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmAndNotification.this, AddAlarm.class));
            }
        });

        //create or open a database
        final AlarmDatabase db = Room.databaseBuilder(getApplicationContext(),
            AlarmDatabase.class, "AlarmEvents")
            .build();

        //RecyclerView
        final RecyclerView rvAlarms = (RecyclerView) findViewById(R.id.alarm_events_display);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                events = db.alarmDao().getAll();

                final AdapterForAlarmEvents adapter = new AdapterForAlarmEvents(events);

                runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                           rvAlarms.setAdapter(adapter);
                           rvAlarms.setLayoutManager(new LinearLayoutManager(AlarmAndNotification.this));
                      }
                 });

                adapter.notifyDataSetChanged();
                
                db.close();
            }
        });
     }
}