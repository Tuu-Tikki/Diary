package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import java.util.List;

import static com.example.diary.AppDatabase.MIGRATION_2_3;
import static com.example.diary.AppDatabase.MIGRATION_3_4;

public class MainActivity extends AppCompatActivity {
    List<BloodPressureData> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //customized Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mainToolbar);

        //create or open database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "BloodPressureRecords")
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build();

        //use RecyclerView for display the table with records from the database
        final RecyclerView rvRecords = (RecyclerView) findViewById(R.id.records_display);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                records = db.bloodPressureDataDao().getAll();

                AdapterForRecordsDisplay adapter = new AdapterForRecordsDisplay(records);

                rvRecords.setAdapter(adapter);

                rvRecords.setLayoutManager(new LinearLayoutManager(MainActivity.this));}
        });

        //the button for adding a new record in the database
        final Button button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRecord.class));
            }
        });

        //the button to go to alarm and notification activity
        final ImageButton imageButton = findViewById(R.id.create_alarm);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlarmAndNotification.class));
            }
        });
    }
}

