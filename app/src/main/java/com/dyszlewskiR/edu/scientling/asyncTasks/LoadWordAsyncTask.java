package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.os.AsyncTask;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.WordListAdapter;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.data.models.params.WordsListParams;
import com.dyszlewskiR.edu.scientling.fragment.ManageWordsFragment;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Razjelll on 11.01.2017.
 */

public class LoadWordAsyncTask extends AsyncTask<WordsListParams, Void, WordListAdapter> {

    private DataManager mDataManager;
    private ManageWordsFragment mFragment;

    public LoadWordAsyncTask(DataManager dataManager, ManageWordsFragment fragment) {
        mDataManager = dataManager;
        mFragment = fragment;
    }

    @Override
    protected WordListAdapter doInBackground(WordsListParams... params) {
        List<Word> words = null;
        try {
            words = mDataManager.getWords(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WordListAdapter adapter = new WordListAdapter(mFragment.getActivity(), R.layout.item_word_list,
                words, mDataManager);
        return adapter;
    }

    @Override
    protected void onPostExecute(WordListAdapter result) {
        mFragment.onPostAsyncTask(result);
    }

}
