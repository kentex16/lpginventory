package com.example.lpginventory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Person.class,ResetData.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private double updatedTotalAmount;

    public abstract PersonDao personDao();




    public static final Migration MIGRATION_1_2 = new Migration (1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Add the new 'mop' column to the 'persons' table
            database.execSQL ("ALTER TABLE persons ADD COLUMN mop TEXT");
        }
    };
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Add the new 'updated_total_amount' column to the 'persons' table
            database.execSQL("ALTER TABLE persons ADD COLUMN updated_total_amount REAL NOT NULL DEFAULT 0.0");
        }
    };

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "person-db")
                    .addMigrations(MIGRATION_1_2) // Add all migrations to the builder
                    .fallbackToDestructiveMigration() // Allow destructive migration for testing
                    .build();
        }
        return instance;
    }

    public double getUpdatedTotalAmount(Context context, double paidAmount) {
        updatedTotalAmount = paidAmount * 2;
        return updatedTotalAmount;
    }


    public static final Executor databaseWriteExecutor = Executors.newSingleThreadExecutor();

}

