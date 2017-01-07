package com.dyszlewskiR.edu.scientling.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.widgets.RepetitionCalendarView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepetitionsFragment extends Fragment {

    private RepetitionCalendarView mCalendarView;

    public RepetitionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_repetitions, container, false);
        mCalendarView = (RepetitionCalendarView) view.findViewById(R.id.calendar_view);
        return view;
    }
}
