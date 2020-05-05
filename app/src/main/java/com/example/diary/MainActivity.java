package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import static com.example.diary.AppDatabase.MIGRATION_2_3;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "BloodPressureRecords")
                .addMigrations(MIGRATION_2_3)
                .build();

        //Read from db last 5 records
        final int[] recordInt = new int[5];
        final String recordString;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    String recordString = db.bloodPressureDataDao().getCertainRecord(i).dateAndTime;
                    TextView cellDate = findViewById(R.id.column1row1);
                    cellDate.setText(recordString);
                    recordInt[i] = db.bloodPressureDataDao().getCertainRecord(i).systolicPressure;
                    recordString = Integer.toString(recordInt[i]);
                    TextView cellSystolic = findViewById(R.id.column2row1);
                    cellSystolic.setText(recordString);
                    recordInt[i] = db.bloodPressureDataDao().getCertainRecord(i).diastolicPressure;
                    recordString = Integer.toString(recordInt[i]);
                    TextView cellDiastolic = findViewById(R.id.column3row1);
                    cellDiastolic.setText(recordString);
                }
            }
        });

        final Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRecord.class));
            }
        });
    }
}

