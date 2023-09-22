package com.example.lpginventory;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lpginventory.AppDatabase;
import com.example.lpginventory.Person;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private PersonDao personDao;
    private LiveData<List<Person>> allPersons;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        personDao = database.personDao();
        allPersons = personDao.getAllPersons();
    }

    // Expose the LiveData containing the list of all persons to the UI
    public LiveData<List<Person>> getAllPersons() {
        return allPersons;
    }

    // Insert a person into the database using a coroutine to ensure it runs on a background thread
    public void insertPerson(Person person) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            personDao.insert(person);
        });
    }

    // Update a person in the database using a coroutine to ensure it runs on a background thread
    public void updatePerson(Person person) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            personDao.update(person);
        });
    }

    // Delete a person from the database using a coroutine to ensure it runs on a background thread
    public void deletePerson(Person person) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            personDao.delete(person);
        });
    }

    // New method to get persons by mode of payment (MOP)

}
