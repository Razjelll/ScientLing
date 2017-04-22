package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;

import java.util.List;

/**
 * Created by Razjelll on 16.11.2016.
 */

public interface IExerciseDirection {

    String getQuestion(Word word);

    String getAnswer(Word word);

    String getTranscription(Word word); //TODO prawdopodobnie będzie to można obsłużyć we fragmencie

    String[] getAnswers(List<Word> words);

    String getCode(VocabularySet set);
}
