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
    private int mFragmentToStart;

    public CreateExerciseTask(ExerciseActivity activity, int fragmentToStart) {
        mActivity = activity;
        mFragmentToStart = fragmentToStart;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ExerciseManager doInBackground(ExerciseParams... params) {
        DataManager dataManager = ((LingApplication)mActivity.getApplication()).getDataManager();
        return new ExerciseManager(params[0], dataManager);
    }

    @Override
    protected void onPostExecute(ExerciseManager result) {
        mActivity.prepareExerciseProgress(result.getNumQuestions());

        mActivity.hideCircleProgressBar();

        mActivity.setExerciseManager(result);
        if(result.getNumQuestions()==0){
            mActivity.finish();
        }
        mActivity.setNumQuestions(result.getNumQuestions());
        mActivity.changeFragment(mFragmentToStart);
    }
}
