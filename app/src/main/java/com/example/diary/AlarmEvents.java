package com.example.diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlarmEvents {
    @PrimaryKey (autoGenerate = true)
    public int alarmEventId;

    @ColumnInfo (name = "timeOfAlarm")
    public String timeOfAlarm;

    @ColumnInfo (name = "alarmForMeasurement")
    public boolean measurement;

    @ColumnInfo (name = "alarmForPills")
    public boolean pills;
}
