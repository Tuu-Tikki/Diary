package com.example.diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity: Represents a table within the database.
@Entity
public class BloodPressureData {
    @PrimaryKey (autoGenerate = true)
    public int bloodPressureDataId;

    @ColumnInfo(name = "dateAndTime")
    public String dateAndTime;

    @ColumnInfo(name = "systolicPressure")
    public int systolicPressure;

    @ColumnInfo(name = "diastolicPressure")
    public int diastolicPressure;
}
