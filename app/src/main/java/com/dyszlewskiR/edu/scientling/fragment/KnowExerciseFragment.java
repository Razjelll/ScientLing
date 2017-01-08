package com.dyszlewskiR.edu.scientling.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;
import com.dyszlewskiR.edu.scientling.services.exercises.KnowExercise;

public class KnowExerciseFragment extends Fragment {


    private static ExerciseManager mExerciseManager; //TODO zastanowić się czy te statiki mogą być
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private TextView mTranslationTextView;
    private Button mSpeechButton;
    private Button mShowAnswerButton;
    private Button mKnowButton;
    private Button mDontKnowButton;
    private Button mAlmostKnowButton;

    public KnowExerciseFragment() {
    }

    public static KnowExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseDirection language) {
        KnowExerciseFragment fragment = new KnowExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new KnowExercise());
        mExerciseManager.setExerciseLanguage(language);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void toAnswer(String answer) //TODO może będzie trzeba zmienić na inta, aby było zgodne z interfejsem
    {
        mExerciseManager.checkAnswer(answer);
    }

    private void showAnswer() {
        mShowAnswerButton.setVisibility(View.GONE);

        mKnowButton.setVisibility(View.VISIBLE);
        mDontKnowButton.setVisibility(View.VISIBLE);
        mAlmostKnowButton.setVisibility(View.VISIBLE);
        mTranslationTextView.setVisibility(View.VISIBLE);
    }

    private void showQuestion() //TODO może być konieczna zmiana nazwy
    {
        mShowAnswerButton.setVisibility(View.VISIBLE);

        mKnowButton.setVisibility(View.GONE);
        mDontKnowButton.setVisibility(View.GONE);
        mAlmostKnowButton.setVisibility(View.GONE);
        mTranslationTextView.setVisibility(View.INVISIBLE);

        mWordTextView.setText(mExerciseManager.getQuestion());
        mTranscriptionTextView.setText(mExerciseManager.getTranscription());
        mTranslationTextView.setText(mExerciseManager.getCorrectAnswer());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_know_exercise, container, false);
        //return inflater.inflate(R.layout.fragment_know_exercise, container, false);
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mTranslationTextView = (TextView) view.findViewById(R.id.knowTranslation);
        mShowAnswerButton = (Button) view.findViewById(R.id.showAnswerButton);
        mSpeechButton = (Button) view.findViewById(R.id.knowSpeechButton);
        mKnowButton = (Button) view.findViewById(R.id.knowButton);
        mDontKnowButton = (Button) view.findViewById(R.id.dontKnowButton);
        mAlmostKnowButton = (Button) view.findViewById(R.id.almostKnowButton);

        mShowAnswerButton.setOnClickListener(new ShowAnswerOnClickListener());

        mKnowButton.setOnClickListener(new KnowButtonOnClickListener("Know")); //TODO zmienić na stałe, albo coś takiego
        mDontKnowButton.setOnClickListener(new KnowButtonOnClickListener("DontKnow"));
        mAlmostKnowButton.setOnClickListener(new KnowButtonOnClickListener("AlmostKnow"));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showQuestion();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected class KnowButtonOnClickListener implements View.OnClickListener {
        private String mValue;

        public KnowButtonOnClickListener(String value) {
            mValue = value;
        }

        @Override
        public void onClick(View v) {
            toAnswer(mValue);
            mExerciseManager.nextQuestion();
            ((ExerciseActivity) getActivity()).updateQuestion();
            showQuestion();
        }
    }

    protected class ShowAnswerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showAnswer();
        }
    }
}
