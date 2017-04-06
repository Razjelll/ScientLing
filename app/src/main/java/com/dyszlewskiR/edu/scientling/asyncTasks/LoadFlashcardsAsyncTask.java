package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.os.AsyncTask;

import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.util.List;

/**
 * Created by Razjelll on 05.01.2017.
 */

public class LoadFlashcardsAsyncTask extends AsyncTask<FlashcardParams, Void, List<Word>> {
    private DataManager mDataManager;

    public LoadFlashcardsAsyncTask(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    protected List<Word> doInBackground(FlashcardParams... params) {
        return mDataManager.getWords(params[0]);
    }
}
