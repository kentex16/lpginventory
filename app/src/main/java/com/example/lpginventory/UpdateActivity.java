package com.example.lpginventory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class UpdateActivity extends AppCompatActivity {

    private int personId;
    private AppDatabase database;
    private EditText etName, etModeOfPayment, etLpgWeight, etAmount;
    private Button btnUpdate;

    private CheckBox checkBoxContainerReturned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_update);

        // Get the personId from the intent
        personId = getIntent ().getIntExtra ("personId", -1);

        if (personId == -1) {
            // Handle invalid personId, show an error or finish the activity
            Toast.makeText (this, "Invalid personId", Toast.LENGTH_SHORT).show ();
            finish ();
        }

        database = Room.databaseBuilder (this, AppDatabase.class, "person-db")
                .addMigrations (new Migration1to2 ())
                .build ();

        etName = findViewById (R.id.etName);
        etModeOfPayment = findViewById (R.id.etModeOfPayment);
        etLpgWeight = findViewById (R.id.etLpgWeight);
        etAmount = findViewById (R.id.etAmount);
        btnUpdate = findViewById (R.id.btnUpdate);
        checkBoxContainerReturned = findViewById(R.id.checkBoxContainerReturned);

        btnUpdate.setOnClickListener (v -> {
            updatePerson ();
        });

        // Fetch the person details and populate the UI with the existing data
        fetchPersonDetails (personId);
    }

    private void fetchPersonDetails(int personId) {
        AsyncTask.execute (new Runnable () {
            @Override
            public void run() {
                // Access the database on a background thread
                PersonDao personDao = database.personDao ();
                Person person = personDao.getPersonById (personId);

                // Update the UI with the retrieved person data (you can use runOnUiThread() to update UI elements)
                runOnUiThread (new Runnable () {
                    @Override
                    public void run() {
                        // Update UI elements with person data here
                        if (person != null) {
                            etName.setText (person.getName ());
                            etModeOfPayment.setText (person.getModeOfPayment ());
                            etLpgWeight.setText (String.valueOf (person.getLpgWeight ()));
                            etAmount.setText (String.valueOf (person.getAmount ()));
                            checkBoxContainerReturned.setChecked(person.isContainerReturned());
                        } else {
                            // Handle case where person is not found, show an error or finish the activity
                            Toast.makeText (UpdateActivity.this, "Person not found", Toast.LENGTH_SHORT).show ();
                            finish ();
                        }
                    }
                });
            }
        });
    }


    private void updatePerson() {
        String name = etName.getText ().toString ().trim ();
        String modeOfPayment = etModeOfPayment.getText ().toString ().trim ();
        double lpgWeight = Double.parseDouble (etLpgWeight.getText ().toString ().trim ());
        double amount = Double.parseDouble (etAmount.getText ().toString ().trim ());

        // Perform validation on the input data if needed

        // Update the person details in the database on a background thread
        AsyncTask.execute (new Runnable () {
            @Override
            public void run() {
                PersonDao personDao = database.personDao ();
                Person person = personDao.getPersonById (personId);

                if (person != null) {
                    person.setName (name);
                    person.setModeOfPayment (modeOfPayment);
                    person.setLpgWeight (lpgWeight);
                    person.setAmount (amount);
                    person.setContainerReturned(checkBoxContainerReturned.isChecked());

                    // Perform the actual update operation
                    personDao.update (person);

                    // Show a success message or handle the update completion on the UI thread
                    runOnUiThread (new Runnable () {
                        @Override
                        public void run() {
                            Toast.makeText (UpdateActivity.this, "Person details updated successfully", Toast.LENGTH_SHORT).show ();

                            // Finish the activity after updating the person
                            finish ();
                        }
                    });
                } else {
                    // Handle case where person is not found, show an error or finish the activity on the UI thread
                    runOnUiThread (new Runnable () {
                        @Override
                        public void run() {
                            Toast.makeText (UpdateActivity.this, "Person not found", Toast.LENGTH_SHORT).show ();
                            finish ();
                        }
                    });
                }
            }
        });
    }
}