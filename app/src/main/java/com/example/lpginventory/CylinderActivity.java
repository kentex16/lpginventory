package com.example.lpginventory;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class CylinderActivity extends AppCompatActivity implements PersonAdapter.OnReturnedButtonClickListener {

    private CylinderReturnedListener cylinderReturnedListener;

    public interface CylinderReturnedListener {
        void onCylinderReturned(Person person);
    }

    private PersonViewModel personViewModel;
    private RecyclerView recyclerViewCylinder;
    private PersonAdapter personAdapter;
    private PersonDao personDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cylinder);

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "person-db")
                .addMigrations(new Migration1to2())
                .build();
        personDao = database.personDao();

        recyclerViewCylinder = findViewById(R.id.recyclerViewCylinder);
        recyclerViewCylinder.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter();
        personAdapter.setOnReturnedButtonClickListener(this);
        recyclerViewCylinder.setAdapter(personAdapter);

        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);

        // Observe changes in the list of persons using LiveData
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> persons) {
                // Filter the list to get persons with borrowed cylinders
                List<Person> borrowedCylinderPersons = getBorrowedCylinderPersons(persons);
                // Update the RecyclerView with the filtered list
                personAdapter.setPersons(borrowedCylinderPersons);
            }
        });

        personAdapter.setOnReturnedButtonClickListener(new PersonAdapter.OnReturnedButtonClickListener() {
            @Override
            public void onCylinderReturned(Person person) {
                // Notify the listener (DailySalesActivity) that the cylinder is returned
                if (cylinderReturnedListener != null) {
                    cylinderReturnedListener.onCylinderReturned(person);
                }
            }
        });
    }

    private List<Person> getBorrowedCylinderPersons(List<Person> persons) {
        List<Person> borrowedCylinderPersons = new ArrayList<>();
        for (Person person : persons) {
            if (person.isBorrowedCylinder()) {
                borrowedCylinderPersons.add(person);
            }
        }
        return borrowedCylinderPersons;
    }

    private void updateDatabaseForReturnedPerson(Person person) {
        // Set the person's containerReturned field to true
        person.setContainerReturned(true);

        Log.d("CylinderActivity", "Updating database for person: " + person.getName() + ", ID: " + person.getId() + ", Container returned: " + person.isContainerReturned());

        // Update the containerReturned field in the database using the PersonDao
        personDao.update(person);

        Log.d("CylinderActivity", "Database update completed.");

        // Notify the adapter about the data change so it can update the RecyclerView
        personAdapter.notifyDataSetChanged();

        // Display a message indicating that the container has been returned
        Toast.makeText(this, "Container returned for: " + person.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCylinderReturned(Person person) {
        updateDatabaseForReturnedPerson(person);
    }

    public void setCylinderReturnedListener(CylinderReturnedListener listener) {
        this.cylinderReturnedListener = listener;
    }
}


