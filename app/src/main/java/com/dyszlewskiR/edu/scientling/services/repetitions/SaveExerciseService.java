package com.dyszlewskiR.edu.scientling.services.repetitions;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Repetition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.utils.DateCalculator;
import com.dyszlewskiR.edu.scientling.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Razjelll on 09.01.2017.
 */

public class SaveExerciseService extends IntentService {
    private Handler handler = new Handler();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SaveExerciseService(String name) {
        super(name);
    }

    public SaveExerciseService(){
        super("SaveExerciseService");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(),"Usługa rozpoczęta");
        final List<Word> wordsList = intent.getParcelableArrayListExtra("list");
        final List<Word> incorrectWord = intent.getParcelableArrayListExtra("incrrect");
        final DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
        final List<Repetition> repetitionsList = new ArrayList<>();
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(Word word : wordsList){
                    word.setMasterLevel((byte)getMasterLevel(word, incorrectWord));
                    word.setLearningDate(getLearningDate());
                    repetitionsList.add(getRepetition(word));
                }
                dataManager.saveRepetitionAndUpdateWords(repetitionsList,wordsList);
                Toast.makeText(getBaseContext(), "Zakończono tentegować", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getMasterLevel(Word word, List<Word> incorrectWords){
        int masterLevel = word.getMasterLevel();
        if(incorrectWords != null){
            for(Word incorrect: incorrectWords){
                if(word.getId() == incorrect.getId()){
                    return masterLevel;
                }
            }
        }
        return Interval.getNextMasterLevel(masterLevel);
    }

    private int getLearningDate(){
        return DateCalculator.dateToInt(DateUtils.getTodayDate());
    }

    private Repetition getRepetition(Word word){
        Repetition repetition = prepareRepetition(word);
        switch (Interval.getLearningState(word.getMasterLevel())){
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

    private Repetition prepareRepetition(Word word){
        Repetition repetition = new Repetition();
        repetition.setWordId(word.getId());
        int interval = Interval.getInterval(word.getMasterLevel());
        Date repetitionDate = DateUtils.addDays(DateUtils.getTodayDate(),interval);
        int date = DateCalculator.dateToInt(repetitionDate);
        repetition.setDate(date);
        return repetition;
    }

}
