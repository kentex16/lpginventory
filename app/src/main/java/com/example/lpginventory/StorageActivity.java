package com.example.lpginventory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageActivity extends AppCompatActivity {

    private List<String> resetNamesList; // Declare the list to store reset names
    private LinearLayoutCompat storageLayout;
    private ResetReceiver resetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_activity);

        ListView storageListView = findViewById(R.id.storageListView);
        String resetName = getIntent().getStringExtra("resetName");
        // Initialize resetNamesList and populate it with data from storage
        resetNamesList = new ArrayList<>();


        // Create the adapter and set it to the ListView
        CardListAdapter adapter = new CardListAdapter(this, resetNamesList);
        storageListView.setAdapter(adapter);
        resetReceiver = new ResetReceiver();
        storageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resetName = resetNamesList.get(position);
                // Handle item click, e.g., show details in a dialog
                showResetDetailsDialog(resetName);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(
                resetReceiver,
                new IntentFilter("reset_ui_elements")
        );

        // Initialize resetNamesList and populate it with data from storage
        resetNamesList = retrieveResetNamesFromStorage();

        storageLayout = findViewById(R.id.storageLayout);

        for (String name : resetNamesList) {
            addResetCard(name);
        }
    }

    private void showResetDetailsDialog(String resetName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Details");
        builder.setMessage("Reset Name: " + resetName); // You can add more details here

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private class ResetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update UI elements
            refreshResetCards(); // Update/reset your reset cards here
        }
    }

    private void refreshResetCards() {
        // Clear existing card views
        storageLayout.removeAllViews();

        // Retrieve reset names from storage and populate reset cards
        resetNamesList = retrieveResetNamesFromStorage();
        for (String name : resetNamesList) {
            addResetCard(name);
        }
    }


    private void addResetCard(String resetName) {
        CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.card_reset_activity, null);
        TextView resetTitleTextView = cardView.findViewById(R.id.resetTitleTextView);
        TextView resetDetailsTextView = cardView.findViewById(R.id.resetDetailsTextView);

        resetTitleTextView.setText(resetName);
        resetDetailsTextView.setText("Click to view details"); // You can customize this text as needed

        // Set a click listener for the card view
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the ResetDetailsActivity with the corresponding reset name
                Intent resetDetailsIntent = new Intent(StorageActivity.this, ResetDetailsActivity.class);
                resetDetailsIntent.putExtra("resetName", resetName);
                startActivity(resetDetailsIntent);
            }
        });

        // Add the card view to the storage layout
        storageLayout.addView(cardView);
        saveResetNameToStorage(resetName);
    }

    private List<String> retrieveResetNamesFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("reset_names", Context.MODE_PRIVATE);
        Set<String> resetNamesSet = sharedPreferences.getStringSet("reset_names_set", new HashSet<> ());
        return new ArrayList<>(resetNamesSet);
    }

    private void saveResetNameToStorage(String resetName) {
        SharedPreferences sharedPreferences = getSharedPreferences("reset_names", Context.MODE_PRIVATE);
        Set<String> resetNamesSet = sharedPreferences.getStringSet("reset_names_set", new HashSet<>());
        resetNamesSet.add(resetName);
        sharedPreferences.edit().putStringSet("reset_names_set", resetNamesSet).apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(resetReceiver);
    }
}

