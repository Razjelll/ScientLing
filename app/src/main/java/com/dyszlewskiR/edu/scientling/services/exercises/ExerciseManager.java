package com.dyszlewskiR.edu.scientling.services.exercises;

import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.models.params.AnswersParams;
import com.dyszlewskiR.edu.scientling.data.models.params.QuestionsParams;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.RepetitionItem;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.utils.TranslationListConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private List<Word> mIncorrectAnswers;
    private List<Word> mAnswers;
    private IExercise mExerciseType;
    private IExerciseDirection mExerciseDirection;
    private DataManager mDataManager;

    private ExerciseParams mExerciseParams;

    public ExerciseManager(ExerciseParams exerciseParams, DataManager dataManager) {
        setupParams(exerciseParams);
        prepareFields();
        mDataManager = dataManager;

        QuestionsParams questionsParams = ExerciseParamsHelper.getQuestionParams(exerciseParams);
        mQuestions = mDataManager.getQuestions(questionsParams);
        mNumQuestions = mQuestions.size();
        mQuestionsQueue = getFilledQuestionQueue(mNumQuestions);

        //od razu pobieramy pierwszy element z kolejki, ponieważ pierwsze pytanie zostaje wyświetlane
        //po załadowaniu się obiektu. Jesli wartość 0 zostałaby w kolejce, słówko pojawiłoby się
        //na liście 2 razy
        if(!mQuestionsQueue.isEmpty()) {
            mCurrentQuestion = mQuestionsQueue.poll();
        }
    }

    private void setupParams(ExerciseParams params){
        mExerciseParams = params;
    }

    private void prepareFields() {
        mNumCorrectAnswers = 0;
        mNumIncorrectAnswers = 0;
        mToRepeat = new ArrayList<>();
        mIncorrectAnswers = new ArrayList<>();
    }

    private Queue<Integer> getFilledQuestionQueue(int numQuestion) {
        Queue<Integer> questionQueue = new LinkedList<>();
        for (int i = 0; i < numQuestion; i++) {
            questionQueue.add(i);
        }
        return questionQueue;
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

    public int getNumAnswers(){return mExerciseParams.getNumberAnswers();}

    public void setExerciseType(IExercise exerciseType) {
        mExerciseType = exerciseType;
    }

    public void setExerciseLanguage(IExerciseDirection exerciseLanguage) {
        mExerciseDirection = exerciseLanguage;
    }

    public String getQuestion() {
        return mExerciseDirection.getQuestion(getCurrentQuestionWord());
    }

    private Word getCurrentQuestionWord() {
        return mQuestions.get(mCurrentQuestion);
    }

    public String getTranscription() {
        return mExerciseDirection.getTranscription(getCurrentQuestionWord());
    }

    public int getRemainingQuestion() {
        return mQuestionsQueue.size();
    }

    public String[] getAnswers(int howMuch) {
        Word currentWord = getCurrentQuestionWord();
        AnswersParams answersParams = ExerciseParamsHelper.getAnswerParams(mExerciseParams, currentWord);
        mAnswers = mDataManager.getAnswers(answersParams);
        mAnswers.add(getCurrentQuestionWord()); //dodajemy prawidłową odpowiedź
        Collections.shuffle(mAnswers);
        //lista wartości odpowiedzi które będą wyświetlone w aktywności
        String[] answers = mExerciseDirection.getAnswers(mAnswers);
        return answers;
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
        String correctAnswer = mExerciseDirection.getAnswer(mQuestions.get(mCurrentQuestion));
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
        return mExerciseDirection.getAnswer(mQuestions.get(mCurrentQuestion));
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

    private static class ExerciseParamsHelper {
        public static QuestionsParams getQuestionParams(ExerciseParams exerciseParams) {
            QuestionsParams questionsParams = new QuestionsParams();
            questionsParams.setSetId(exerciseParams.getSetId());
            if(exerciseParams.isRepetitionDate())
            {
                questionsParams.setMonth(exerciseParams.getRepetitionMonth());
                questionsParams.setDay(exerciseParams.getRepetitionDay());
            }
            questionsParams.setLimit(exerciseParams.getNumberQuestion());

            return questionsParams;
        }

        public static AnswersParams getAnswerParams(ExerciseParams exerciseParams, Word word) {
            AnswersParams answersParams = new AnswersParams();
            answersParams.setSetId(exerciseParams.getSetId());
            if(exerciseParams.isAnswerFromLesson()) {
                answersParams.setLessonId(word.getLessonId());
            }
            if(exerciseParams.isAnswerFromCategory()){
                if(word.getCategory() != null){
                    answersParams.setCategoryId(word.getCategory().getId());
                }
            }
            answersParams.setUsedContent(word.getContent());
            String[] usedTranslations = TranslationListConverter.toStringArray(word.getTranslations());
            answersParams.setUsedTranslations(usedTranslations);
            answersParams.setLimit(exerciseParams.getNumberAnswers()-1);
            return answersParams;
        }
    }
}
