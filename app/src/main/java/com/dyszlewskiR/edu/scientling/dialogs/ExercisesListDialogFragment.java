package com.dyszlewskiR.edu.scientling.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.adapters.ExercisesTypesAdapter;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Exercise;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Razjelll on 06.01.2017.
 */

public class ExercisesListDialogFragment  extends DialogFragment implements DialogInterface.OnClickListener{

   // private ExercisesTypesAdapter mAdapter;
    private ArrayAdapter<String> mAdapter;
    private List<String> mExerciseNames;
    private int mSelectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mExerciseNames = Arrays.asList(getResources().getStringArray(R.array.exercises_types));
        Bundle arguments = getArguments();
        mSelectedItem = arguments.getInt("selected");
        /*mAdapter = new ExercisesTypesAdapter(getActivity().getBaseContext(), R.layout.item_exercises_types_dialog,
                mExerciseNames, mSelectedItem);*/
        mAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.item_exercises_types_dialog, mExerciseNames);
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        //builder.setAdapter(mAdapter, this);
        builder.setSingleChoiceItems(R.array.exercises_types, mSelectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExerciseActivity activity = (ExerciseActivity)getActivity();
                activity.setExerciseFragment(which+1);
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(getClass().getSimpleName(), "Wybrano " + which);
    }
}
