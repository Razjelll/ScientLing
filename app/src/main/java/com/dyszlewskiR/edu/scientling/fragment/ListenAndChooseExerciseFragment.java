package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.services.exercises.ChooseExercise;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListenAndChooseExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListenAndChooseExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListenAndChooseExerciseFragment extends Fragment {

    private static ExerciseManager mExerciseManager;
    private Button speechButton;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button answer5Button;
    private Button answer6Button;
    private OnFragmentInteractionListener mListener;

    public ListenAndChooseExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListenAndChooseExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListenAndChooseExerciseFragment newInstance(ExerciseManager exerciseManager, IExerciseDirection language) {
        ListenAndChooseExerciseFragment fragment = new ListenAndChooseExerciseFragment();
        mExerciseManager = exerciseManager;
        mExerciseManager.setExerciseType(new ChooseExercise());
        mExerciseManager.setExerciseLanguage(language);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listen_and_choose_exercise, container, false);
        speechButton = (Button) view.findViewById(R.id.speechButton);
        answer1Button = (Button) view.findViewById(R.id.answer1);
        answer2Button = (Button) view.findViewById(R.id.answer2);
        answer3Button = (Button) view.findViewById(R.id.answer3);
        answer4Button = (Button) view.findViewById(R.id.answer4);
        answer5Button = (Button) view.findViewById(R.id.answer5);
        answer6Button = (Button) view.findViewById(R.id.answer6);

        return view;
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
}
