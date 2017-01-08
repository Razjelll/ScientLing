package com.dyszlewskiR.edu.scientling.services.exercises;

import android.os.AsyncTask;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;
import com.dyszlewskiR.edu.scientling.data.models.params.QuestionsParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Exercise;
import com.dyszlewskiR.edu.scientling.services.DataManager;

/**
 * Created by Razjelll on 16.11.2016.
 */

public class CreateExerciseTask extends AsyncTask<ExerciseParams, Void, ExerciseManager> {

    private ExerciseActivity mActivity;
    private ExerciseParams mParams;

    public CreateExerciseTask(ExerciseActivity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ExerciseManager doInBackground(ExerciseParams... params) {
        mParams = params[0];
        DataManager dataManager = ((LingApplication)mActivity.getApplication()).getDataManager();
        return new ExerciseManager(mParams, dataManager);
    }

    @Override
    protected void onPostExecute(ExerciseManager result) {
        mActivity.onEndCreatingExercise(result, mParams);
    }
}
