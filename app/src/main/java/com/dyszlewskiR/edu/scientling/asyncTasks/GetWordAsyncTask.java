package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.os.AsyncTask;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.fragment.WordAdvancedEditFragment;
import com.dyszlewskiR.edu.scientling.fragment.WordEditFragment;
import com.dyszlewskiR.edu.scientling.services.DataManager;

/**
 * Created by Razjelll on 20.01.2017.
 */

public class GetWordAsyncTask extends AsyncTask<Long, Void, Word>
{
    private DataManager mDataManager;
    private WordEditFragment mBasicFragment;
    private WordAdvancedEditFragment mAdvancedFragment;
    public GetWordAsyncTask(DataManager dataManager, WordEditFragment editFragment, WordAdvancedEditFragment advancedFragment){
        mDataManager = dataManager;
        mBasicFragment = editFragment;
        mAdvancedFragment = advancedFragment;
    }
    @Override
    protected Word doInBackground(Long... params) {
        Word word = mDataManager.getWord(params[0]);
        return word;
    }

    @Override
    protected void onPostExecute(Word result){
        //mBasicFragment.setWord(result);
        mAdvancedFragment.setWord(result);
    }
}
