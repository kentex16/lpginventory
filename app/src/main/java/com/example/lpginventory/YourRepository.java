package com.example.lpginventory;

import android.content.Context;
import android.os.AsyncTask;

public class YourRepository {
    private PersonDao personDao;

    public YourRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance (context);
        personDao = appDatabase.personDao ();
    }

    // Example method to insert a person into the database
    public void insertPerson(Person person) {
        new InsertPersonTask (personDao).execute (person);
    }

    private static class InsertPersonTask extends AsyncTask<Person, Void, Void> {
        private PersonDao personDao;

        public InsertPersonTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... persons) {
            personDao.insert (persons[0]);
            return null;
        }

        public void execute(Person person) {
        }
    }
}