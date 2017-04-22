package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;
import com.dyszlewskiR.edu.scientling.services.exercises.WriteExercise;
import com.dyszlewskiR.edu.scientling.services.speech.ISpeechCallback;
import com.dyszlewskiR.edu.scientling.services.speech.ISpeechRecognitionResult;
import com.dyszlewskiR.edu.scientling.services.speech.SpeechPlayer;
import com.dyszlewskiR.edu.scientling.services.speech.SpeechToText;
import com.dyszlewskiR.edu.scientling.utils.StringSimilarityCalculator;
import com.dyszlewskiR.edu.scientling.widgets.SpeechButton;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WriteExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WriteExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteExerciseFragment extends Fragment implements ISpeechRecognitionResult, ISpeechCallback {


    private static ExerciseManager mExerciseManager; //TODO dlaczego to jest stałe
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private SpeechButton mSpeechButton;
    private Button mSayButton;
    private EditText mAnswerEditText;
    private Button mCheckAnswerButton;

    private SpeechPlayer mSpeechPlayer;

    public WriteExerciseFragment() {
    }

    public static WriteExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseDirection language) {
        WriteExerciseFragment fragment = new WriteExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new WriteExercise());
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

    private void showQuestion() {
        mAnswerEditText.setText("");

        String question = mExerciseManager.getQuestion();
        String recordFile = mExerciseManager.getRecordName();
        mSpeechPlayer.setWord(question, recordFile);
        mWordTextView.setText(question);
        mTranscriptionTextView.setText(mExerciseManager.getTranscription());
    }

    private void toAnswer(String answer) {
        boolean correct = mExerciseManager.checkAnswer(answer);
        if (correct) {
            new CorrectDialog(getActivity()).show();
        } else {
            new IncorrectDialog(getActivity(), answer, mExerciseManager.getCorrectAnswer()).show();
        }
    }

    public void nextQuestion() {
        mExerciseManager.nextQuestion();
        showQuestion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_exercise, container, false);
        setupControls(view);
        setListeners();


        return view;
    }

    private void setupControls(View view) {
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mSpeechButton = (SpeechButton) view.findViewById(R.id.speechButton);
        mSayButton = (Button) view.findViewById(R.id.say_button);
        mAnswerEditText = (EditText) view.findViewById(R.id.wordEditText);
        mAnswerEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mCheckAnswerButton = (Button) view.findViewById(R.id.checkAnswer);
    }

    private void setListeners() {
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

        mSayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechToText speechToText = new SpeechToText(getActivity().getBaseContext(), "en-US", WriteExerciseFragment.this);
                speechToText.startListening();
            }
        });

        mAnswerEditText.setOnEditorActionListener(new OnEditorEnterListener());
        mCheckAnswerButton.setOnClickListener(new AcceptAnswerOnClickListener());
        mAnswerEditText.requestFocus();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showQuestion();
        showKeyboard();
    }

    private void showKeyboard() {
        //ustawienie klawiatury zawsze widocznej, klawiatura wyświetlana jest po każdym pytaniu
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //ustawienie klawiatury widocznej przy starcie fragmentu
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mAnswerEditText, 0);
    }

    @Override
    public void onDetach() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSpeechPlayer.release();
        super.onDetach();
    }

    @Override
    public void receiveRecognitionResult(String[] result) {
        if (result != null) {
            String answer = mExerciseManager.getCorrectAnswer();
            String mostSimilar = StringSimilarityCalculator.getMostSimilarLevenshtein(answer, result);
            mAnswerEditText.setText(mostSimilar);
        }
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

    protected class AcceptAnswerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String answer = String.valueOf(mAnswerEditText.getText());
            toAnswer(answer);
        }
    }

    /**
     * Listener, który nasłuchuje specjalnych akcji pola edycyjnego. W tym rpzypadku
     * jest używany do zaakceptowania wartości wpisanej w pole tekstowe. Aby to rozwiązanie
     * zadziałało we właściwościach tego pola tekstowego ustawiono wartość
     * android:imeOptions = DONE. Dzięki takemu ustawieniu przycisk na wirtualnej kalwiaturze
     * urządzenia z Androidem odpowiada za wywołanie etod toAnswer. Ustawienie to zmienia
     * ikonę tego przycisku na ikone akceptacji. Ustawiając inne wartości dla parametru
     * imeOptions można uzyskać przejścia do kolejnych pól tekstowych, przejściu do podanej
     * lokalizacji, wysłać coś.
     */
    protected class OnEditorEnterListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                toAnswer(String.valueOf(mAnswerEditText.getText()));
                return true;
            }
            return false;
        }
    }

    public class CorrectDialog extends Dialog {

        private Button mNextButton;

        public CorrectDialog(Context context) {
            super(context);
            setupDialog();
            setupControls();
            setListeners();
            setDialogSize();
        }

        private void setupDialog() {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setContentView(R.layout.correct_dialog);
            this.setCancelable(false);
        }

        private void setDialogSize() {
            new DialogSizeHelper().setDialogWidthMatchParent(this);
        }

        private void setupControls() {
            mNextButton = (Button) this.findViewById(R.id.correctDialogNextButton);
        }

        private void setListeners() {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion();
                    ((ExerciseActivity) getActivity()).updateQuestion();
                    dismiss();
                }
            });
        }
    }

    public class IncorrectDialog extends Dialog {

        private TextView mUserAnswer;
        private TextView mCorrectAnswer;
        private Button mNextButton;

        public IncorrectDialog(Context context, String userAnswer, String correctAnswer) {
            super(context);
            setupDialog();
            setupControls();
            setControlsValue(userAnswer, correctAnswer);
            setListeners();
            setDialogSize();
        }

        private void setupDialog() {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setContentView(R.layout.incorrect_dialog);
            this.setCancelable(false);
        }

        private void setDialogSize() {
            new DialogSizeHelper().setDialogWidthMatchParent(this);
        }

        private void setupControls() {
            mUserAnswer = (TextView) this.findViewById(R.id.yourAnswerTextView);
            mCorrectAnswer = (TextView) this.findViewById(R.id.correctAnswerTextView);
            mNextButton = (Button) this.findViewById(R.id.nextButton);
        }

        private void setControlsValue(String userAnswer, String correctAnswer) {
            mUserAnswer.setText(userAnswer);
            mCorrectAnswer.setText(correctAnswer);
        }

        private void setListeners() {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion();
                    dismiss();
                }
            });
        }
    }

    private class DialogSizeHelper {
        public void setDialogWidthMatchParent(Dialog dialog) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }
}
