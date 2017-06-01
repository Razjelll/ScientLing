package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import java.util.List;

public interface IExerciseDirection {

    String getQuestion(Word word);

    String getAnswer(Word word);

    String[] getAnswers(List<Word> words);

}
