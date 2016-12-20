package com.dyszlewskiR.edu.scientling.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryExerciseFragment extends Fragment {

    private static ExerciseManager mExerciseManager;

    public SummaryExerciseFragment() {
        // Required empty public constructor
    }


    public static SummaryExerciseFragment newInstance(ExerciseManager exerciseManager) {
        SummaryExerciseFragment fragment = new SummaryExerciseFragment();
        mExerciseManager = exerciseManager;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
