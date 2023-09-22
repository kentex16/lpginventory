package com.example.lpginventory.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;

@Entity(tableName = "sales_records")
public class SalesRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private LocalDate date;
    private double totalAmount;

    // Add constructors, getters, setters, and any other methods here

    // Constructor
    public SalesRecord(LocalDate date, double totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
