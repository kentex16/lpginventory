package com.example.lpginventory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class ResetDetailsActivity extends AppCompatActivity {

    private AppDatabase database; // Add this line to declare the database variable

    private TextView nameTextView, mopTextView, weightTextView, amountTextView, containerTextView;



    private class RetrieveResetDataTask extends AsyncTask<String, Void, ResetData> {

        @Override
        protected ResetData doInBackground(String... resetNames) {
            String resetName = resetNames[0];
            return database.personDao().getResetDataByName(resetName);
        }

        @Override
        protected void onPostExecute(ResetData resetData) {
            if (resetData != null) {
                nameTextView.setText("Name: " + resetData.getName());
                mopTextView.setText("MOP: " + resetData.getMop());
                weightTextView.setText("Weight: " + resetData.getWeight());
                amountTextView.setText("Amount: " + resetData.getAmount());
                containerTextView.setText("Container: " + (resetData.isContainerReturned() ? "Returned" : "Not Returned"));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_details);

        database = Room.databaseBuilder(this, AppDatabase.class, "person-db")
                .addMigrations(new Migration1to2())
                .fallbackToDestructiveMigration()
                .build();

        nameTextView = findViewById(R.id.nameTextView);
        mopTextView = findViewById(R.id.mopTextView);
        weightTextView = findViewById(R.id.weightTextView);
        amountTextView = findViewById(R.id.amountTextView);
        containerTextView = findViewById(R.id.containerTextView);

        String resetName = getIntent().getStringExtra("resetName");

        new RetrieveResetDataTask().execute(resetName);

        // Fetch reset data using resetName and populate the TextViews
        ResetData resetData = getResetDataFromStorage(resetName);
        if (resetData != null) {
            nameTextView.setText("Name: " + resetData.getName());
            mopTextView.setText("MOP: " + resetData.getMop());
            weightTextView.setText("Weight: " + resetData.getWeight());
            amountTextView.setText("Amount: " + resetData.getAmount());
            containerTextView.setText("Container: " + (resetData.isContainerReturned() ? "Returned" : "Not Returned"));
        }
    }

    private ResetData getResetDataFromStorage(String resetName) {
        ResetData resetData = database.personDao().getResetDataByName(resetName);
        return resetData;
    }
}
