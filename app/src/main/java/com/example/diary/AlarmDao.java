package com.example.diary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM AlarmEvents")
    List<AlarmEvents> getAll();

    @Query("SELECT * FROM AlarmEvents WHERE alarmEventId = (SELECT MAX(alarmEventId) FROM AlarmEvents)")
    AlarmEvents getLast();

    @Insert
    void insert(AlarmEvents event);

    @Delete
    void delete(AlarmEvents event);
}
