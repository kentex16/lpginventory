package com.example.lpginventory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddActivity extends AppCompatActivity {
    private EditText editTextName;
    private Spinner spinnerModeOfPayment;
    private Spinner spinnerItem;
    private EditText editTextPaidCredits;
    private CheckBox checkBoxContainerReturned;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        database = Room.databaseBuilder(
                this, AppDatabase.class, "person-db"
        ).addMigrations(new Migration1to2()).build();

        editTextName = findViewById(R.id.editTextName);
        spinnerModeOfPayment = findViewById(R.id.spinnerModeOfPayment);
        spinnerItem = findViewById(R.id.spinnerItem);
        editTextPaidCredits = findViewById(R.id.editTextPaidCredits);
        checkBoxContainerReturned = findViewById(R.id.checkBoxContainerReturned);

        ArrayAdapter<CharSequence> adapterModeOfPayment = ArrayAdapter.createFromResource(
                this,
                R.array.mode_of_payment_array,
                android.R.layout.simple_spinner_item
        );
        adapterModeOfPayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModeOfPayment.setAdapter(adapterModeOfPayment);

        ArrayAdapter<CharSequence> adapterItem = ArrayAdapter.createFromResource(this,
                R.array.item_array, android.R.layout.simple_spinner_item);
        adapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(adapterItem);

        Button buttonAddPersonDialog = findViewById(R.id.buttonAddPersonDialog);
        buttonAddPersonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to handle adding a person
                onAddPersonClicked(v);
            }
        });
    }

    public void onAddPersonClicked(View view) {
        // Retrieve the data entered by the user
        String name = editTextName.getText().toString().trim();
        String modeOfPayment = spinnerModeOfPayment.getSelectedItem().toString();
        String selectedSpinnerItem = spinnerItem.getSelectedItem().toString();

        // Remove non-numeric characters from the selectedSpinnerItem
        String numericPart = selectedSpinnerItem.replaceAll("[^\\d.]", "");
        double lpgWeight = Double.parseDouble(numericPart); // Parse the numeric part as a double

        double paidCredits = Double.parseDouble(editTextPaidCredits.getText().toString().trim());
        boolean containerReturned = checkBoxContainerReturned.isChecked();

        // Create a new Person object with the collected data
        Person person = new Person(name, modeOfPayment, lpgWeight, paidCredits, containerReturned);

        // Use AsyncTask to insert the person into the database on a background thread
        new InsertPersonTask(database.personDao()).execute(person);

        // Finish the activity and return to the MainDashboard
        finish();
    }

    private static class InsertPersonTask extends AsyncTask<Person, Void, Void> {
        private PersonDao personDao;

        public InsertPersonTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... persons) {
            personDao.insert(persons[0]);
            return null;
        }
    }
}
