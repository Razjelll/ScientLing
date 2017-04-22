package com.dyszlewskiR.edu.scientling.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;
import com.dyszlewskiR.edu.scientling.services.exercises.KnowExercise;
import com.dyszlewskiR.edu.scientling.services.speech.ISpeechCallback;
import com.dyszlewskiR.edu.scientling.services.speech.SpeechPlayer;
import com.dyszlewskiR.edu.scientling.widgets.SpeechButton;

import java.io.IOException;

public class KnowExerciseFragment extends Fragment implements ISpeechCallback {


    private static ExerciseManager mExerciseManager; //TODO zastanowić się czy te statiki mogą być
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private TextView mTranslationTextView;
    private SpeechButton mSpeechButton;
    private Button mShowAnswerButton;
    private Button mKnowButton;
    private Button mDontKnowButton;
    private Button mAlmostKnowButton;

    private SpeechPlayer mSpeechPlayer;

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
        initSpeechPlayer();
    }

    private void initSpeechPlayer() {
        mSpeechPlayer = new SpeechPlayer(getContext());
        mSpeechPlayer.setCallback(this);
        long setId = Settings.getCurrentSetId(getContext());
        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        VocabularySet currentSet = dataManager.getSetById(setId);
        mSpeechPlayer.setSet(currentSet);
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

        String question = mExerciseManager.getQuestion();
        String recordName = mExerciseManager.getRecordName();
        mSpeechPlayer.setWord(question, recordName);
        mWordTextView.setText(question);
        mTranscriptionTextView.setText(mExerciseManager.getTranscription());
        mTranslationTextView.setText(mExerciseManager.getCorrectAnswer());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_know_exercise, container, false);
        //return inflater.inflate(R.layout.fragment_know_exercise, container, false);
        setupControls(view);
        setListeners();
        return view;
    }

    private void setupControls(View view) {
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mTranslationTextView = (TextView) view.findViewById(R.id.knowTranslation);
        mShowAnswerButton = (Button) view.findViewById(R.id.showAnswerButton);
        mSpeechButton = (SpeechButton) view.findViewById(R.id.speech_button);
        mKnowButton = (Button) view.findViewById(R.id.knowButton);
        mDontKnowButton = (Button) view.findViewById(R.id.dontKnowButton);
        mAlmostKnowButton = (Button) view.findViewById(R.id.almostKnowButton);
    }

    private void setListeners() {
        mShowAnswerButton.setOnClickListener(new ShowAnswerOnClickListener());

        mKnowButton.setOnClickListener(new KnowButtonOnClickListener("Know")); //TODO zmienić na stałe, albo coś takiego
        mDontKnowButton.setOnClickListener(new KnowButtonOnClickListener("DontKnow"));
        mAlmostKnowButton.setOnClickListener(new KnowButtonOnClickListener("AlmostKnow"));

        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpeechButton.setLoading(true);
                try {
                    mSpeechPlayer.speech();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        mSpeechPlayer.release();
        super.onDetach();

    }

    @Override
    public void onSpeechStart() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpeechButton.setLoading(false);
                mSpeechButton.setPauseImage();
            }
        });
    }

    @Override
    public void onSpeechCompleted() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpeechButton.setPlayImage();
            }
        });

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
