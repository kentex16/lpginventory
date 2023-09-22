package com.example.lpginventory;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonCredit implements Parcelable {
    private String name;
    private double amountCredited;

    public PersonCredit(String name, double amountCredited) {
        this.name = name;
        this.amountCredited = amountCredited;
    }

    public String getName() {
        return name;
    }

    public double getAmountCredited() {
        return amountCredited;
    }

    // Implement Parcelable methods here

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(amountCredited);
    }

    public static final Parcelable.Creator<PersonCredit> CREATOR = new Parcelable.Creator<PersonCredit>() {
        @Override
        public PersonCredit createFromParcel(Parcel source) {
            return new PersonCredit(source);
        }

        @Override
        public PersonCredit[] newArray(int size) {
            return new PersonCredit[size];
        }
    };

    private PersonCredit(Parcel source) {
        name = source.readString();
        amountCredited = source.readDouble();
    }
}
