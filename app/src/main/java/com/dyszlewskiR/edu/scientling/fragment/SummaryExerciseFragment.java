package com.dyszlewskiR.edu.scientling.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.adapters.SummaryRepetitionAdapter;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.dialogs.ExercisesListDialogFragment;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.repetitions.SaveExerciseService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryExerciseFragment extends Fragment {

    private static ExerciseManager mExerciseManager;

    private TextView mNumberWordsTextView;
    private ListView mWordList;
    private Button mRepeatButton;
    private Button mFinishButton;

    private int mLastFragment = -1;

    public SummaryExerciseFragment() {
        // Required empty public constructor
    }

    public static SummaryExerciseFragment newInstance(ExerciseManager exerciseManager) {
        SummaryExerciseFragment fragment = new SummaryExerciseFragment();
        mExerciseManager = exerciseManager;
        return fragment;
    }

    public void setLastFragment(int lastFragment) {
        mLastFragment = lastFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_exercise, null);
        mNumberWordsTextView = (TextView) view.findViewById(R.id.number_words_text_view);
        mWordList = (ListView) view.findViewById(R.id.list);
        mRepeatButton = (Button) view.findViewById(R.id.repeat_button);
        mFinishButton = (Button) view.findViewById(R.id.finish_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int numQuestions = mExerciseManager.getNumQuestions();
        mNumberWordsTextView.setText(String.valueOf(numQuestions));

        SummaryRepetitionAdapter adapter = new SummaryRepetitionAdapter(getActivity(), R.layout.item_summary_exercise, mExerciseManager.getQuestions());
        mWordList.setAdapter(adapter);
        setListeners();
    }

    private void setListeners() {
        mRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putInt("selected", mLastFragment);
                ExercisesListDialogFragment dialog = new ExercisesListDialogFragment();
                dialog.setArguments(arguments);
                dialog.show(getActivity().getFragmentManager(), "TAG");
                ((ExerciseActivity) getActivity()).restart();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), SaveExerciseService.class);
                intent.putParcelableArrayListExtra("list", (ArrayList<Word>) mExerciseManager.getQuestions());
                getActivity().startService(intent);
                getActivity().finish();
            }
        });
    }
}
