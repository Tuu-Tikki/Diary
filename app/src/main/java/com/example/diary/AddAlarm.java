package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        //customized toolbar
        Toolbar addAlarmToolbar = (Toolbar) findViewById(R.id.add_alarm_toolbar);
        setSupportActionBar(addAlarmToolbar);

        //the return arrow on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //prefill EditText add_time_for_alarm
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat timeFormat = new SimpleDateFormat(getString(R.string.time_format_string));
        final EditText chooseTime = findViewById(R.id.add_time_for_alarm);
        chooseTime.setText(timeFormat.format(calendar.getTime()));

        //user sets the time with TimePicker
        final TimePickerDialog.OnTimeSetListener timeFromPicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                chooseTime.setText(timeFormat.format(calendar.getTime()));
            }
        };
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddAlarm.this, timeFromPicker, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //declare variable to save new alarm event
        final AlarmEvents event = new AlarmEvents();

        //save CheckBoxes condition
        final CheckBox measurement = findViewById(R.id.measure_pressure_alarm);
        final CheckBox pills = findViewById(R.id.taking_pills_alarm);
        measurement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                event.measurement = isChecked;
            }
        });
        pills.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                event.pills = isChecked;
            }
        });

        //save button on the toolbar
        final ImageButton saveAlarm = findViewById(R.id.save_alarm);
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm(event, calendar, chooseTime);
            }
        });

        //save button below
        final Button saveAlarmBelow = findViewById(R.id.alarm_save_button);
        saveAlarmBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm(event, calendar, chooseTime);
            }
        });
    }

    //check and save new alarm in the database
    public void saveAlarm(final AlarmEvents event, final Calendar calendar, final EditText time) {
        //check if an user checked one of CheckBoxes

        //if no - to show an error message
        if ((!event.measurement) && (!event.pills)) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, getString(R.string.error_message_for_add_alarm), Toast.LENGTH_SHORT);
            toast.show();

        //if yes - to save new event in the database
        } else {
            //save the time for new alarm event
            event.timeOfAlarm = time.getText().toString();

            //create or open the database
            final AlarmDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AlarmDatabase.class, "AlarmEvents")
                    .build();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    db.alarmDao().insert(event);
                }
            });

            //close the database
            db.close();

            //initialize AlarmManager and create Intent for AlarmReceiver.class
            final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Context context = getApplicationContext();
            final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentForAlarmReceiver, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            //return to Alarm and Notification
            navigateUpTo(new Intent(AddAlarm.this, AlarmAndNotification.class));
        }
    }
}