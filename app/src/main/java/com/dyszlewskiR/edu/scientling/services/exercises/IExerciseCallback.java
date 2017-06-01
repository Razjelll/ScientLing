package com.dyszlewskiR.edu.scientling.services.exercises;

public interface IExerciseCallback {
    void onAnswer(boolean correct);
    void onExerciseFinish();

}
