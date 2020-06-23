package com.example.diary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AlarmEvents.class}, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();
}
