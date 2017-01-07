package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseLanguage;
import com.dyszlewskiR.edu.scientling.services.exercises.KnowExercise;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KnowExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KnowExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private OnFragmentInteractionListener mListener;

    public KnowExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KnowExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KnowExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseLanguage language) {
        KnowExerciseFragment fragment = new KnowExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new KnowExercise());
        mExerciseManager.setExerciseLanguage(language);
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
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
