package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseLanguage;
import com.dyszlewskiR.edu.scientling.services.exercises.WriteExercise;
import com.dyszlewskiR.edu.scientling.services.speech.ISpeechRecognitionResult;
import com.dyszlewskiR.edu.scientling.services.speech.SpeechToText;
import com.dyszlewskiR.edu.scientling.utils.StringSimilarityCalculator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WriteExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WriteExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteExerciseFragment extends Fragment implements ISpeechRecognitionResult {


    private static ExerciseManager mExerciseManager; //TODO dlaczego to jest stałe
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private Button mSpeechButton;
    private EditText mAnswerEditText;
    private Button mCheckAnswerButton;
    private OnFragmentInteractionListener mListener;

    public WriteExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WriteExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WriteExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseLanguage language) {
        WriteExerciseFragment fragment = new WriteExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new WriteExercise());
        mExerciseManager.setExerciseLanguage(language);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void showQuestion() {
        mAnswerEditText.setText("");

        mWordTextView.setText(mExerciseManager.getQuestion());
        mTranscriptionTextView.setText(mExerciseManager.getTranscription());

    }

    private void toAnswer(String answer) {
        boolean correct = mExerciseManager.checkAnswer(answer);
        if (correct) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.correct_dialog);


            TextView text = (TextView) dialog.findViewById(R.id.correctDialogText);
            Button nextButton = (Button) dialog.findViewById(R.id.correctDialogNextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion();
                    ((ExerciseActivity)getActivity()).updateQuestion();
                    dialog.dismiss();
                }
            });
            dialog.show();


            setDialogWidth(dialog);

        } else {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.incorrect_dialog);


            TextView yourAnswer = (TextView) dialog.findViewById(R.id.yourAnswerTextView);
            yourAnswer.setText(answer);
            TextView correctsAnswers = (TextView) dialog.findViewById(R.id.correctAnswerTextView);
            correctsAnswers.setText(mExerciseManager.getCorrectAnswer());
            Button nextButton = (Button) dialog.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion();
                    dialog.dismiss();
                }
            });
            dialog.show();

            setDialogWidth(dialog);
        }
    }

    private void setDialogWidth(Dialog dialog) {
        //ustawianie szerokości powiadomienia. Nie można tego zrobić w xml, ponieważ
        // miejscu okno dialogowe nie zna szerokości rodzica
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void nextQuestion() {
        mExerciseManager.nextQuestion();
        showQuestion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_exercise, container, false);
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mSpeechButton = (Button) view.findViewById(R.id.speechButton);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechToText speechToText = new SpeechToText(getActivity().getBaseContext(), "en-US", WriteExerciseFragment.this);
                speechToText.startListening();
            }
        });
        mAnswerEditText = (EditText) view.findViewById(R.id.wordEditText);
        mAnswerEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mAnswerEditText.setOnEditorActionListener(new OnEditorEnterListener());
        mCheckAnswerButton = (Button) view.findViewById(R.id.checkAnswer);
        mCheckAnswerButton.setOnClickListener(new AcceptAnswerOnClickListener());

        mAnswerEditText.requestFocus();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showQuestion();

        //ustawienie klawiatury zawsze widocznej, klawiatura wyświetlana jest po każdym pytaniu
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //ustawienie klawiatury widocznej przy starcie fragmentu
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mAnswerEditText, 0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onDetach();
        mListener = null;
    }

    @Override
    public void receiveRecognitionResult(String[] result) {
        if (result != null) {
            String answer = mExerciseManager.getCorrectAnswer();
            String mostSimilar = StringSimilarityCalculator.getMostSimilarLevenshtein(answer, result);
            mAnswerEditText.setText(mostSimilar);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
}
