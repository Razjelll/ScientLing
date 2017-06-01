package com.dyszlewskiR.edu.scientling.fragment;

import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;

public interface IExerciseFragment {
    void toAnswer(String answer);
    void showQuestion();
    void setExerciseManager(ExerciseManager exerciseManager);
    void setExerviceCallback(); //TODO dodać parametr
}
