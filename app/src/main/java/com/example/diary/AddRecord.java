package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.diary.AppDatabase.MIGRATION_2_3;
import static com.example.diary.AppDatabase.MIGRATION_3_4;

public class AddRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //custimized toolbar
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


        final Button button = findViewById(R.id.saveButton);
        final EditText systolicPressure = findViewById(R.id.addSystolicPressure);
        final EditText diastolicPressure = findViewById(R.id.addDiastolicPressure);

        //prefill date and time fields
        final EditText date = findViewById(R.id.date);
        final EditText time = findViewById(R.id.time);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(dateFormat.format(calendar.getTime()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        time.setText(timeFormat.format(calendar.getTime()));

        final EditText pulse = findViewById(R.id.addPulse);
        final BloodPressureData record = new BloodPressureData();

        //save new record in db
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
