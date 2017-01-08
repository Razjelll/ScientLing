package com.dyszlewskiR.edu.scientling.preferences;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dyszlewskiR.edu.scientling.R;

/**
 * Created by Razjelll on 28.12.2016.
 */

public class Preferences {

    public enum AnswerConnection
    {
        LACK(1),
        LESSON(2),
        CATEGORY(3);
        private int id;
        AnswerConnection(int id){
            this.id = id;
        }
        int getValue(){return id;}
        String getStringValue(){return String.valueOf(id);}
    }

    public enum ExerciseDirection{
        L1_TO_L2(0),
        L2_TO_L1(1);
        private int id;
        ExerciseDirection(int id) {this.id=id;}
        int getValue(){return id;}
        String getStringValue(){return String.valueOf(id);}
    }



    public static final String WORDS_IN_LEARNING_PREF = "prefWordsInLearning";
    public static final String WORDS_IN_REPETITION_PREF = "prefWordsInRepetitions";
    public static final String NUMBER_ANSWER_PREF = "prefNumberAnswers";
    public static final String ANSWER_CONNECTION_PREF = "prefAnswerConnection";
    public static final String DEFAULT_EXERCISE_PREF ="prefDefaultExercise";
    public static final String DEFAULT_DIRECTION_PREF="prefDefaultDirection";
    public static final String NUMBER_FLASHCARD_PREF = "prefNumberFlashcard";
    public static final String ORDER_LEARNING_PREF = "prefOrderLearning";

    public static int getNumberWordsInLearning(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(WORDS_IN_LEARNING_PREF,null);
        int value = Integer.valueOf(valueString);
        return value;
    }

    public static int getNumberWordsInRepetitions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(WORDS_IN_REPETITION_PREF, null);
        int value = Integer.valueOf(valueString);
        return value;
    }

    public static int getNumberAnswers(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(NUMBER_ANSWER_PREF,null);
        int value = Integer.valueOf(valueString);
        return value;
    }

    public static int getNumberFlashcards(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(NUMBER_FLASHCARD_PREF, null);
        int value = Integer.valueOf(valueString);
        return value;
    }

    public static int getDefaultExercise(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(DEFAULT_EXERCISE_PREF, null);
        int value = Integer.valueOf(valueString);
        return value;
    }

    public static AnswerConnection getAnswerConnection(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(ANSWER_CONNECTION_PREF, null);
        int position = Integer.valueOf(valueString);
        return AnswerConnection.values()[position];
        /*if(valueString.equals(AnswerConnection.LACK.getStringValue()))
        {
            return AnswerConnection.LACK;
        }
        if(valueString.equals(AnswerConnection.LESSON.getStringValue()))
        {
            return AnswerConnection.LESSON;
        }
        return AnswerConnection.CATEGORY; //w przeciwnym razie*/
    }

    public static ExerciseDirection getExerciseDirection(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString = prefs.getString(DEFAULT_DIRECTION_PREF, null);
        int position = Integer.valueOf(valueString);
        return ExerciseDirection.values()[position];
    }

    public static int getOrderLearning(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String valueString =prefs.getString(ORDER_LEARNING_PREF, null);
        int value = Integer.valueOf(valueString);
        return value;
    }


}
