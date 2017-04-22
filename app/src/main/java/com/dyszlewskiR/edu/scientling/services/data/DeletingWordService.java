package com.dyszlewskiR.edu.scientling.services.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.file.WordFileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;

/**
 * Created by Razjelll on 07.04.2017.
 */

public class DeletingWordService extends Service {
    private final String LOG_TAG = "DeletingWordService";
    private DataManager mDataManager;

    @Override
    public void onCreate() {
        mDataManager = ((LingApplication) getApplication()).getDataManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        Word word = intent.getParcelableExtra("word");
        String setCatalog = intent.getStringExtra("catalog");
        DeletingRunnable runnable = new DeletingRunnable(word, setCatalog, mDataManager);
        runnable.run();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DeletingRunnable implements Runnable {
        private Word mWord;
        private DataManager mDataManager;
        private String mSetCatalog;

        public DeletingRunnable(Word word, String catalog, DataManager dataManager) {
            mWord = word;
            mDataManager = dataManager;
            mSetCatalog = catalog;
        }

        @Override
        public void run() {
            if (WordFileSystem.checkFileExist(mWord.getImageName(), mSetCatalog, getBaseContext())) {
                WordFileSystem.deleteImage(mWord.getImageName(), mSetCatalog, getBaseContext());
            }
            if (WordFileSystem.checkFileExist(mWord.getRecordName(), mSetCatalog, getBaseContext())) {
                WordFileSystem.deleteRecord(mWord.getRecordName(), mSetCatalog, getBaseContext());
            }
            mDataManager.deleteWord(mWord);

        }
    }
}
