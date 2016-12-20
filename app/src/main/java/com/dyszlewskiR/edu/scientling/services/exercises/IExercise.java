package com.dyszlewskiR.edu.scientling.services.exercises;

/**
 * Created by Razjelll on 16.11.2016.
 */

public interface IExercise {

    boolean checkAnswer(String answer, String correctAnswer);

    String getComment(String answer, String question);
}
