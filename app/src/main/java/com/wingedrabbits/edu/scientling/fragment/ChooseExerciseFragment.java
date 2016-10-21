package com.wingedrabbits.edu.scientling.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wingedrabbits.edu.scientling.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseExerciseFragment extends Fragment {

    private TextView wordTextView;
    private TextView pronunciationTextView;
    private Button speechButton;

    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    private Button answerButton5;
    private Button answerButton6;

    private OnFragmentInteractionListener mListener;

    public ChooseExerciseFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static ChooseExerciseFragment newInstance(String param1, String param2) {
        ChooseExerciseFragment fragment = new ChooseExerciseFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_exercise, container, false);

        wordTextView = (TextView)view.findViewById(R.id.knowWord);
        pronunciationTextView = (TextView)view.findViewById(R.id.knowPronunciation);
        speechButton = (Button) view.findViewById(R.id.chooseSpeechButton);

        answerButton1 = (Button)view.findViewById(R.id.answer1);
        answerButton2 = (Button)view.findViewById(R.id.answer2);
        answerButton3 = (Button)view.findViewById(R.id.answer3);
        answerButton4 = (Button)view.findViewById(R.id.answer4);
        answerButton5 = (Button)view.findViewById(R.id.answer5);
        answerButton6 = (Button)view.findViewById(R.id.answer6);

        return view;
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
}
