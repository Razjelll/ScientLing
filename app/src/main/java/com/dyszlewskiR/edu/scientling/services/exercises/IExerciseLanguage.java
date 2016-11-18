package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.data.database.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.Word;

/**
 * Created by Razjelll on 16.11.2016.
 */

public interface IExerciseLanguage {

    String getQuestion(Word word);
    String getAnswer(Word word);
    String getTranscription(Word word); //TODO prawdopodobnie będzie to można obsłużyć we fragmencie
    Answer[] getAnswers(ExerciseParameters parameters, String[] differentFrom, DataManager dataManager);
}
