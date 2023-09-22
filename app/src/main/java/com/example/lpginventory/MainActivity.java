package com.example.lpginventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{



    CardView addCardView, updateCardView, creditsCardView, containersCardView, nothingCardView, exitCardView;


    private void navigateToAnotherSection(Class<?> targetClass) {
        Intent intent = new Intent (MainActivity.this, targetClass);
        startActivity (intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        FirebaseApp.initializeApp (this);
        FirebaseAuth Auth = FirebaseAuth.getInstance ();
        Auth.signInAnonymously ()
                .addOnCompleteListener (this, task -> {
                    if (task.isSuccessful ()) {
                        // Sign in success
                        FirebaseUser user = Auth.getCurrentUser ();
                    } else {
                        // Sign in failed
                    }
                });
        addCardView = findViewById (R.id.add_cardview);
        updateCardView = findViewById (R.id.update_cardview);
        creditsCardView = findViewById (R.id.credits_cardview);
        containersCardView = findViewById (R.id.containers_cardview);
        nothingCardView = findViewById (R.id.nothing_cardview);
        exitCardView = findViewById (R.id.exit_cardview);

        addCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                navigateToAnotherSection (AddActivity.class);
            }
        });

        updateCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                navigateToAnotherSection (DataDisplayActivity.class);
            }
        });

        creditsCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                navigateToAnotherSection (DeleteActivity.class);
            }
        });

        containersCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                navigateToAnotherSection (CylinderActivity.class);
            }
        });

        nothingCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                navigateToAnotherSection (DailySalesActivity.class);
            }
        });

        exitCardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog ();
            }

            private void showExitConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                builder.setTitle ("Close Novero LPG Inventory");
                builder.setMessage ("Are you sure you want to exit?");
                builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish ();
                    }
                });
                builder.setNegativeButton ("No", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                });
                builder.show ();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_calendar) {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_storage) {
            Intent intent = new Intent (this, StorageActivity.class);
            startActivity (intent);
            return true;
        }
        else if (id == R.id.nav_chat) {
            Intent intent = new Intent (this, ChatActivity.class);
            startActivity (intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


