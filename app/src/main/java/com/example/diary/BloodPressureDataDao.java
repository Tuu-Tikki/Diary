package com.example.diary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//Data Access Objects (Dao): Contains the methods used for accessing the database.
@Dao
public interface BloodPressureDataDao {
    @Query("SELECT * FROM BloodPressureData")
    List<BloodPressureData> getAll();

    @Query("SELECT * FROM BloodPressureData WHERE BloodPressureDataId = " +
            "(SELECT MAX(bloodPressureDataId) FROM BloodPressureData)")
    BloodPressureData getLastRecord();

    @Query("SELECT * FROM BloodPressureData WHERE BloodPressureDataId = " +
            "((SELECT MAX(bloodPressureDataId) FROM BloodPressureData)-:number)")
    public BloodPressureData getCertainRecord(int number);

    @Insert
    void insert(BloodPressureData record);

    @Delete
    void delete(BloodPressureData record);
}
