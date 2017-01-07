package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.utils.TranslationListConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class L1toL2 implements IExerciseLanguage {

    @Override
    public String getQuestion(Word word) {
        assert word != null;
        assert word.getTranslations() != null;
        return TranslationListConverter.toString(word.getTranslations());
    }

    @Override
    public String getAnswer(Word word) {
        return word.getContent();
    }

    @Override
    public String getTranscription(Word word) {
        return "";
    }

    @Override
    public String[] getAnswers(List<Word> words) {
        assert words != null && words.size() != 0;
        int length = words.size();
        String[] answers = new String[length];
        for(int i = 0; i<length; i++) {
            answers[i] = words.get(i).getContent();
        }
        return answers;

    }
}
