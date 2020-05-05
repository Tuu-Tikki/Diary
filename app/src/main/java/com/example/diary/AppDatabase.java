package com.example.diary;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Database: Contains the database holder and serves
//as the main access point for the underlying connection to your app's persisted, relational data.
@Database(entities = {BloodPressureData.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BloodPressureDataDao bloodPressureDataDao();

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE BloodPressureData " + " ADD COLUMN dateAndTime TEXT");
        }
    };
};
