package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.os.AsyncTask;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.adapters.LearningWordsAdapter;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.services.DataManager;

import java.util.List;

/**
 * Klasa odpowiedzialana za pobranie słówek. Klasa wybiera słówka, które nie zostały jeszcze rozpoczęte,
 * kiedując się losowo lub podanymi parametrami.
 */

public class LoadLearningAsyncTask extends AsyncTask<Void, Void, List<Word>> {

    private DataManager mDatamanager;


    public LoadLearningAsyncTask(DataManager dataManager)
    {
        mDatamanager = dataManager;
    }

    @Override
    protected List<Word> doInBackground(Void... params) {
        List<Word> words = mDatamanager.getAllWords(); //TODO dodać parametry  i zobaczyć czy pobiera odpowiednie słówka
        return words;
    }
}
