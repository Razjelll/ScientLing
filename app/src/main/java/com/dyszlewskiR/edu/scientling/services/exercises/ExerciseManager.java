package com.dyszlewskiR.edu.scientling.services.exercises;

import android.content.Context;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.RepetitionItem;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Razjelll on 16.11.2016.
 */


public class ExerciseManager {
    private final String TAG = "ExerciseManager";

    private final int QUESTION_ANSWERS_RATIO = 3;

    /**
     * Liczba wszystkich pytań w danym zestawie ćwiczeń
     */
    private int mNumQuestions;

    /**
     * Numer obecnego pytania. Początkowo numer idzie od pierwszego do ostattniego,
     * później przechodzi po slówkach, na które użytkownik odpowiedział źle
     */
    private int mCurrentQuestion;

    /**
     * Liczba poprawnych odpowiedzi. Ta zmeinna służy do wypełnienia paska postępu ćwiczenia w ExerciseActivity
     * Cwiczenie kończy się, kiedy liczba poprawnych odpowiedzi będzie równa liczbie pytań
     */
    private int mNumCorrectAnswers;

    /**
     * Liczba niepoprawnych odpowiedzi użytkownika. Moze posłużyć do pokazania użytkownikowi statystyk
     * ćwiczenia
     */
    private int mNumIncorrectAnswers;

    /**
     * Lista słówek w ćwiczeniu na które użytkownik bedzie odpowiadał. Słówka losowane z bazy danych.
     */
    private List<Word> mQuestions;

    /**
     * Kolejka, w której znajdują się numery indeksu słówek do listy mQestions. Kolejka wyznacza kolejność
     * pokazywania ćwiczeń. Jeśli użytkownik odpowie źle pozycja słówka w liście mQuestions zostajnie dodany do
     * kolejki. Jeśli odpowie dobrze pozycja zostanie usunięta z kolejki
     */
    private Queue<Integer> mQuestionsQueue;

    /**
     * List przechowująca numery identyfikacyjne do słówek w tabeli mQuestions, na które użytkownik aplikacji odpowiedział.
     * Na podstawie tej listy będą wyznaczane powtóreki, aby zapisywać tylko te słówka, które
     * rzeczywiście zostaly przećwiczone. W innych programach tego typu do powtórek zostają zapisane
     * wszystkie słówka, które wchodziły w skład ćwiczenia.
     */
    private List<Integer> mToRepeat;

    private List<Word> mAnswersL2;
    private List<Translation> mAnswersL1;
    private List<Word> mIncorrectAnswers;
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
        mNumCorrectAnswers = 0;
        mNumIncorrectAnswers = 0;

        mDataManager = new DataManager(context);
        long start = System.currentTimeMillis();
        mQuestions = mDataManager.getQuestions(mSet, mLesson, mCategory, mDifficult, mNumQuestions);
        mQuestionsQueue = new LinkedList<>();
        //wypełnianie kolejki numerami identyfikacyjnymi słówek
        for (int i = 0; i < mNumQuestions; i++) {
            Log.d("ExerciseManager", mQuestions.get(i).getContent());
            mQuestionsQueue.add(i);
        }
        //od razu pobieramy pierwszy element z kolejki, ponieważ pierwsze pytanie zostaje wyświetlane
        //po załadowaniu się obiektu. Jesli wartość 0 zostałaby w kolejce, słówko pojawiłoby się
        //na liście 2 razy
        mCurrentQuestion = mQuestionsQueue.poll();
        mToRepeat = new ArrayList<>();

        long stop = System.currentTimeMillis(); //TODO później usunąć
        Log.d("ExerciseManager", String.valueOf(stop - start));
        mIncorrectAnswers = new ArrayList<>();
    }

    public int getNumCorrectAnswers() {
        return mNumCorrectAnswers;
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

    public int getRemainingQuestion() {
        return mQuestionsQueue.size();
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

    /**
     * Metoda sprawdzająca poprawność odpowiedzi użytkownika. Sprawdzenie odbywa się poprzez wywołanie
     * metody getAnswer() z klasy implementującej interfejs IExerciseLanguafe, która zwraca
     * poprawną odpowiedź dla danego pytania z uwzględnieniem, czy odpowiedź jest w jezyku L1, czy L2.
     * Otrzymana odpowiedź jest następnie wykorzystana w metodzie checkAnswer interfejsu IExercise
     * Tam nastepuje sprawdzenie, czy odpowiedź jest uznawana za słuszną. Odpowiedź moze być różnie
     * postrzegana jako poprawna. Podczas wpisywania będą uwzględniane literówki, a podczas
     * ćwiczenia wiem nie wiem będą uwzględniane tylko wartości know, not know, almoust know.
     *
     * @param answer
     * @return
     */
    public boolean checkAnswer(String answer) {
        String correctAnswer = mExerciseLanguage.getAnswer(mQuestions.get(mCurrentQuestion));
        boolean correct = mExerciseType.checkAnswer(answer, correctAnswer);

        serveAnswer(correct);

        if (mToRepeat.size() < mNumQuestions) {
            mToRepeat.add(mCurrentQuestion);
        }
        return correct;
    }

    private void serveAnswer(boolean correct) {
        if (correct) {
            mNumCorrectAnswers++;
        } else {
            mNumIncorrectAnswers++;
            mIncorrectAnswers.add(mQuestions.get(mCurrentQuestion));
            mQuestionsQueue.add(mCurrentQuestion);
            //mCurrentQuestion++;
        }
    }

    public String getCorrectAnswer() {
        return mExerciseLanguage.getAnswer(mQuestions.get(mCurrentQuestion));
    }

    /**
     * Funkcja zwracająca następne słowko z listy mQuestions. Najpierw pobiera odpowiedni ineks
     * z kolejki, a później element przecgowywany na liście mQuestions pod tym indeksem.
     * Z kolejki zostaje usunięty numer indeksu.
     */
    public void nextQuestion() {
        Log.d(TAG, "nextQuestion questionQueue.peek() : " + mQuestionsQueue.peek());
        if (!mQuestionsQueue.isEmpty()) {
            mCurrentQuestion = mQuestionsQueue.poll();
        }
    }

    /**
     * Metoda ustawiająca wszystkie zmienne na wartości początkowe. Metoda zostaje wykonana, keidy po
     * odpowiedzieniu poprawnie na wszystkie pytania użtykownik zarzyczy sobie kolejne przećwiczenie
     * materiału.
     */
    public void restart() {
        mNumCorrectAnswers = 0;
        mNumIncorrectAnswers = 0;

        for (int i = 0; i < mNumQuestions; i++) {
            mQuestionsQueue.add(i);
        }
        mCurrentQuestion = mQuestionsQueue.poll();
    }

    public List<RepetitionItem> prepareRepetitionsItems() {
        StringBuilder translationsBuilder = new StringBuilder();
        List<RepetitionItem> repetitions = new ArrayList<>();
        for (int i : mToRepeat) //dla każdego identyfikatora znajdującego się w mToRepeat
        {
            translationsBuilder.setLength(0);
            for (int translation = 0; translation < mQuestions.get(i).getTranslations().size(); translation++) {
                translationsBuilder.append(mQuestions.get(i).getTranslations().get(translation).getContent());
                if (translation != mQuestions.get(i).getTranslations().size() - 1) {
                    translationsBuilder.append(", ");
                }
            }
            repetitions.add(new RepetitionItem(mQuestions.get(i).getId(), mQuestions.get(i).getContent(),
                    translationsBuilder.toString()));
        }
        return repetitions;
    }
}
