package com.dyszlewskiR.edu.scientling.services.exercises;

import java.util.concurrent.ExecutionException;

/**
 * Created by Razjelll on 16.11.2016.
 */

public class ExerciseParameters {

    private long mSet;
    private long mLesson;
    private long mDifficult;
    private long mCategory;
    private int mNumQuestions;

    public ExerciseParameters(long set, long lesson, long difficult, long category, int numQuestion)
    {
        mSet = set;
        mLesson = lesson;
        mDifficult = difficult;
        mCategory = category;
        mNumQuestions = numQuestion;
    }

    public long getSet(){return mSet;}
    public long getLesson(){return mLesson;}
    public long getDifficult() {return mDifficult;}
    public long getCategory(){return mCategory;}
    public int getNumQuestions(){return mNumQuestions;}

    public void setSet(long setId){ mSet = setId;}
    public void setLesson(long lessonId){mLesson = lessonId;}
    public void setDifficult(long difficult){ mDifficult = difficult;}
    public void setCategory(long categoryId){mCategory = categoryId;}
    public void setNumQuestions(int numQuestions) {mNumQuestions = numQuestions;}
}
