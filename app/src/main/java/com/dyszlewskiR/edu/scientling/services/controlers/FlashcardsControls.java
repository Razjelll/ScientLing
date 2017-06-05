package com.dyszlewskiR.edu.scientling.services.controlers;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.asyncTasks.LoadFlashcardsAsyncTask;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlashcardsControls {

    private List<Word> mWords;
    private DataManager mDataManager;

    public FlashcardsControls(){
        mDataManager = LingApplication.getInstance().getDataManager();
    }

    public List<Word> getWords(FlashcardParams params){
        LoadFlashcardsAsyncTask task = new LoadFlashcardsAsyncTask(mDataManager);
        try {
            mWords = task.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return mWords;
    }



}
