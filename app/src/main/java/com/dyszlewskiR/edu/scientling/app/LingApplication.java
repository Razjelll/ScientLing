package com.dyszlewskiR.edu.scientling.app;

import android.app.Application;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.preferences.PreferenceInitializer;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.utils.Constants;

/**
 * Created by Razjelll on 01.12.2016.
 */

public class LingApplication extends Application {

    private final String TAG = "LingApplication";

    private DataManager mDataManager;
    private long mCurrentSetId;
    private long mCurrentLessonId;

    public DataManager getDataManager() {
        return mDataManager;
    }

    public long getCurrentSetId() {
        return mCurrentSetId;
    }

    public long getCurrentLessonId() {
        return mCurrentLessonId;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mDataManager = new DataManager(getBaseContext());
        mCurrentSetId = Settings.getCurrentSetId(getBaseContext());
        if (!PreferenceInitializer.isInitialize(getBaseContext())) {
            PreferenceInitializer.initialize(getBaseContext());
            Settings.setCurrentSetId(Constants.DEFAULT_SET_ID, getBaseContext());
        }

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        mDataManager.release();
        super.onTerminate();
    }
}
