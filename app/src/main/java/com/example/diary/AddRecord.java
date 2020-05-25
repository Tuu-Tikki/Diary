package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.diary.AppDatabase.MIGRATION_2_3;
import static com.example.diary.AppDatabase.MIGRATION_3_4;

public class AddRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //customized toolbar
        Toolbar addToolbar = (Toolbar) findViewById(R.id.add_record_toolbar);
        setSupportActionBar(addToolbar);

        //return arrow on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create or open database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "BloodPressureRecords")
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build();

        //prefill date and time fields
        final EditText date = findViewById(R.id.date);
        final EditText time = findViewById(R.id.time);
        final Calendar calendar = Calendar.getInstance();
        String dateFormatString = "dd.MM.yyyy";
        String timeFormatString = "HH:mm";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        date.setText(dateFormat.format(calendar.getTime()));
        final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);
        time.setText(timeFormat.format(calendar.getTime()));

        //date is filled with calendar by user
        final DatePickerDialog.OnDateSetListener dateFromPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date.setText(dateFormat.format(calendar.getTime()));
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddRecord.this, dateFromPicker,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //time is filled by user with TimePickerDialog
        final TimePickerDialog.OnTimeSetListener timeFromPicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                time.setText(timeFormat.format(calendar.getTime()));
            }
        };
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddRecord.this, timeFromPicker, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });


        //variables for saving new record
        final EditText systolicPressure = findViewById(R.id.addSystolicPressure);
        final EditText diastolicPressure = findViewById(R.id.addDiastolicPressure);
        final BloodPressureData record = new BloodPressureData();

        //create NumberPicker for the pulse field
        final NumberPicker pulse = (NumberPicker) findViewById(R.id.addPulse);
        pulse.setMaxValue(300);
        pulse.setMinValue(30);
        pulse.setValue(70);
        pulse.setWrapSelectorWheel(false);
        pulse.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pulse.setValue(newVal);
            }
        });


        //save new record in db
        final Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.systolicPressure = Integer.parseInt(systolicPressure.getText().toString());
                record.diastolicPressure = Integer.parseInt(diastolicPressure.getText().toString());
                record.dateOfRecord = date.getText().toString();
                record.timeOfRecord = time.getText().toString();
                record.pulse = pulse.getValue();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.bloodPressureDataDao().insert(record);
                    }
                });
                //return to MainActivity
                navigateUpTo(new Intent(AddRecord.this, MainActivity.class));
            }
        });
    }
}
