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
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
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

        //start TimePickerDialog with start of the activity
        new TimePickerDialog(AddAlarm.this, timeFromPicker, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();

        //or start TimePickerDialog with click on the time field
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddAlarm.this, timeFromPicker, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //declare variable to save new alarm event
        final AlarmEvents event = new AlarmEvents();

        //Switch variable
        final Switch typeOfAlarm = findViewById(R.id.measure_or_pills);

        //save button on the toolbar
        final ImageButton saveAlarm = findViewById(R.id.save_alarm);
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSaved = saveAlarm(event, chooseTime, typeOfAlarm);

                if (isSaved) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            setAlarm(context, getLastRecord());
                        }
                    });
                }
            }
        });

        //save button below
        final Button saveAlarmBelow = findViewById(R.id.alarm_save_button);
        saveAlarmBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSaved = saveAlarm(event, chooseTime, typeOfAlarm);

                if (isSaved) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            setAlarm(context, getLastRecord());
                        }
                    });
                }
            }
        });
    }

    //save new alarm in the database and return true if new alarm created
    public boolean saveAlarm(final AlarmEvents event, final EditText time, final Switch type) {
        //check the switch for type of alarm
        if (type.isChecked()) {
            event.pills = type.isChecked();
        } else {
            event.measurement = !type.isChecked();
        }

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

        //return to Alarm and Notification
        navigateUpTo(new Intent(AddAlarm.this, AlarmAndNotification.class));

        return true;
    }

    //initialize AlarmManager and create Intent for AlarmReceiver.class
    public static void setAlarm(Context context, AlarmEvents event) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        final int requestCode = event.alarmEventId;
        final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
        intentForAlarmReceiver.putExtra("RequestCode", requestCode);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intentForAlarmReceiver, 0);
        //get time from database record to calendar
        final Calendar calendar = Calendar.getInstance();
        final int hour = Integer.parseInt(event.timeOfAlarm.substring(0, 2));
        final int minute = Integer.parseInt(event.timeOfAlarm.substring(3));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        //set alarm
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
        }
    }

    public AlarmEvents getLastRecord() {
        final AlarmDatabase db = Room.databaseBuilder(getApplicationContext(),
                AlarmDatabase.class, "AlarmEvents")
                .build();

        return db.alarmDao().getLast();
    }
}