package com.dyszlewskiR.edu.scientling;

import android.app.Application;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.services.DataManager;

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
        mCurrentSetId = 1; //TODO zmienić na pobieranie z opcji
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();

    }
}
