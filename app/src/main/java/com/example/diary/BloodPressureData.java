package com.example.diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity: Represents a table within the database.
@Entity
public class BloodPressureData {
    @PrimaryKey (autoGenerate = true)
    public int bloodPressureDataId;

    @ColumnInfo(name = "dateOfRecord")
    public String dateOfRecord;

    @ColumnInfo(name = "timeOfRecord")
    public String timeOfRecord;

    @ColumnInfo(name = "systolicPressure")
    public int systolicPressure;

    @ColumnInfo(name = "diastolicPressure")
    public int diastolicPressure;

    @ColumnInfo(name = "pulse")
    public int pulse;
}
