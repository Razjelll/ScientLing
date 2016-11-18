package com.dyszlewskiR.edu.scientling.services.exercises;

import android.content.Context;

import com.dyszlewskiR.edu.scientling.data.database.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Razjelll on 16.11.2016.
 */


public class ExerciseManager {

    private final int QUESTION_ANSWERS_RATIO = 3;

    private int mNumQuestions;
    private int mCurrentQuestion;
    private int mNumCorrectAnswers;
    private int mNumIncorrectAnswers;
    private ArrayList<Word> mQuestions;
    private ArrayList<Word> mAnswersL2;
    private ArrayList<Translation> mAnswersL1;
    private ArrayList<Word> mIncorrectAnswers;
    private Answer[] mAnswers; //TODO posprawdzać czy lepiej się sprawdzi to czy mAnswersL1 i L2
    private IExercise mExerciseType;
    private IExerciseLanguage mExerciseLanguage; //TODO przydałaby się lepsza nazwa
    private DataManager mDataManager;

    private long mSet;
    private long mLesson;
    private long mDifficult;
    private long mCategory;

    public ExerciseManager(ExerciseParameters parameters, Context context) {
        mSet = parameters.getSet();
        mLesson = parameters.getLesson();
        mDifficult = parameters.getDifficult();
        mCategory = parameters.getCategory();
        mNumQuestions = parameters.getNumQuestions();
        mCurrentQuestion = 0;
        mNumCorrectAnswers = 0;
        mNumIncorrectAnswers = 0;

        mDataManager = new DataManager(context);
        mQuestions = mDataManager.getQuestions(mSet, mLesson, mCategory, mDifficult, mNumQuestions);
        mIncorrectAnswers = new ArrayList<>();
    }

    public int getNumQuestions() {
        return mNumQuestions;
    }

    public int getCurrentQuestionNum() {
        return mCurrentQuestion;
    }

    public void setExerciseType(IExercise exerciseType) {
        mExerciseType = exerciseType;
    }

    public void setExerciseLanguage(IExerciseLanguage exerciseLanguage) {
        mExerciseLanguage = exerciseLanguage;
    }

    public String getQuestion() {
        return mExerciseLanguage.getQuestion(mQuestions.get(mCurrentQuestion));
    }

    public String getTranscription() {
        return mExerciseLanguage.getTranscription(mQuestions.get(mCurrentQuestion));
    }

    public String[] getAnswers(int howMuch) {
        //TODO dorobić zapamiętywanie odpowiedznich odpowiedzi
        ExerciseParameters parameters = new ExerciseParameters(mSet, -1, mDifficult, mCategory, mNumQuestions * QUESTION_ANSWERS_RATIO);
        String[] presentQuestion = new String[mQuestions.size()];
        for (int i = 0; i < mQuestions.size(); i++) {
            presentQuestion[i] = mQuestions.get(i).getContent();
        }
        mAnswers = mExerciseLanguage.getAnswers(parameters, presentQuestion, mDataManager);

        String[] answersArray = new String[mAnswers.length];
        for (int i = 0; i < mAnswers.length; i++) {
            answersArray[i] = mAnswers[i].getAnswer();
        }
        String correctAnswer = mExerciseLanguage.getAnswer(mQuestions.get(mCurrentQuestion));
        String[] resultArray = getRandomStringsFromArray(answersArray, howMuch, correctAnswer); //TODO wstawiona wartość poprawić
        return resultArray;
    }

    private String[] getRandomStringsFromArray(String[] fullArray, int howMuch, String correct) {
        //TODO trochę prymitywna metoda, odpowiedzi będą wyświetlane w kolejności alfabetycznej
        TreeSet<String> set = new TreeSet<>();
        Random random = new Random();
        int range = fullArray.length;
        set.add(correct);
        while (set.size() != howMuch) {
            int i = random.nextInt(range);
            String s = fullArray[i];
            if (!set.contains(s)) {
                set.add(s);
            }
        }
        return set.toArray(new String[0]);
    }

    public boolean checkAnswer(String answer) {
        String correctAnswer = mExerciseLanguage.getAnswer(mQuestions.get(mCurrentQuestion));
        boolean correct = mExerciseType.checkAnswer(answer, correctAnswer);
        if (correct) {
            mNumCorrectAnswers++;
        } else {
            mNumIncorrectAnswers--;
            mIncorrectAnswers.add(mQuestions.get(mCurrentQuestion)); //zapamiętujemy słówka które poszły nam źle
        }
        return mExerciseType.checkAnswer(answer, correctAnswer);
    }

    public String getCorrectAnswer() {
        return mExerciseLanguage.getAnswer(mQuestions.get(mCurrentQuestion));
    }

    public void nextQuestion() {
        mCurrentQuestion++;
    }

}
