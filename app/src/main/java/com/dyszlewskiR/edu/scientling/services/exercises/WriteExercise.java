package com.dyszlewskiR.edu.scientling.services.exercises;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class WriteExercise implements IExercise {
    @Override
    public boolean checkAnswer(String answer, String correctAnswer) {
        return answer.equals(correctAnswer); //TODO To jest uproszczenie. Wprowadzić wykrywanie literówek
    }

    @Override
    public String getComment(String answer, String question) {
        return null;
    }
}
