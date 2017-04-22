package com.dyszlewskiR.edu.scientling.asyncTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.file.WordFileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.data.models.params.SaveWordParams;
import com.dyszlewskiR.edu.scientling.dialogs.SaveWordDialog;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.io.IOException;

/**
 * Created by Razjelll on 20.01.2017.
 */
//TODO zastanowić się czy zwracać tylko id czy całe słówko. A może nic nie zwracać
public class SaveWordAsyncTask extends AsyncTask<SaveWordParams, Void, Word> {

    private final String TAG = "SaveWordAsyncTask";

    private DataManager mDataManager;
    private Context mContext;
    private SaveWordDialog mDialog;
    private Callback mCallback;


    public interface Callback {
        void onSaveCompleted(Word word);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public SaveWordAsyncTask(DataManager dataManager, Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        mDialog = new SaveWordDialog(mContext);
        mDialog.show();
    }

    @Override
    protected Word doInBackground(SaveWordParams... params) {
        Log.d(TAG, "doInBackground");
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Word word = params[0].getWord();
        VocabularySet set = params[0].getSet();
        long wordId;
        if (params[0].isEdit()) {
            updateWord(word);
            wordId = word.getId();
        } else {
            wordId = saveWord(word);
        }
        if (params[0].getImageUri() != null) {
            saveImage(word.getImageName(), set.getCatalog(), params[0].getImageUri());
        }
        if (params[0].getRecordUri() != null) {
            saveRecord(word.getRecordName(), set.getCatalog(), params[0].getRecordUri());
        }
        word.setId(wordId);
        return word;
    }

    @Override
    protected void onPostExecute(Word result) {
        Log.d(TAG, "onPostExecute");
        if (mCallback != null) {
            mCallback.onSaveCompleted(result);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private long saveWord(Word word) {
        long wordId = mDataManager.saveWord(word);
        //zmiana informacji na dialogu

        return wordId;
    }

    private void updateWord(Word word) {
        mDataManager.updateWord(word);
    }

    private void saveImage(String fileName, String setCatalog, Uri uri) {
        try {
            WordFileSystem.saveImage(fileName, setCatalog, uri, mContext, true);
        } catch (IOException e) {
            e.printStackTrace(); // TODO pokazać na dialogu  błąd zapisywanie obrazka
        }
    }

    private void saveRecord(String fileName, String setCatalog, Uri uri) {
        try {
            WordFileSystem.saveRecord(fileName, setCatalog, uri, mContext);
        } catch (IOException e) {
            e.printStackTrace(); //TODO pokazać na dialogu błąd zapisywania nagrania
        }
    }
}
