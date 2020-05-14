package com.example.diary;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Database: Contains the database holder and serves
//as the main access point for the underlying connection to your app's persisted, relational data.
@Database(entities = {BloodPressureData.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BloodPressureDataDao bloodPressureDataDao();

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE BloodPressureData " + " ADD COLUMN dateAndTime TEXT");
        }
    };

    //delete from table column dateAndTime and add columns dateOfRecord, timeOfRecord, pulse
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void  migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE BloodPressureData_new (bloodPressureDataId INTEGER NOT NULL, " +
                    "systolicPressure INTEGER NOT NULL, " +
                    "diastolicPressure INTEGER NOT NULL, " +
                    "PRIMARY KEY(bloodPressureDataId))");
            database.execSQL("INSERT INTO BloodPressureData_new (bloodPressureDataId,systolicPressure, diastolicPressure) " +
                    "SELECT bloodPressureDataId, systolicPressure, diastolicPressure FROM BloodPressureData");
            database.execSQL("DROP TABLE bloodPressureData");
            database.execSQL("ALTER TABLE BloodPressureData_new RENAME TO BloodPressureData");

            database.execSQL("ALTER TABLE BloodPressureData ADD COLUMN dateOfRecord TEXT");
            database.execSQL("ALTER TABLE BloodPressureData ADD COLUMN timeOfRecord TEXT");
            database.execSQL("ALTER TABLE BloodPressureData ADD COLUMN pulse INTEGER NOT NULL DEFAULT 0");
        }
    };
};
