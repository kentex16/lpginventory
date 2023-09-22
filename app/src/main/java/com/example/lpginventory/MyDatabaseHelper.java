package com.example.lpginventory;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    // Define your table name and columns here
    private static final String TABLE_CREDITS = "credits";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";

    // SQL statement to create the table
    private static final String SQL_CREATE_TABLE_CREDITS =
            "CREATE TABLE " + TABLE_CREDITS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TOTAL_AMOUNT + " REAL" +
                    ")";

    public MyDatabaseHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database tables
        db.execSQL (SQL_CREATE_TABLE_CREDITS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed (not shown in this example)
    }

    public void updateTotalAmount(double newTotalAmount) {
        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues values = new ContentValues ();
        values.put (COLUMN_TOTAL_AMOUNT, newTotalAmount);
        db.update (TABLE_CREDITS, values, null, null);
        db.close ();
    }
}
