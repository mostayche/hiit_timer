package com.hiit.timer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    ImageButton prevButton, nextButton;
    TextView textViewCurrentDate;
    GridView gridView;

    private static final int MAX_CALENDAR_DAYS = 42;

    Calendar calendar = Calendar.getInstance();
    GridAdapter gridAdapter;
    Context context;
    DataSupport ds;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View calendarView = inflater.inflate(R.layout.fragment_calendar, container, false);

        context = calendarView.getContext();

        nextButton = calendarView.findViewById(R.id.button_next_month);
        prevButton = calendarView.findViewById(R.id.button_previous_month);
        textViewCurrentDate = calendarView.findViewById(R.id.text_view_month);
        gridView = calendarView.findViewById(R.id.grid_view_callendar);
        ds = new DataSupport();
        eventsList = createEventListFromDb();

        setUpCalendar();
        nextMonth();
        previousMonth();

        return calendarView;
    }


    private void previousMonth() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setUpCalendar();
            }
        });
    }

    private void nextMonth() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setUpCalendar();
            }
        });
    }

    private void setUpCalendar() {
        String dateNow = dateFormat.format(calendar.getTime());
        textViewCurrentDate.setText(ds.properDateOnTheScreen(dateNow));
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 2;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new GridAdapter(context, dates, calendar, eventsList, getLayoutInflater());
        gridView.setAdapter(gridAdapter);

        //readEventsPerDayFromDB();

    }

    private List<Events> createEventListFromDb() {
        List<Events> evList = new ArrayList<>();
        DBCalendarHelper dbch = new DBCalendarHelper(context);
        Cursor cursor = dbch.readEventsFromDB();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String event = cursor.getString(1);
            String day = cursor.getString(2);
            String eventNumber = cursor.getString(3);
            String month = cursor.getString(4);
            String year = cursor.getString(5);
            Events ev = new Events(event, day, eventNumber, month, year);
            evList.add(ev);
            cursor.moveToNext();
        }
        cursor.close();
        dbch.close();
        return evList;
    }

}
