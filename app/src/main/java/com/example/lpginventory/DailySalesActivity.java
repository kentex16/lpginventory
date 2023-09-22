        package com.example.lpginventory;

        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.localbroadcastmanager.content.LocalBroadcastManager;
        import androidx.room.Room;

        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.time.LocalTime;
        import java.time.format.DateTimeFormatter;
        import java.time.format.DateTimeParseException;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;
        import java.util.concurrent.TimeUnit;


        public  class DailySalesActivity extends AppCompatActivity {

            private AppDatabase database;
            private TableLayout tableLayout;
            private PersonViewModel personViewModel;
            private LiveData<List<Person>> personsLiveData;
            private List<CreditPerson> creditPersonsList = new ArrayList<> ();
            private PersonDao personDao;

            private Timer midnightTimer;


            private static final int REQUEST_CODE_CREDITS_ACTIVITY = 1;
            private double totalNonCreditAmount = 0.0;
            private double totalAmount;
            private Object salesRecordDao;
            private LocalDate selectedDate;
            private BroadcastReceiver ResetReceiver;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate (savedInstanceState);
                setContentView (R.layout.daily_sales_activity);

                BroadcastReceiver resetReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Update UI elements
                        populateTable(new ArrayList<>()); // Clear the table
                    }
                };
                LocalBroadcastManager.getInstance(this).registerReceiver(
                        resetReceiver,
                        new IntentFilter ("reset_ui_elements")
                );


                LocalDate currentDate = LocalDate.now ();
                LocalTime currentTime = LocalTime.now ();

                String selectedDateStr = getIntent().getStringExtra("selectedDate");

                if (selectedDateStr != null) {
                    try {
                        LocalDate selectedDate = LocalDate.parse(selectedDateStr);
                        // Now you can use the selectedDate
                    } catch (DateTimeParseException e) {
                        // Handle the case where the parsing fails
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case where selectedDateStr is null
                }


                startMidnightTimer ();
                scheduleResetOperation();
                TextView timestampTextView = findViewById(R.id.timestampTextView);


                LocalDateTime currentDateTime = LocalDateTime.now();


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);

                timestampTextView.setText("Timestamp: " + formattedDateTime);

                database = Room.databaseBuilder (this, AppDatabase.class, "person-db")
                        .addMigrations (new Migration1to2 ())
                        .build ();
                personDao = database.personDao ();
                tableLayout = findViewById (R.id.tableLayoutPersons);

                personViewModel = new ViewModelProvider (this).get (PersonViewModel.class);

                // Observe changes in the list of persons using LiveData
                personsLiveData = personViewModel.getAllPersons ();
                observePersonsLiveData ();


                // Check if personsLiveData is not null before calling getTotalCreditAmount
                List<Person> persons = personsLiveData.getValue ();
                if (persons != null) {
                    getTotalCreditAmount (persons);
                }

                // Call the method to get the total credit amount
                personsLiveData.observe (this, new Observer<List<Person>> () {
                    @Override
                    public void onChanged(List<Person> persons) {
                        // The data has changed, update the UI
                        populateTable (persons);

                        // Update the credit persons list whenever the data changes
                        updateCreditPersonsList (persons);
                    }
                });
            }

            private void scheduleResetOperation() {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ResetReceiver.class);
                Log.d("ResetReceiver", "Reset operation triggered");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE // or PendingIntent.FLAG_MUTABLE
                );

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
                // Cancel the midnight timer when the activity is destroyed
                cancelMidnightTimer();
                LocalBroadcastManager.getInstance(this).unregisterReceiver(ResetReceiver);
            }
            private void startMidnightTimer() {
                midnightTimer = new Timer ();
                TimerTask midnightTask = new TimerTask () {
                    @Override
                    public void run() {
                        // Check if it's midnight (12:00 AM)
                        LocalTime currentTime = LocalTime.now ();
                        if (currentTime.getHour () == 0 && currentTime.getMinute () == 0) {
                            runOnUiThread (new Runnable () {
                                @Override
                                public void run() {
                                    resetActivity ();
                                }
                            });
                        }
                    }
                };

                // Schedule the task to run every minute (adjust as needed)
                midnightTimer.scheduleAtFixedRate (midnightTask, 0, TimeUnit.MINUTES.toMillis (1));
            }

            private void cancelMidnightTimer() {
                if (midnightTimer != null) {
                    midnightTimer.cancel ();
                    midnightTimer = null;
                }
            }

            private void resetActivity() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Rename and Reset");
                builder.setMessage("Enter a name for the reset data:");

                final EditText input = new EditText(this);
                builder.setView(input);

                builder.setPositiveButton("Rename and Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String resetName = input.getText().toString().trim();

                        if (!resetName.isEmpty()) {
                            clearDatabase();
                            resetUIElements();

                            // Save resetName to SharedPreferences (or your chosen storage method)
                            saveResetNameToStorage(resetName);

                            // Notify the StorageActivity about the reset
                            Intent intent = new Intent("reset_ui_elements");
                            intent.putExtra("resetName", resetName); // Pass the resetName as an extra
                            LocalBroadcastManager.getInstance(DailySalesActivity.this).sendBroadcast(intent);

                            dialog.dismiss();
                        } else {
                            Toast.makeText(DailySalesActivity.this, "Please enter a valid reset name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }


            private void saveResetNameToStorage(String resetName) {
                List<String> resetNamesList = new ArrayList<>();


                resetNamesList.add(resetName);


            }


            private void resetUIElements() {
                List<Person> emptyPersonList = new ArrayList<>();
                populateTable(emptyPersonList);
                Log.d("ResetReceiver", "Reset operation triggered");
            }

            private void clearDatabase() {
                AsyncTask.execute(() -> {
                    PersonDao personDao = database.personDao();
                    personDao.deleteAll();
                });
            }


            public void populateTable(List<Person> persons) {
                tableLayout.removeAllViews (); // Clear existing table rows

                // Create table header row
                TableRow headerRow = new TableRow (this);
                headerRow.setLayoutParams (new TableRow.LayoutParams (
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                // Add header column headers
                addHeaderCell (headerRow, "Name");
                addHeaderCell (headerRow, "MOP");
                addHeaderCell (headerRow, "Weight");
                addHeaderCell (headerRow, "Amount");
                addHeaderCell (headerRow, "Binalik ang tanke?");
                tableLayout.addView (headerRow);

                // Create table rows for each person
                for (Person person : persons) {
                    TableRow tableRow = new TableRow (this);
                    tableRow.setLayoutParams (new TableRow.LayoutParams (
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));

                    // Add data to each column for the current person
                    addDataCell (tableRow, person.getName ());
                    addDataCell (tableRow, person.getModeOfPayment ());
                    addDataCell (tableRow, String.valueOf (person.getLpgWeight ()));
                    addDataCell (tableRow, String.valueOf (person.getAmount ()));
                    addDataCell (tableRow, person.isContainerReturned () ? "Yes" : "No"); // Display container returned status

                    tableLayout.addView (tableRow);
                }
            }

            private void addHeaderCell(TableRow row, String text) {
                TextView textView = new TextView (this);
                textView.setText (text);
                textView.setTextSize (18);
                textView.setPadding (16, 16, 16, 16);
                row.addView (textView);
            }

            private void addDataCell(TableRow row, String text) {
                TextView textView = new TextView (this);
                textView.setText (text);
                textView.setTextSize (16);
                textView.setPadding (16, 16, 16, 16);
                row.addView (textView);
            }

            public void onCalculateSumClicked(View view) {
                List<CreditPerson> creditPersonsList = new ArrayList<> ();
                totalNonCreditAmount = 0.0; // Reset the total non-credit amount

                // Since we are observing LiveData, we can directly get the updated list from personsLiveData
                List<Person> persons = personsLiveData.getValue ();
                if (persons != null) {
                    for (Person person : persons) {
                        if ("Credit".equalsIgnoreCase (person.getModeOfPayment ())) {
                            creditPersonsList.add (new CreditPerson (person.getName (), person.getAmount ()));
                        } else {
                            totalNonCreditAmount += person.getAmount ();
                        }
                    }
                }

                // Launch CreditsActivity and pass the list of credit persons through an intent.
                Intent intent = new Intent (this, CreditsActivity.class);
                intent.putParcelableArrayListExtra ("creditPersonsList", new ArrayList<> (creditPersonsList));
                intent.putExtra ("totalAmount", totalNonCreditAmount);
                startActivityForResult (intent, REQUEST_CODE_CREDITS_ACTIVITY);
            }

            public void handleCylinderReturned(Person person) {
                // Update the database or perform any necessary actions when the cylinder is returned
                // For demonstration purposes, we assume there is a method to update the daily sales database
                updateDailySalesDatabase (person);

                // Remove the person from the list
                List<Person> persons = personsLiveData.getValue ();
                if (persons != null && persons.contains (person)) {
                    persons.remove (person);
                    populateTable (persons); // Update the UI by populating the table again
                }
            }

            private void updateDailySalesDatabase(Person person) {
                PersonDao personDao = database.personDao ();

                // Perform the update operation
                personDao.update (person);
            }

            private void updateCreditPersonsList(List<Person> persons) {
                creditPersonsList.clear (); // Clear the previous credit persons list

                double totalAmount = 0.0;
                for (Person person : persons) {
                    totalAmount += person.getAmount ();

                    if ("Credit".equalsIgnoreCase (person.getModeOfPayment ())) {
                        creditPersonsList.add (new CreditPerson (person.getName (), person.getAmount ()));
                    }
                }
            }

            private double getTotalCreditAmount(List<Person> persons) {
                double totalAmount = 0.0;
                for (Person person : persons) {
                    if ("Credit".equalsIgnoreCase (person.getModeOfPayment ())) {
                        totalAmount += person.getAmount ();
                    }
                }
                return totalAmount;
            }

            private void handleCreditPaid(double amount) {
                // Get the list of persons from LiveData
                List<Person> persons = personsLiveData.getValue ();
                if (persons != null) {
                    for (Person person : persons) {
                        if ("Credit".equalsIgnoreCase (person.getModeOfPayment ())) {
                            // Reduce the amount for credit persons by the credit paid amount
                            double personAmount = person.getAmount ();
                            personAmount -= amount;
                            if (personAmount <= 0) {
                                // If the credit person's amount is fully paid, change the mode of payment to Cash
                                personAmount = 0;
                                person.setModeOfPayment ("Cash");
                                Toast.makeText (this, "Successfully paid for " + person.getName (), Toast.LENGTH_SHORT).show ();
                            }
                            person.setAmount (personAmount);
                        }
                    }

                    // Update the database with the modified person data
                    updatePersonsInDatabase (persons);
                    populateTable (persons);

                    totalAmount -= amount;
                }
            }

            // New method to update the list of persons in the database
            private void updatePersonsInDatabase(List<Person> persons) {
                AsyncTask.execute (() -> {
                    PersonDao personDao = database.personDao ();
                    personDao.updateAll (persons);
                });
            }

            // Existing method to observe changes in LiveData
            private void observePersonsLiveData() {
                personsLiveData = personViewModel.getAllPersons ();
                personsLiveData.observe (this, new Observer<List<Person>> () {
                    @Override
                    public void onChanged(List<Person> persons) {
                        // The data has changed, update the UI
                        populateTable (persons);

                        // Update the credit persons list whenever the data changes
                        updateCreditPersonsList (persons);


                    }
                });
            }

            // New method to handle the result from CreditsActivity
            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult (requestCode, resultCode, data);
                if (requestCode == REQUEST_CODE_CREDITS_ACTIVITY && resultCode == RESULT_OK && data != null) {
                    double creditPaidAmount = data.getDoubleExtra ("creditPaidAmount", 0.0);
                    if (creditPaidAmount > 0) {
                        // Update the database with the modified amount
                        handleCreditPaid (creditPaidAmount);

                        // Show a toast indicating that the payment was successful
                        Toast.makeText (this, "Successfully paid.", Toast.LENGTH_SHORT).show ();
                    }
                }

            }
            public void showResetDialog(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Reset");
                builder.setMessage("Are you sure you want to reset? This action cannot be undone.");

                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetActivity();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }

