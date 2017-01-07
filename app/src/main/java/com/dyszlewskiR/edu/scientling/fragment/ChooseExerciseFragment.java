package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.dyszlewskiR.edu.scientling.services.exercises.ChooseExercise;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseLanguage;
import com.dyszlewskiR.edu.scientling.services.speech.TextToSpeech;
import com.dyszlewskiR.edu.scientling.utils.resources.Colors;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseExerciseFragment extends Fragment {

    private final static String TAG = "ChooseExerciseFragment";
    private static ExerciseManager mExerciseManager;
    FragmentTransaction fragmentTransaction;
    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private Button mSpeechButton;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private Button mAnswerButton5;
    private Button mAnswerButton6;
    private Button mNextButton;
    private Button moreButton;
    private int mNumAnswersButtons = 6; //TODO wartość ustalona z góry, zmienić, pobierać z preferencji
    private Button[] mAnswersButtons;
    private boolean mCanAnswer;
    private OnFragmentInteractionListener mListener;
    private View mFragmentView;

    public ChooseExerciseFragment() {

    }

    public static ChooseExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseLanguage language) {
        ChooseExerciseFragment fragment = new ChooseExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new ChooseExercise());
        mExerciseManager.setExerciseLanguage(language);
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mCanAnswer = true;
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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
            String transcription = mExerciseManager.getTranscription();
            mTranscriptionTextView.setText(transcription);

            Animation animation = AnimationUtils.makeInAnimation(getActivity(), false);
            mFragmentView.startAnimation(animation);
            //getActivity().overridePendingTransition(R.anim.move_from_right_side, R.anim.move_to_left_side);
        }
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
        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mTranscriptionTextView = (TextView) view.findViewById(R.id.transcriptionTextView);
        mSpeechButton = (Button) view.findViewById(R.id.speechButton);

        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextToSpeech textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), "en-US");
                textToSpeech.notifyNewMessage(mExerciseManager.getQuestion());
            }
        });

        mAnswerButton1 = (Button) view.findViewById(R.id.answer1);
        mAnswerButton2 = (Button) view.findViewById(R.id.answer2);
        mAnswerButton3 = (Button) view.findViewById(R.id.answer3);
        mAnswerButton4 = (Button) view.findViewById(R.id.answer4);
        mAnswerButton5 = (Button) view.findViewById(R.id.answer5);
        mAnswerButton6 = (Button) view.findViewById(R.id.answer6);
        mNextButton = (Button) view.findViewById(R.id.nextButton);
        mNextButton.setOnClickListener(new NextButtonOnClickListener());

        return view;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        resetButtons();
        super.onDetach();
        mListener = null;
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
