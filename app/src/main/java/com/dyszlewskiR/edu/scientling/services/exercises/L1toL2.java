package com.dyszlewskiR.edu.scientling.services.exercises;

import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class L1toL2 implements IExerciseLanguage {

    @Override
    public String getQuestion(Word word) {
        //TODO prawdopodobnie można to trochę usprawnić
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < word.getTranslations().size(); i++) {
            builder.append(word.getTranslations().get(i).getContent());
            if (i != word.getTranslations().size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
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
    public Answer[] getAnswers(ExerciseParameters parameters, String[] differentFrom, DataManager dataManager) {
        long set = parameters.getSet();

        long difficult = parameters.getDifficult();
        long category = parameters.getCategory();
        int howMuch = parameters.getNumQuestions();
        ArrayList<Word> words = (ArrayList<Word>) dataManager.getAnswersL2(set, difficult, category, howMuch, differentFrom);

        Answer[] answers = new Answer[words.size()]; //TODO sprawdzić czy to jest potrzebne
        Answer answer = null;
        for (int i = 0; i < words.size(); i++) {
            answer = new Answer(words.get(i).getId(), words.get(i).getContent());
            answers[i] = answer;
        }
        return answers;
    }
}
