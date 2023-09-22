package com.example.lpginventory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "credits")
public class Credits {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "total_amount")
    public double totalAmount;
    // ... other columns if needed
}
