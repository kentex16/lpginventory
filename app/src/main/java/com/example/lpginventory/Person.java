package com.example.lpginventory;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons")
public class Person implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String modeOfPayment;
    private double lpgWeight; // Changed from credits to lpgWeight
    private double amount; // Renamed from paidCredits to amount
    private boolean containerReturned;

    @ColumnInfo(name = "updated_total_amount")
    private double updatedTotalAmount;


    public Person(String name, String modeOfPayment, double lpgWeight, double amount, boolean containerReturned) {
        this.name = name;
        this.modeOfPayment = modeOfPayment;
        this.lpgWeight = lpgWeight;
        this.amount = amount;
        this.containerReturned = containerReturned;
    }

    // Getters and setters for all fields

    protected Person(Parcel in) {
        id = in.readInt ();
        name = in.readString ();
        modeOfPayment = in.readString ();
        lpgWeight = in.readDouble ();
        amount = in.readDouble ();
        containerReturned = in.readByte () != 0;
        updatedTotalAmount = in.readDouble ();
    }

    public static final Creator<Person> CREATOR = new Creator<Person> () {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person (in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public double getLpgWeight() {
        return lpgWeight;
    }

    public void setLpgWeight(double lpgWeight) {
        this.lpgWeight = lpgWeight;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isContainerReturned() {
        return containerReturned;
    }

    public void setContainerReturned(boolean containerReturned) {
        this.containerReturned = containerReturned;
    }

    public boolean isBorrowedCylinder() {
        return !containerReturned;
    }

    public double getUpdatedTotalAmount() {

        return updatedTotalAmount;
    }

    public void setUpdatedTotalAmount(double updatedTotalAmount) {
        this.updatedTotalAmount = updatedTotalAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt (id);
        dest.writeString (name);
        dest.writeString (modeOfPayment);
        dest.writeDouble (lpgWeight);
        dest.writeDouble (amount);
        dest.writeByte ((byte) (containerReturned ? 1 : 0));
        dest.writeDouble (updatedTotalAmount);
    }
}

