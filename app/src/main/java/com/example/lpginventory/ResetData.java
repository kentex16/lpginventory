package com.example.lpginventory;

import androidx.lifecycle.LiveData;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class ResetData extends LiveData<List<ResetData>> {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String mop;
    public double weight;
    public double amount;
    public boolean containerReturned;


    public ResetData(String name, String mop, double weight, double amount, boolean containerReturned) {
        this.name = name;
        this.mop = mop;
        this.weight = weight;
        this.amount = amount;
        this.containerReturned = containerReturned;
    }

    public String getName() {
        return name;
    }

    public String getMop() {
        return mop;
    }

    public double getWeight() {
        return weight;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isContainerReturned() {
        return containerReturned;
    }
}
