package com.hiit.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapter extends ArrayAdapter {

    List<Date> dates;
    Calendar currentDate;
    List<Events> eventsList;
    LayoutInflater inflater;


    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> eventsList, LayoutInflater inflater) {
        super(context, R.layout.single_cell_callendar);
        this.dates = dates;
        this.currentDate = currentDate;
        this.eventsList = eventsList;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        final int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH);
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);
        final int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);


        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell_callendar, null);
        }

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.calendar_cell_color));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.transparent_gray));
        }

        TextView dayNumberTextView = view.findViewById(R.id.text_view_calendar_day_date);
        dayNumberTextView.setText(String.valueOf(displayDay));
        if (displayDay == currentDay && displayMonth == currentMonth) {
            dayNumberTextView.setTextColor(getContext().getResources().getColor(R.color.calendar_red));
            dayNumberTextView.setTextScaleX((float) 1.4);
        }

        //EVENTS
        TextView numberOfEventsTextView = view.findViewById(R.id.text_view_number_of_events);
        ImageView eventImage = view.findViewById(R.id.image_view_calendar_day_icon);


        if (eventsList.size() > 0) {
            for (Events e : eventsList) {
                if (castStringToInt(e.getEventDay()) == displayDay &&
                        (castStringToInt(e.getEventMonth())) == displayMonth &&
                        castStringToInt(e.getEventYear()) == displayYear) {
                    numberOfEventsTextView.setText(e.getNumberOfEvents() + "x");
                    eventImage.setImageResource(R.drawable.ic_fitness_center_red_24dp);
                }
            }
        }

        return view;
    }

    private int castStringToInt(String numberString) {
        return Integer.valueOf(numberString);
    }


    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }


}
