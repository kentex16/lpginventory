package com.example.lpginventory;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class DataDisplayActivity extends AppCompatActivity {

    private AppDatabase database;
    private TableLayout tableLayout;
    private PersonViewModel personViewModel;
    private LiveData<List<Person>> personsLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_display_activity);

        database = Room.databaseBuilder(this, AppDatabase.class, "person-db")
                .addMigrations(new Migration1to2())
                .build();

        tableLayout = findViewById(R.id.tableLayoutPersons);

        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);

        // Observe changes in the list of persons using LiveData
        personsLiveData = personViewModel.getAllPersons();
        personsLiveData.observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> persons) {
                // The data has changed, update the UI
                populateTable(persons);
            }
        });
    }

    private void populateTable(List<Person> persons) {
        tableLayout.removeAllViews(); // Clear existing table rows

        // Create table header row
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Add header column headers
        addHeaderCell(headerRow, "Edit");
        addHeaderCell(headerRow, "Name");
        addHeaderCell(headerRow, "MOP");
        addHeaderCell(headerRow, "Weight");
        addHeaderCell(headerRow, "Amount");
        addHeaderCell(headerRow, "Container Returned?");
        headerRow.addView(new TextView(this)); // Empty cell for the update button
        tableLayout.addView(headerRow);

        // Create table rows for each person
        for (Person person : persons) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Add data to each column for the current person
            addDataCell(tableRow, person.getName());
            addDataCell(tableRow, person.getModeOfPayment());
            addDataCell(tableRow, String.valueOf(person.getLpgWeight()));
            addDataCell(tableRow, String.valueOf(person.getAmount()));
            addDataCell(tableRow, person.isContainerReturned() ? "Yes" : "No");

            // Add the update button
            addEditIcon(tableRow, person.getId());
            tableLayout.addView(tableRow);
        }
    }

    private void addHeaderCell(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }

    private void addDataCell(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }



    private void addEditIcon(TableRow row, int personId) {
        ImageView editIcon = new ImageView(this);
        editIcon.setImageResource(R.drawable.ic_edit); // Make sure R.drawable.ic_edit is correctly referencing the icon
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the edit icon click here
                Intent intent = new Intent(DataDisplayActivity.this, UpdateActivity.class);
                intent.putExtra("personId", personId);
                startActivity(intent);
            }
        });
        row.addView(editIcon, 0); // Add the edit icon to the first column
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu,menu);
        MenuItem menuItem = menu.findItem (R.id.action_search);
        SearchView searchView = (SearchView)  menuItem.getActionView ();
        searchView.setQueryHint ("Type here to search");

        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText.toLowerCase();

                // Get the list of persons from the LiveData
                List<Person> allPersons = personsLiveData.getValue();

                // Create a filtered list to store matching persons
                List<Person> filteredPersons = new ArrayList<> ();

                // Loop through all persons and add matching ones to the filtered list
                for (Person person : allPersons) {
                    if (person.getName().toLowerCase().contains(query)
                            || person.getModeOfPayment().toLowerCase().contains(query)
                            || String.valueOf(person.getLpgWeight()).contains(query)
                            || String.valueOf(person.getAmount()).contains(query)
                            || (person.isContainerReturned() ? "Yes" : "No").contains(query)) {
                        filteredPersons.add(person);
                    }
                }

                // Update the UI with the filtered list
                populateTable(filteredPersons);

                return true;
            }
        });
        return super.onCreateOptionsMenu (menu);
    }
}
