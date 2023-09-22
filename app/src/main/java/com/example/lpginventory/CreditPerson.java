package com.example.lpginventory;
import android.os.Parcel;
import android.os.Parcelable;

public class CreditPerson implements Parcelable {
    private String name;
    private double amount;
    private boolean isPaid;


    public CreditPerson(String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.isPaid = false; // Default value is false (not paid)
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }


    // Parcelable implementation
    protected CreditPerson(Parcel in) {
        name = in.readString ();
        amount = in.readDouble ();
        isPaid = in.readByte () != 0;
    }

    public static final Creator<CreditPerson> CREATOR = new Creator<CreditPerson> () {
        @Override
        public CreditPerson createFromParcel(Parcel in) {
            return new CreditPerson (in);
        }

        @Override
        public CreditPerson[] newArray(int size) {
            return new CreditPerson[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString (name);
        dest.writeDouble (amount);
        dest.writeByte ((byte) (isPaid ? 1 : 0));
    }

}