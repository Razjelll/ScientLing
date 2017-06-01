package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.activity.MainActivity;
import com.dyszlewskiR.edu.scientling.data.models.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.util.List;

public class MainInitializationValuesTask extends AsyncTask<DataManager, Void, Void> {

    private MainActivity mActivity;
    private VocabularySet mSet;
    private List<Lesson> mLessons;

    public MainInitializationValuesTask(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        mActivity.onPreGetDataTask();
    }

    @Override
    protected Void doInBackground(DataManager... params) {
        DataManager dataManager = params[0];
        Log.d(getClass().getSimpleName(), "getDataManager");
        mSet = dataManager.getSetById(Settings.getCurrentSetId(mActivity.getBaseContext()));
        Log.d(getClass().getSimpleName(), "getSet");
        mLessons = dataManager.getLessonsWithProgress(mSet);
        Log.d(getClass().getSimpleName(), "getLesson");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mActivity.onPostGetDataTask(mSet, mLessons);
    }
}
