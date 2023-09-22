package com.example.lpginventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CalendarActivity extends AppCompatActivity  implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private boolean selectedDateStr;

    @Override
    protected void onCreate (Bundle savedInstancedState)
    {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray (selectedDate);

        CalendarAdapter calendarAdapter= new CalendarAdapter (daysInMonth,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager (getApplicationContext(),7 );
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList <>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth =selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i<=42 ; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add (" ");
            }
            else{
                daysInMonthArray.add(String.valueOf(i -dayOfWeek));
            }
        }
        return daysInMonthArray;
    }
    private String monthYearFromDate (LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String selectedDateStr = selectedDate.format(formatter);
        return date.format(formatter);

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthyearTV);
    }

    public void previousMonthAction (View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();

    }

    public void nextMonthAction (View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();

    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(dayText.equals(""))
        {
            int selectedDay = Integer.parseInt(dayText);

            // Initialize selectedDate
            LocalDate selectedDate = LocalDate.of(this.selectedDate.getYear(), this.selectedDate.getMonth(), selectedDay);

            // Convert selectedDate to a string to pass it as an extra
            String selectedDateStr = selectedDate.toString();

            Intent intent = new Intent(this, DailySalesActivity.class);
            intent.putExtra("selectedDate", selectedDateStr);
            startActivity(intent);

        }
    }
}
