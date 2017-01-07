package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;

import java.util.List;

/**
 * Created by Razjelll on 16.11.2016.
 */

public interface IExerciseLanguage {

    String getQuestion(Word word);

    String getAnswer(Word word);

    String getTranscription(Word word); //TODO prawdopodobnie będzie to można obsłużyć we fragmencie

   String[] getAnswers(List<Word> words);
}
