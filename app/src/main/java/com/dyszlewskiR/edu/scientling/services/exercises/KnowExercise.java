package com.dyszlewskiR.edu.scientling.services.exercises;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class KnowExercise implements IExercise {

    @Override
    public boolean checkAnswer(String answer, String question) {

        if(answer.equals("Know")) //TODO przerobić boolean na int, ponieważ nie można uwzględnić pośrednich wyników
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String getComment(String answer, String question) {
        return null;
    }
}
