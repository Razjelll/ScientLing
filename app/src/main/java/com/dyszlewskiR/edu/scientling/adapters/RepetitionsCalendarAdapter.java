package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Razjelll on 29.12.2016.
 */

public class RepetitionsCalendarAdapter extends ArrayAdapter {

    private static final String TAG = RepetitionsCalendarAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> mDates;
    private Calendar mCurrentDate;
    //TODO lista z powtórkami


    public RepetitionsCalendarAdapter(Context context, int resource, List<Date> dates, Calendar currentDate) {
        super(context, resource);
        mDates = dates;
        mCurrentDate = currentDate;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Date date = mDates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        int dayValue = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = mCurrentDate.get(Calendar.MONTH) + 1;
        int currentYear = mCurrentDate.get(Calendar.YEAR);

        if (view == null) {
            view = mInflater.inflate(R.layout.calendar_cell_layout, parent, false);
        }
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#FF5733"));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView dayTextView = (TextView) view.findViewById(R.id.calendar_day_text_view);
        dayTextView.setText(String.valueOf(dayValue));

        TextView numberRepetitionsTextView = (TextView) view.findViewById(R.id.calendar_number_repetitions_text_view);
        numberRepetitionsTextView.setText("(" + ")");
        //TODO uzupełnić wyświetlanie liczby powtórek

        return view;
    }

    @Override
    public int getCount() {
        return mDates.size();
    }

    @Override
    public Object getItem(int position) {
        return mDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return mDates.indexOf(item);
    }
}
