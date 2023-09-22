package com.example.lpginventory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class DeleteActivity extends AppCompatActivity {

    private AppDatabase database;
    private EditText etNameToDelete;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        database = Room.databaseBuilder(this, AppDatabase.class, "person-db")
                .addMigrations(new Migration1to2())
                .build();

        etNameToDelete = findViewById(R.id.etPersonName);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> {
            String nameToDelete = etNameToDelete.getText().toString().trim();
            deletePersonData(nameToDelete);
        });
    }

    private void deletePersonData(String nameToDelete) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Access the database on a background thread
                PersonDao personDao = database.personDao();
                Person person = personDao.getPersonByName(nameToDelete);

                if (person != null) {
                    // Delete the person from the database
                    personDao.delete(person);

                    // Show a success message or handle the deletion completion on the UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteActivity.this, "Person data deleted successfully", Toast.LENGTH_SHORT).show();

                            // Finish the activity after deleting the person
                            finish();
                        }
                    });
                } else {
                    // Handle case where person with the given name is not found
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteActivity.this, "Person with the given name not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

