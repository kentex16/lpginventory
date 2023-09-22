package com.example.lpginventory;

import androidx.appcompat.app.AppCompatActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.Calendar;

public class Main extends AppCompatActivity {
    public static void main(String[] args) {
        // Create a new DailySales object
        Calendar date = Calendar.getInstance();
        double salesAmount = 100.0;
        DailySales dailySales = new DailySales(date, salesAmount);

        // Serialize the DailySales object to XML
        Serializer serializer = new Persister();
        File xmlFile = new File("daily_sales.xml");

        try {
            serializer.write(dailySales, xmlFile);
            System.out.println("DailySales object serialized and saved to daily_sales.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
