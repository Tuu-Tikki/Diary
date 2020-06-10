package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmAndNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_and_notification);

        //customized toolbar
        Toolbar addToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(addToolbar);

        //the return arrow on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //prefill the time EditText
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat timeFormat = new SimpleDateFormat(getString(R.string.time_format_string));
        final EditText chosenTime = findViewById(R.id.choose_time);
        chosenTime.setText(timeFormat.format(calendar.getTime()));

        //user sets the time with TimePicker
        final TimePickerDialog.OnTimeSetListener timeFromPicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                chosenTime.setText(timeFormat.format(calendar.getTime()));
            }
        };
        chosenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AlarmAndNotification.this, timeFromPicker, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //initialize AlarmManager and create Intent for AlarmReceiver.class
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Context context = getApplicationContext();
        final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentForAlarmReceiver, 0);

        //watch the state of Switch button
        Switch alarmSwitch = findViewById(R.id.alarm_on_off);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, /*calendar.getTimeInMillis()*/ 5000, pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                }
            }
        });
     }
}