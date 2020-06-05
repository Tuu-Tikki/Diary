package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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

        //the return arrow on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create or open a database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "BloodPressureRecords")
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build();

        //prefill the date and the time fields
        final EditText date = findViewById(R.id.date);
        final EditText time = findViewById(R.id.time);
        final Calendar calendar = Calendar.getInstance();
        String dateFormatString = "dd.MM.yyyy";
        String timeFormatString = "HH:mm";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        date.setText(dateFormat.format(calendar.getTime()));
        final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);
        time.setText(timeFormat.format(calendar.getTime()));

        //the date field is filled with a calendar by user
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

        //the time field is filled by user with TimePickerDialog
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


        //declaring variables for saving a new record
        final EditText systolicPressure = findViewById(R.id.addSystolicPressure);
        final EditText diastolicPressure = findViewById(R.id.addDiastolicPressure);
        final EditText pulse = findViewById(R.id.addPulse);
        final BloodPressureData record = new BloodPressureData();

        //check fields addPulse/addSystolicPressure/addDiastolicPressure with TextWatcher class
        pulse.addTextChangedListener(new CheckMax());
        systolicPressure.addTextChangedListener(new CheckMax());
        diastolicPressure.addTextChangedListener(new CheckMax());

        //make error message with setError after the data entering
        //for the pulse (heart rate) field
        final TextInputLayout errorPulse = (TextInputLayout) findViewById(R.id.pulse_text_input_layout);
        errorPulse.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pulse.getText().toString().equals("") || Integer.parseInt(pulse.getText().toString())==0) {
                        errorPulse.setError(getString(R.string.error_pulse));
                        errorPulse.setErrorEnabled(true);
                    } else {
                        errorPulse.setErrorEnabled(false);
                    }
                }
            }
        });
        //for the systolic pressure field
        final TextInputLayout errorSysPressure = (TextInputLayout) findViewById(R.id.systolicPressure_text_input_layout);
        errorSysPressure.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (systolicPressure.getText().toString().equals("")
                            || Integer.parseInt(systolicPressure.getText().toString())==0) {
                        errorSysPressure.setError(getString(R.string.error_systolic_pressure));
                        errorSysPressure.setErrorEnabled(true);
                    } else {
                        errorSysPressure.setErrorEnabled(false);
                    }
                }
            }
        });
        //for the diastolic pressure field
        final TextInputLayout errorDiasPressure = (TextInputLayout) findViewById((R.id.diastolicPressure_text_input_layout));
        errorDiasPressure.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (diastolicPressure.getText().toString().equals("")
                            || Integer.parseInt(diastolicPressure.getText().toString())==0) {
                        errorDiasPressure.setError(getString(R.string.error_diastolic_pressure));
                        errorDiasPressure.setErrorEnabled(true);
                    }   else {
                        errorDiasPressure.setErrorEnabled(false);
                    }
                }
            }
        });

        //save new record in db with previously check if the fields are filled correctly
        //the save button below
        final Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRecord(systolicPressure, diastolicPressure, pulse)) {
                    record.systolicPressure = Integer.parseInt(systolicPressure.getText().toString());
                    record.diastolicPressure = Integer.parseInt(diastolicPressure.getText().toString());
                    record.dateOfRecord = date.getText().toString();
                    record.timeOfRecord = time.getText().toString();
                    record.pulse = Integer.parseInt(pulse.getText().toString());
                    AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.bloodPressureDataDao().insert(record);
                    }
                    });
                    //return to MainActivity
                    navigateUpTo(new Intent(AddRecord.this, MainActivity.class));
                } else {
                    //floating message about the error without writing the new record in the database
                    Context context = getApplicationContext();
                    CharSequence message = getString(R.string.error_message);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, message, duration);
                    toast.show();
                }
            }
        });
        //the save button on the toolbar
        final ImageButton toolbarButton = findViewById(R.id.save_button_toolbar);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRecord(systolicPressure, diastolicPressure, pulse)) {
                    record.systolicPressure = Integer.parseInt(systolicPressure.getText().toString());
                    record.diastolicPressure = Integer.parseInt(diastolicPressure.getText().toString());
                    record.dateOfRecord = date.getText().toString();
                    record.timeOfRecord = time.getText().toString();
                    record.pulse = Integer.parseInt(pulse.getText().toString());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.bloodPressureDataDao().insert(record);
                        }
                    });
                    //return to MainActivity
                    navigateUpTo(new Intent(AddRecord.this, MainActivity.class));
                } else {
                    Context context = getApplicationContext();
                    CharSequence message = "Error! All fields should be filled!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, message, duration);
                    toast.show();
                }
            }
        });
    }

    //implement CheckMax for checking if the max amount was exceeded
    class CheckMax implements TextWatcher {
        public void afterTextChanged(Editable s) {
            try {
                if(Integer.parseInt(s.toString()) > 300)
                    s.replace(0, s.length(), "300");
            }
            catch(NumberFormatException nfe){}
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used, details on text just before it changed
            // used to track in detail changes made to text, e.g. implement an undo
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not used, details on text at the point change made
        }
    }

    //check if the pressure and pulse fields are empty or filled with 0
    public boolean checkRecord(EditText sPressure, EditText dPressure, EditText pulse) {
        boolean notEmpty;
        if ((sPressure.getText().toString().equals("") || Integer.parseInt(sPressure.getText().toString())==0)
            || (dPressure.getText().toString().equals("") || Integer.parseInt(dPressure.getText().toString())==0)
            || (pulse.getText().toString().equals("") || Integer.parseInt(pulse.getText().toString())==0)) {
            notEmpty = false;
        } else {
            notEmpty = true;
        }
        return notEmpty;
    }
}
