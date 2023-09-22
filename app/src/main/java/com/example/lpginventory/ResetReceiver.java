package com.example.lpginventory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import java.util.ArrayList;

public class ResetReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        clearData();

        // Reset UI elements or perform UI-related reset operations
        resetUIElements();
    }


    private void clearData() {
        // Clear data in your database or SharedPreferences
        // For example, you might want to clear the list of persons
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "person-db").build();
        PersonDao personDao = database.personDao();
        AsyncTask.execute(() -> {
            personDao.deleteAll();
        });
    }

    private void resetUIElements() {
        Intent intent = new Intent("reset_ui_elements");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        DailySalesActivity activity = (DailySalesActivity) context;
        activity.runOnUiThread(() -> {

            activity.populateTable(new ArrayList<> ()); // Clear the table

        });
    }
}
