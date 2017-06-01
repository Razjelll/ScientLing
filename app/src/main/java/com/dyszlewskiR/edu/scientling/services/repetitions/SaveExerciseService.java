package com.dyszlewskiR.edu.scientling.services.repetitions;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.Repetition;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.utils.DateCalculator;
import com.dyszlewskiR.edu.scientling.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveExerciseService extends IntentService {
    private Handler handler = new Handler();

    public SaveExerciseService(String name) {
        super(name);
    }

    public SaveExerciseService() {
        super("SaveExerciseService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final List<Word> wordsList = intent.getParcelableArrayListExtra("list");
        final boolean[] answersCorrectness = intent.getBooleanArrayExtra("correctness");
        final DataManager dataManager = LingApplication.getInstance().getDataManager();
        final List<Repetition> repetitionsList = new ArrayList<>();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Word word;
                for(int i =0; i< wordsList.size(); i++){
                    word = wordsList.get(i);
                    if(answersCorrectness == null || answersCorrectness[i]){
                        byte masterLevel = word.getMasterLevel();
                        byte nextMasterLevel = Interval.getNextMasterLevel(masterLevel);
                        word.setMasterLevel(nextMasterLevel);
                    }
                    word.setLearningDate(getLearningDate());
                    repetitionsList.add(getRepetition(word));
                }
                dataManager.saveRepetitionAndUpdateWords(repetitionsList, wordsList);
            }
        });
    }



    private int getLearningDate() {
        return DateCalculator.dateToInt(DateUtils.getTodayDate());
    }

    private Repetition getRepetition(Word word) {
        Repetition repetition = prepareRepetition(word);
        switch (Interval.getLearningState(word.getMasterLevel())) {
            case START:
                repetition.setActions(Repetition.Action.SAVE);
                break;
            case MIDDLE:
                repetition.setActions(Repetition.Action.UPDATE);
                break;
            case FINISH:
                repetition.setActions(Repetition.Action.DELETE);
                break;
        }
        return repetition;
    }

    private Repetition prepareRepetition(Word word) {
        Repetition repetition = new Repetition();
        repetition.setWordId(word.getId());
        int interval = Interval.getInterval(word.getMasterLevel());
        Date repetitionDate = DateUtils.addDays(DateUtils.getTodayDate(), interval);
        int date = DateCalculator.dateToInt(repetitionDate);
        repetition.setDate(date);
        return repetition;
    }

}
