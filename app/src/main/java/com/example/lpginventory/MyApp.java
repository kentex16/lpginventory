package com.example.lpginventory;
import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "inventory_db")
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}