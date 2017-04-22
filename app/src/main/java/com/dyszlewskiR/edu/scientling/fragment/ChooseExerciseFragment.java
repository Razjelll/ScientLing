package com.dyszlewskiR.edu.scientling.fragment;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ChooseExercise;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;
import com.dyszlewskiR.edu.scientling.services.speech.ISpeechCallback;
import com.dyszlewskiR.edu.scientling.services.speech.SpeechPlayer;
import com.dyszlewskiR.edu.scientling.utils.resources.Colors;
import com.dyszlewskiR.edu.scientling.widgets.SpeechButton;

import java.io.IOException;


public class ChooseExerciseFragment extends Fragment implements ISpeechCallback {

    private final static String TAG = "ChooseExerciseFragment";
    private static ExerciseManager mExerciseManager;
    FragmentTransaction fragmentTransaction;
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private SpeechButton mSpeechButton;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private Button mAnswerButton5;
    private Button mAnswerButton6;
    private Button mNextButton;
    private Button moreButton;
    private int mNumAnswersButtons;
    private Button[] mAnswersButtons;
    private boolean mCanAnswer;
    private View mFragmentView;

    private SpeechPlayer mSpeechPlayer;

    public ChooseExerciseFragment() {

    }

    public static ChooseExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseDirection language) {
        ChooseExerciseFragment fragment = new ChooseExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new ChooseExercise());
        mExerciseManager.setExerciseLanguage(language);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mCanAnswer = true;

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

    private void createAnswersButtonArray() {
        mAnswersButtons = new Button[mNumAnswersButtons];
        mAnswersButtons[0] = mAnswerButton1;
        mAnswersButtons[1] = mAnswerButton2;
        if (mNumAnswersButtons > 2)
            mAnswersButtons[2] = mAnswerButton3;
        if (mNumAnswersButtons > 3)
            mAnswersButtons[3] = mAnswerButton4;
        if (mNumAnswersButtons > 4)
            mAnswersButtons[4] = mAnswerButton5;
        if (mNumAnswersButtons > 5)
            mAnswersButtons[5] = mAnswerButton6;
    }

    /**
     * Umyślnie niewstawiono breaków
     */
    private void hideNonUseAnswersButtons() {
        switch (mNumAnswersButtons) {
            case 2:
                mAnswerButton3.setVisibility(View.GONE);
            case 3:
                mAnswerButton4.setVisibility(View.GONE);
            case 4:
                mAnswerButton5.setVisibility(View.GONE);
            case 5:
                mAnswerButton6.setVisibility(View.GONE);
        }
    }

    private void addAnswersButtonsListeners() {
        for (int i = 0; i < mNumAnswersButtons; i++) {
            mAnswersButtons[i].setOnClickListener(new AnswerOnClickListener(i));
        }
    }

    private void showQuestion() {
        String question = mExerciseManager.getQuestion();
        if (question != null) {
            mWordTextView.setText(question);
            //String transcription = mExerciseManager.getTranscription();
            //mTranscriptionTextView.setText(transcription);

            setSpeechPlayer(question);
            Animation animation = AnimationUtils.makeInAnimation(getActivity(), false);
            mFragmentView.startAnimation(animation);
        }
    }

    private void setSpeechPlayer(String wordContent) {
        String recordName = mExerciseManager.getRecordName();
        mSpeechPlayer.setWord(wordContent, recordName);
        String languageCode = mExerciseManager.getLanguageCode();
        Log.d(TAG, "setSpeechPlayer " + languageCode);
        mSpeechPlayer.setTextToSpeechLanguage(mExerciseManager.getLanguageCode());
    }

    private void toAnswer(int button) {
        mCanAnswer = false;
        String answer = (String) mAnswersButtons[button].getText();
        boolean isCorrect = mExerciseManager.checkAnswer(answer);
        if (isCorrect) {
            // mAnswersButtons[button].setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_green));
            GradientDrawable buttonShape = (GradientDrawable) mAnswersButtons[button].getBackground().getCurrent();
            buttonShape.setColor(Colors.getColor(R.color.correctColor, getActivity().getBaseContext()));
        } else {
            GradientDrawable buttonShape = (GradientDrawable) mAnswersButtons[button].getBackground().getCurrent();
            buttonShape.setColor(Colors.getColor(R.color.incorrectColor, getActivity().getBaseContext()));
            // mAnswersButtons[button].setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_red));
            String correctAnswer = mExerciseManager.getCorrectAnswer();
            Button correctButton = findButton(correctAnswer);
            //correctButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_green));
            GradientDrawable correctButtonShape = (GradientDrawable) correctButton.getBackground().getCurrent();
            correctButtonShape.setColor(Colors.getColor(R.color.correctColor, getActivity().getBaseContext()));
            //correctButton.setBackgroundColor(getResources().getColor(R.color.correctColor));
        }

        Animation animation = AnimationUtils.makeInAnimation(getActivity(), false);
        mNextButton.setVisibility(View.VISIBLE);
        mNextButton.startAnimation(animation);

    }

    private Button findButton(String text) {
        for (int i = 0; i < mNumAnswersButtons; i++) {
            if (mAnswersButtons[i].getText().equals(text)) {
                return mAnswersButtons[i];
            }
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_choose_exercise, container, false);
        mFragmentView = view;
        setupControls(view);
        setListeners();
        return view;
    }

    private void setupControls(View view) {
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mSpeechButton = (SpeechButton) view.findViewById(R.id.speechButton);
        mAnswerButton1 = (Button) view.findViewById(R.id.answer1);
        mAnswerButton2 = (Button) view.findViewById(R.id.answer2);
        mAnswerButton3 = (Button) view.findViewById(R.id.answer3);
        mAnswerButton4 = (Button) view.findViewById(R.id.answer4);
        mAnswerButton5 = (Button) view.findViewById(R.id.answer5);
        mAnswerButton6 = (Button) view.findViewById(R.id.answer6);
        mNextButton = (Button) view.findViewById(R.id.nextButton);
    }

    private void setListeners() {
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TextToSpeech textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), "en-US");
                textToSpeech.notifyNewMessage(mExerciseManager.getQuestion());*/
                try {
                    mSpeechPlayer.speech();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mNextButton.setOnClickListener(new NextButtonOnClickListener());
    }

    /**
     * W metodzie onViewCreated ustawiamy pytanie, oraz jeśli są wymagane dostępne podpowiedzi.
     * Nie można tego zrobić w metodzie onCreate, ponieważ musimy mieć dostęp do obiektu klasy
     * View, który nie jest dostępny podczas wykonywania metody onCreate. Obiekt ten zostaje
     * tworzony w metodzie onCreateView klasy Fragment. W tej metodzie tworzone są takze wszystki
     * komponenty jakie mają znaleźć się we fragmencie. Następną metodą po metodzie onCreateView
     * jest metoda onViewCreated, która jest wywoływana po utworzeniu obiektu View. Dopiero teraz
     * można przypisać tekst komponentowi TextView dostępnym we fragmencie.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mNumAnswersButtons = mExerciseManager.getNumAnswers();

        createAnswersButtonArray();
        hideNonUseAnswersButtons();
        addAnswersButtonsListeners();
        showExercise();

    }

    private void showAnswers() //TODO można zmienić nazwę
    {
        String[] answers = mExerciseManager.getAnswers(mNumAnswersButtons);
        for (int i = 0; i < mNumAnswersButtons; i++) {
            mAnswersButtons[i].setText(answers[i]);
        }
    }

    private void resetButtons() {
        GradientDrawable buttonShape;
        for (int i = 0; i < mNumAnswersButtons; i++) {
            //mAnswersButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
            buttonShape = (GradientDrawable) mAnswersButtons[i].getBackground().getCurrent();
            buttonShape.setColor(getResources().getColor(R.color.colorMain));

        }
    }

    private void showExercise() //TODO kolejna nazwa do zmienienia :(
    {
        showQuestion();
        showAnswers();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        resetButtons();
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


    protected class AnswerOnClickListener implements View.OnClickListener {
        int mIndex;

        public AnswerOnClickListener(int index) {
            mIndex = index;
        }

        @Override
        public void onClick(View v) {
            if (mCanAnswer) {
                toAnswer(mIndex);
            }
        }
    }

    protected class NextButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //TODO to może będzie można usunąć ponieważ nie widać rezultatu
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(100);

            mFragmentView.startAnimation(animation);
            if (mExerciseManager.getRemainingQuestion() != 0) {


                mExerciseManager.nextQuestion();

                resetButtons();
                mNextButton.setVisibility(View.INVISIBLE);
                mCanAnswer = true;
                showExercise();
            }
            ((ExerciseActivity) getActivity()).updateQuestion();
        }
    }
}
