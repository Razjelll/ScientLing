package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.data.database.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class L2toL1 implements IExerciseLanguage {

    @Override
    public String getQuestion(Word word) {
        return word.getContent();
    }

    @Override
    public String getAnswer(Word word)
    {
        //TODO pobiera tylko jedną odpowiedź, może będzie to trzeba zmienić
        return word.getTranslations().get(0).getContent();
    }

    @Override
    public String getTranscription(Word word) {
        return word.getTranscription();
    }

    @Override
    public Answer[] getAnswers(ExerciseParameters parameters, String[] differentFrom, DataManager dataManager) {
        long set = parameters.getSet();

        long difficult = parameters.getDifficult();
        long category = parameters.getCategory();
        int howMuch = parameters.getNumQuestions();
        List<Translation> translations = dataManager.getAnswersL1(set,difficult, category, howMuch,differentFrom);

        Answer[] answers = new Answer[translations.size()]; //TODO sprawdzić czy to jest potrzebne
        Answer answer = null;
        for(int i= 0 ;i< translations.size();i++)
        {
            answer = new Answer(translations.get(i).getId(), translations.get(i).getContent());
            answers[i] = answer;
        }
        return answers;
    }
}
