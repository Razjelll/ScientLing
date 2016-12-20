package com.dyszlewskiR.edu.scientling.services.exercises;

import android.os.AsyncTask;

import com.dyszlewskiR.edu.scientling.activity.ExerciseActivity;

/**
 * Created by Razjelll on 16.11.2016.
 */

public class CreateExerciseTask extends AsyncTask<ExerciseParameters, Void, ExerciseManager> {

    private ExerciseActivity mActivity;

    public CreateExerciseTask(ExerciseActivity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {

    }


    @Override
    protected ExerciseManager doInBackground(ExerciseParameters... params) {
       /* try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return new ExerciseManager(params[0], mActivity.getApplicationContext());

    }


    @Override
    protected void onPostExecute(ExerciseManager result) {
        mActivity.prepareExerciseProgress(result.getNumQuestions());
        mActivity.setExerciseManager(result);
        mActivity.hideCircleProgressBar();
        mActivity.showCurrentFragment();

    }


}
