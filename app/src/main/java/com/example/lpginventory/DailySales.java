package com.example.lpginventory;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Calendar;

@Root(name = "DailySales")
public class DailySales {
    @Element(name = "date")
    private Calendar date;

    @Element(name = "salesAmount")
    private double salesAmount;

    public DailySales(Calendar date, double salesAmount) {
        this.date = date;
        this.salesAmount = salesAmount;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
}
