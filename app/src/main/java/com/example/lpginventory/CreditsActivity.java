package com.example.lpginventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CreditsActivity extends AppCompatActivity implements CreditPersonAdapter.CreditPersonListener {

    private double totalAmount;
    private double updatedTotalAmount = 0.0;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_credits);

        totalAmount = getIntent ().getDoubleExtra ("totalAmount", 0.0);
        dbHelper = new MyDatabaseHelper (this);
        TextView textViewTotalAmount = findViewById (R.id.textViewTotalAmount);
        textViewTotalAmount.setText ("Total Amount: " + String.format ("%.2f", totalAmount));

        List<CreditPerson> creditPersonsList = getIntent ().getParcelableArrayListExtra ("creditPersonsList");
        if (creditPersonsList != null && !creditPersonsList.isEmpty ()) {
            ListView listViewCreditPersons = findViewById (R.id.listViewCreditPersons);
            CreditPersonAdapter adapter = new CreditPersonAdapter (this, creditPersonsList, this);
            listViewCreditPersons.setAdapter (adapter);
        }
    }

    @Override
    public void onCreditPaid(double amount) {
        // Update the "Updated Total Amount" when credit is paid
        updatedTotalAmount += amount;
        TextView textViewUpdatedTotalAmount = findViewById (R.id.textViewUpdatedTotalAmount);
        textViewUpdatedTotalAmount.setText ("Updated Total: " + String.format ("%.2f", updatedTotalAmount));

        // Update the database with the paid amount
        updateDatabaseWithPaidAmount (amount);

        Intent intent = new Intent ();
        intent.putExtra ("creditPaidAmount", amount);
        setResult (RESULT_OK, intent);

        // Show a toast indicating that the payment was successful
        Toast.makeText (this, "Successfully paid.", Toast.LENGTH_SHORT).show ();
    }

    // Helper method to retrieve the "Total Amount" from the intent
    private double getTotalAmountFromIntent() {
        return getIntent ().getDoubleExtra ("totalAmount", 0.0);
    }

    private double getUpdatedTotalAmount(Context context, double paidAmount) {
        AppDatabase database = AppDatabase.getInstance(context);


        Person person = database.personDao().getPersonById(1); // Replace '1' with the appropriate person ID
        double currentTotalAmount = person.getUpdatedTotalAmount();


        double updatedTotalAmount = currentTotalAmount + paidAmount;


        database.personDao().updateUpdatedTotalAmount(1, updatedTotalAmount); // Replace '1' with the appropriate person ID


        person.setUpdatedTotalAmount(updatedTotalAmount);


        return updatedTotalAmount;
    }



    private void updateDatabaseWithPaidAmount(double amount) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase ();

            // Calculate the new total amount after the payment
            double newTotalAmount = totalAmount - amount;

            // Create a ContentValues object to hold the updated values
            ContentValues values = new ContentValues ();
            values.put ("total_amount", newTotalAmount);

            // Update the row in the database table with the new total amount
            String whereClause = "_id = ?"; // Replace "_id" with the primary key column name of your "Credits" table.
            String[] whereArgs = new String[]{"1"}; // Replace "1" with the primary key value for the row you want to update.
            db.update ("Credits", values, whereClause, whereArgs);

            // Close the database
            db.close ();

            // Update the totalAmount variable in the activity to reflect the new total amount
            totalAmount = newTotalAmount;
        } catch (Exception e) {
            e.printStackTrace ();
            Toast.makeText (this, "Failed to update the database.", Toast.LENGTH_SHORT).show ();
        }
    }
}

