package com.dyszlewskiR.edu.scientling.services.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.database.dao.CategoryDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.DefinitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.HintDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LanguageDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LessonDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.PartOfSpeechDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.RepetitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SentenceDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SetDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.TranslationDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.HintsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsHintsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.database.utils.QueryReader;
import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.params.WordsListParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Hint;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Image;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Language;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Repetition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Translation;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.data.models.params.AnswersParams;
import com.dyszlewskiR.edu.scientling.data.models.params.QuestionsParams;
import com.dyszlewskiR.edu.scientling.data.models.params.WordsParams;
import com.dyszlewskiR.edu.scientling.services.builders.AnswerSelectionBuilder;
import com.dyszlewskiR.edu.scientling.services.builders.FlashcardSelectionBuilder;
import com.dyszlewskiR.edu.scientling.services.builders.LearningSelectionBuilder;
import com.dyszlewskiR.edu.scientling.services.builders.QuestionSelectionBuilder;
import com.dyszlewskiR.edu.scientling.services.builders.WordSelectionBuilder;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.LessonsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.WordsColumns;

/**
 * Created by Razjelll on 10.11.2016.
 */

public class DataManager {

    private final String TAG = "DataManager";

    private final String QUERIES_FOLDER = "sql/queries/";
    private final String SELECT_LIST_WORDS_QUERY = "selectListWords.sql";

    private Context mContext;
    private SQLiteDatabase mDb;

    private WordDao mWordDao; //TODO przetestować, gdzie bedzie działało lepiej
    private CategoryDao mCategoryDao;
    private SetDao mSetDao;


    public DataManager(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);

        /*if (BuildConfig.DEBUG) //TODO sppawdzić dokładnie jak działa BuildConfig.DEBUG
        {
            dbHelper.setFileOpener(new ResourcesFileOpener());
        }*/

        mDb = dbHelper.getWritableDatabase();

        mWordDao = new WordDao(mDb);
        mCategoryDao = new CategoryDao(mDb);
        mSetDao = new SetDao(mDb);
    }


    public Word getWord(long id) {
        Word word = mWordDao.get(id);
        completeWord(word);
        return word;
    }

    private void completeWord(Word word) {
        if (word != null) {
            getAndSetTranslations(word);
            getAndSetSentences(word);
            getAndSetHints(word);
        }
    }

    private void getAndSetTranslations(Word word) {
        TranslationDao translationDao = new TranslationDao(mDb);
        ArrayList<Translation> translations = translationDao.getLinked(word.getId());
        if (translations != null) {
            word.setTranslations(translations);
        }
    }

    private void getAndSetSentences(Word word) {
        SentenceDao sentenceDao = new SentenceDao(mDb);
        ArrayList<Sentence> sentences = (ArrayList<Sentence>) sentenceDao.getLinked(word.getId());
        if (sentences != null) {
            word.setSentences(sentences);
        }
    }

    private void getAndSetHints(Word word) {
        HintDao hintDao = new HintDao(mDb);
        ArrayList<Hint> hints = (ArrayList<Hint>) hintDao.getLinked(word.getId());
        if (hints != null) {
            word.setHints(hints);
        }
    }




    public long saveWord(Word word) {
        mDb.beginTransaction();

        //zapisywanie definicji
        long definitionId = saveDefinition(word.getDefinition());
        if (definitionId > 0) {
            word.getDefinition().setId(definitionId);
        }
        //zapisywanie kategorii nie jest potrzebne, ponieważ kategoria jest ustalona odgórnie
        /*long categoryId = saveCategory(word.getCategory());
        if (categoryId > 0) {
            word.getCategory().setId(categoryId);
        }*/

        assert word.getTranslations() != null;

        WordDao wordDao = new WordDao(mDb);
        long wordId = wordDao.save(word);
        saveTranslations(word.getTranslations(), wordId);
        if (word.getSentences() != null) {
            saveSentences(word.getSentences(), wordId);
        }
        if(word.getHints()!=null){
            saveHints(word.getHints(), wordId);
        }


        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return wordId;
    }

    /**
     * Metoda zapisująca definicje w bazie danych.
     * Na początku sptawdzamy  czy istnieje definicja posiadająca takią zamą zawartośc oraz takie samo tłumaczenie.
     * Jeżeli zawiera zwracamy pobrany numer id. Jeśli nie zapisujemy definicje do bazy danych i zwracamy
     * numer id.
     * Sprawdzamy zawartość oraz tłumaczenie z tego powodu, że różni autorzy zestawów mogą na
     * różne sposoby zapisywać swoje definicje. Jedni mogą umieścić pewną definicję bez tłumaczenia
     * drudzy z tłumaczeniem. Jeśli sprawdzalibyśmy tylko zawartość mogło by dojśc do sytuacji,
     * gdzie zmienionoby definicje z innego zestawu. Była by to ingerencja w zawartość zestawu do
     * w który nie powinniśmy lub niechcielibyśmy ingerować.
     * @param definition
     * @return
     */
    private long saveDefinition(Definition definition) {

        DefinitionDao definitionDao = new DefinitionDao(mDb);
        long definitionId = 0;
        if(definition !=null){
            definitionId = definitionDao.getIdByContentAndTranslation(definition.getContent(), definition.getTranslation());
            if(definitionId<=0){
                definitionId = definitionDao.save(definition);
            }
            return definitionId;
        }
        return -1;
    }

    public long saveCategory(Category category) {
        if (category != null) {
            long categoryId = mCategoryDao.save(category);
            return categoryId;
        }
        return -1;
    }

    /**
     * Dla każdego tłumaczenia
     *      spróbuj pobrać tłumaczenie po wartości
     *      jesli nie ma takiego w bazie zapisujemy je
     *      łączymy słówko z tłumaczeniem
     * @param translationsList
     * @param wordId
     */
    private void saveTranslations(ArrayList<Translation> translationsList, long wordId) {
        //TODO pozmieniać nazwy translation i t
        Translation existingTranslation = null;
        TranslationDao translationDao = new TranslationDao(mDb);
        for (Translation translation : translationsList) {
            existingTranslation = translationDao.getByContent(translation.getContent());
            long translationId;
            if (existingTranslation == null) {
                translationId = translationDao.save(translation);
            } else {
                translationId = existingTranslation.getId();
            }
            translationDao.link(translationId, wordId);
        }
        translationDao.deleteUnlinked();
    }

    private void saveSentences(ArrayList<Sentence> sentencesList, long wordId) {
        Sentence existingSentence = null;
        SentenceDao sentenceDao = new SentenceDao(mDb);
        for (Sentence s : sentencesList) {
            existingSentence = sentenceDao.getByContent(s.getContent());
            long sentenceId;
            if (existingSentence == null) {
                sentenceId = sentenceDao.save(s);
            } else {
                sentenceId = existingSentence.getId();
            }
            sentenceDao.link(sentenceId, wordId);
        }
        sentenceDao.deleteUnlinked();
    }

    private void saveHints(List<Hint> hintsList, long wordId){
        Hint existingHint = null;
        HintDao hintDao = new HintDao(mDb);
        for(Hint hint : hintsList){
            existingHint = hintDao.getByContent(hint.getContent());
            long hintId;
            if(existingHint==null){
                hintId = hintDao.save(hint);
            } else {
                hintId = existingHint.getId();
            }
            hintDao.link(hintId, wordId);
        }
        hintDao.deleteUnlinked();
    }

    public void updateWord(Word word){
        mDb.beginTransaction();
        TranslationDao translationDao = new TranslationDao(mDb);
        translationDao.unLink(word.getId());
        saveTranslations(word.getTranslations(),word.getId());
        SentenceDao sentenceDao = new SentenceDao(mDb);
        sentenceDao.unlink(word.getId());
        saveSentences(word.getSentences(),word.getId());
        long definitionId = saveDefinition(word.getDefinition());
        if(definitionId > 0){
            word.getDefinition().setId(definitionId);
        }
        WordDao wordDao = new WordDao(mDb);
        wordDao.update(word);
        //TODO dodać metody usuwające niepowiązane tłumaczenia i zdania
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public void deleteWord(Word word){
        mDb.beginTransaction();
        TranslationDao translationDao = new TranslationDao(mDb);
        translationDao.unLink(word.getId());
        SentenceDao sentenceDao = new SentenceDao(mDb);
        sentenceDao.unlink(word.getId());
        WordDao wordDao = new WordDao(mDb);
        wordDao.delete(word);

        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public List<Word> getQuestions(QuestionsParams params) {
        String where = QuestionSelectionBuilder.getStatement(params);
        String[] whereArguments = QuestionSelectionBuilder.getArguments(params);
        String limit = null;
        if (params.getLimit() > 0) {
            limit = String.valueOf(params.getLimit());
        }
        List<Word> words = mWordDao.getAllWithJoins(false, where, whereArguments,
                "RANDOM()", null, null, limit);
        for (Word word : words) {
            completeWord(word);
        }
        return (List<Word>) words;
    }

    public List<Word> getAnswers(AnswersParams params) {
        String where = AnswerSelectionBuilder.getStatement(params);
        String[] whereArguments = AnswerSelectionBuilder.getArguments(params);
        String limit = null;
        if (params.getLimit() > 0) {
            limit = String.valueOf(params.getLimit());
        }
        String[] columns = {WordsColumns.ID, WordsColumns.CONTENT};
        List<Word> words = mWordDao.getAll(false, columns, where, whereArguments,
                "RANDOM()", null, null, limit);
        //Jeżeli pobierano słówka z danej lekcji lub kategorii i pobrano niewystarczającą ich liczbę
        //należy pobrać brakujace słowa spoza lekcji lub kategorii
        if((params.getCategoryId()>0 || params.getLessonId() >0) && words.size() < params.getLimit())
        {
            //TODO REFAKTORYZACJA
            params.setLessonId(0);
            params.setCategoryId(0);
            long[] previousWordsIds = new long[words.size()];
            for(int i =0; i< words.size(); i++)
            {
                previousWordsIds[i] = words.get(i).getId();
            }
            params.setPreviousWordsIds(previousWordsIds);
            int newLimit = params.getLimit() - words.size();
            params.setLimit(newLimit);
            where = AnswerSelectionBuilder.getMoreStatement(params);
            whereArguments = AnswerSelectionBuilder.getMoreArguments(params);

            List<Word> newWords = mWordDao.getAll(false, columns, where, whereArguments,
                    "RANDOM()",null,null,String.valueOf(newLimit));
            words.addAll(newWords);

        }
        assert params.getLimit() <=0 || words.size() == params.getLimit();
        for (Word word : words) {
            getAndSetTranslations(word);
        }

        return words;
    }

    public List<VocabularySet> getSets() {
        List<VocabularySet> sets = new SetDao(mDb).getAll();
        //TODO niezbyt dobre rozwiązanie, chyba lepiej będzie pobierać bezpośrednio w zapytaniu języki
        LanguageDao languageDao = new LanguageDao(mDb);
        for (VocabularySet s : sets) {
            s.setLanguageL2(languageDao.get(s.getLanguageL2().getId()));
            s.setLanguageL1(languageDao.get(s.getLanguageL1().getId()));
        }
        return sets;
    }

    public VocabularySet getSetById(long id) {
        return mSetDao.get(id);
    }

    public List<Category> getCategories() {
        return mCategoryDao.getAll();
    }

    public List<Language> getLanguages() {
        LanguageDao dao = new LanguageDao(mDb);
        return dao.getAll();
    }

    public long saveSet(VocabularySet set) {
        mDb.beginTransaction();
        long id = mSetDao.save(set);
        if (id > 0) {
            mDb.setTransactionSuccessful();
        }
        mDb.endTransaction();
        return id;
    }

    public List<Lesson> getLessons(VocabularySet set) {
        LessonDao lessonDao = new LessonDao(mDb);
        if (set == null) {
            return lessonDao.getAll();
        }
        String where = LessonsColumns.SET_FK + " = ?";
        String orderBy = LessonsColumns.NUMBER;
        return lessonDao.getAll(true, LessonsTable.getColumns(), where, new String[]{String.valueOf(set.getId())}, null, null, orderBy, null);
    }

    public List<Lesson> getLessonsWithProgress(VocabularySet set) {
        List<Lesson> lessons = new ArrayList<>();
        LessonDao lessonDao = new LessonDao(mDb);
        if (set == null) return lessons;
        ArrayList<String> queryColumns = new ArrayList<>(Arrays.asList(LessonsTable.getColumns()));
        /*String queryPart = "(SELECT COUNT(1) FROM " + WordsTable.TABLE_NAME
                + " WHERE " + WordsColumns.LESSON_FK + " = " + LessonsTable.TABLE_NAME + "." + LessonsColumns.ID;
        String progressColumn = queryPart + " AND " + WordsColumns.LEARNING_DATE + " IS NOT NULL)*1.0/"
                + queryPart + ")*100 AS X";*/


        String wordCount = new StringBuilder()
                .append("(SELECT COUNT(1) FROM ").append(WordsTable.TABLE_NAME)
                .append(" WHERE ").append(WordsColumns.LESSON_FK).append("=").append(LessonsTable.TABLE_NAME + "." + LessonsColumns.ID)
                .toString();
        String progressColumn = new StringBuilder(wordCount)
                .append(" AND ").append(WordsColumns.LEARNING_DATE).append(" IS NOT NULL)")
                .append("*1.0/").append(wordCount).append(")")
                .toString();
        queryColumns.add(progressColumn);


        String where = LessonsColumns.SET_FK + " =? AND" + wordCount +")<>0";
        String[] whereArguments = {String.valueOf(set.getId())};
        String groupBy = LessonsColumns.ID;
        String orderBy = LessonsColumns.NUMBER;
        lessons = lessonDao.getAll(false, queryColumns.toArray(new String[0]), where, whereArguments,
                groupBy, null, orderBy, null);
        return lessons;
    }

    public long saveLesson(Lesson lesson) {
        LessonDao dao = new LessonDao(mDb);
        mDb.beginTransaction();
        long id = dao.save(lesson);
        if (id > 0) {
            mDb.setTransactionSuccessful();
        }
        mDb.endTransaction();
        return id;
    }


    public List<Word> getAllWords() {
        List<Word> words = mWordDao.getAll();
        for (Word word : words) {
            completeWord(word);
        }
        return words;
    }

    public List<Word> getWords(WordsParams params) {
        assert params.getSetId() > 0;
        StringBuilder whereBuilder = new StringBuilder();

        ArrayList<String> whereArgumentsList = new ArrayList<>();
        //słówko musi być z podanego zestawu
        whereBuilder.append(WordsColumns.LESSON_FK + " IN (SELECT " + LessonsColumns.ID + " FROM " + LessonsTable.TABLE_NAME);
        whereBuilder.append(" WHERE " + LessonsColumns.SET_FK + " = ?)");
        whereArgumentsList.add(String.valueOf(params.getSetId()));
        if (params.getLessonId() > 0) {
            //słówka muszą być z podanej lekcji lub z lekcji nastepnej. W przypadku jesli lekcja
            //nie zostaje podana, słówka są wyszukiwane w obrebie całego zestawu
            whereBuilder.append(" AND " + WordsColumns.LESSON_FK + ">= ?");
            whereArgumentsList.add(String.valueOf(params.getLessonId()));
        }
        if (params.getCategoryId() > 0) {
            whereBuilder.append(" AND " + WordsColumns.CATEGORY_FK + " =?");
            whereArgumentsList.add(String.valueOf(params.getCategoryId()));
        }
        if (params.getDifficult() > 0) {
            //słówka muszą mieć podany lub wyższy stopień trudności
            whereBuilder.append(" AND " + WordsColumns.MASTER_LEVEL + ">=?");
            whereArgumentsList.add(String.valueOf(params.getDifficult()));
        }
        if(params.getType() == WordsParams.SELECTED_WORDS){
            whereBuilder.append(" AND "  + WordsColumns.SELECTED + "=?");
            whereArgumentsList.add("1");
        }
        if(params.getType() == WordsParams.OWN_WORDS){
            whereBuilder.append(" AND " + WordsColumns.OWN + "=?");
            whereArgumentsList.add("1");
        }
        String where = whereBuilder.toString();
        String[] whereArguments = whereArgumentsList.toArray(new String[0]);
        int limitValue = params.getLimit();
        String limit = null;
        if(limitValue != 0){
            limit = String.valueOf(limitValue);
        }

        String order = WordsColumns.LESSON_FK + ", " + WordsColumns.DIFFICULT;

        List<Word> words = mWordDao.getAllWithJoins(true, where, whereArguments, null, null, order, limit);
        for (Word word : words) {
            completeWord(word);
        }
        return words;
    }

    public List<Word> getWords(WordsListParams params) throws IOException {
        WordSelectionBuilder builder = new WordSelectionBuilder();
        String where = null;
        String[] whereArguments = null;
        if(params.getSetId() >0){
            builder.append(WordSelectionBuilder.Parts.SET_PART);
            whereArguments = new String[]{String.valueOf(params.getSetId())};
        }
        switch (params.getTab()){
            case ALL:
                break;
            case OWN:
                if(!builder.toString().equals("")){
                    builder.append(WordSelectionBuilder.Parts.AND);
                }
                builder.append(WordSelectionBuilder.Parts.OWN_PART);
                break;
            case HARD:
                if(!builder.toString().equals("")){
                    builder.append(WordSelectionBuilder.Parts.AND);
                }
                builder.append(WordSelectionBuilder.Parts.SELECTED_PART);
                break;
        }
        if(!builder.toString().equals("")) {
            where = builder.toString();
        }
        String statement = QueryReader.getQuery(QUERIES_FOLDER+SELECT_LIST_WORDS_QUERY, mContext);
        List<Word> words = mWordDao.getAll(statement, where, whereArguments, null,null,null,null);
        for (Word word: words) {
            completeWord(word);
        }
        return words;
    }

    public List<Word> getWords(LearningParams params) {
        String where = LearningSelectionBuilder.getStatement(params);
        String[] whereArguments = LearningSelectionBuilder.getArguments(params);
        String order = null;
        switch (params.getOrder()){
            case RANDOM:
                order = "RANDOM()";
                break;
            case LESSON:
                order = WordsColumns.LESSON_FK;
                break;
            case DIFFICULT:
                order = WordsColumns.DIFFICULT;
                break;
            case LESSON_AND_DIFFICULT:
                order = WordsColumns.LESSON_FK + ", " + WordsColumns.DIFFICULT;
                break;
        }
        List<Word> words = mWordDao.getAllWithJoins(true, where, whereArguments, null, null,
                order,String.valueOf(params.getLimit()));
        for(Word word:words) {
            completeWord(word);
        }
        return words;
    }

    public List<Word> getWords(FlashcardParams params){
        String where = FlashcardSelectionBuilder.getStatement(params);
        String[] whereArguments = FlashcardSelectionBuilder.getArguments(params);
        String order = null;
        switch (params.getChoiceType()){
            case RANDOM:
                order = "RANDOM()";
                break;
            case LAST_LEARNED:
                order = WordsColumns.LEARNING_DATE;
                break;
            case ONLY_LEARNED:
                order = "RANDOM()";
                break;
        }
        if (mWordDao == null){
            mWordDao = new WordDao(mDb);
        }
        List<Word> words = mWordDao.getAllWithJoins(true, where, whereArguments, null,null,
                order,String.valueOf(params.getLimit()));
        for(Word word: words){
            completeWord(word);
        }
        return words;
    }

    /**Metoda pobierająca domyślną lekcję w danym zestawie słówek.
     * Domyślna lekcja nie ma nazwy i zawsze ma numer 0. Do domyślnej lekcji zostana przypisane
     * wszystkie słówka, które nie zostały przypisane do żadnej innej lekcji
     * @param setId numer identyfikacyjny zestawu z którego chcemy pobrać domyślną lekcję
     * @return domyślna lekcja z podanego zestawu
     */
   public Lesson getDefaultLesson(long setId) {
       LessonDao lessonDao = new LessonDao(mDb);
       String where = LessonsColumns.SET_FK + "=? AND " + LessonsColumns.NUMBER + "=?";
       String[] whereArguments = {String.valueOf(setId), String.valueOf(Constants.DEFAULT_LESSON_NUMBER)};
       //wykorzystano metodę pobierającą listę słowek aby móc wprowadzić selekcję
       //ograniczamy liste wyników do 1(ponieważ moze być tylko jedna lekcja domyślna)
       //bierzemy tylko pierwszy element z listy pobranych lekcji
       Lesson lesson = lessonDao.getAll(false, LessonsTable.getColumns(), where, whereArguments,
               null, null, null, String.valueOf(1)).get(0);
       return lesson;
   }

    public int getRepetitionsCount(long setId, int date){
        String where = new WordSelectionBuilder().append(WordSelectionBuilder.Parts.SET_PART)
                .append(WordSelectionBuilder.Parts.AND).append(WordSelectionBuilder.Parts.REPETITION_PART)
                .toString();
        String[] whereArguments = {String.valueOf(setId), String.valueOf(date)};
        int count = new WordDao(mDb).getCount(where, whereArguments);
        return count;
    }

    public void release() {
        mDb.close();
    }

    public void saveRepetitionAndUpdateWords(List<Repetition> repetitions, List<Word> words){
        RepetitionDao repetitionDao = new RepetitionDao(mDb);
        WordDao wordDao = new WordDao(mDb);
        mDb.beginTransaction();
        for(Repetition repetition: repetitions){
            switch(repetition.getAction()){
                case DELETE:
                    repetitionDao.delete(repetition);
                    break;
                case SAVE:
                    repetitionDao.save(repetition);
                    break;
                case UPDATE:
                    repetitionDao.update(repetition);
                    break;
            }
        }
        for(Word word:words){
            wordDao.update(word);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public Lesson getLessonById(long lessonId){
        LessonDao lessonDao = new LessonDao(mDb);
        return lessonDao.get(lessonId);
    }

    public void deleteLesson(Lesson lesson, long newLessonId){
        if(lesson != null){
            mDb.beginTransaction();
            if(newLessonId == -1){ //usuwanie słówek
                deleteWordsFromLesson(lesson);
            } else { //zmiana lekcji słówek na domyślną
                changeWordsLesson(lesson, newLessonId);
            }
            LessonDao lessonDao = new LessonDao(mDb);
            lessonDao.delete(lesson);

            mDb.setTransactionSuccessful();
            mDb.endTransaction();
        }
    }

    private void deleteWordsFromLesson(Lesson lesson){
        WordDao wordDao = new WordDao(mDb);
        TranslationDao translationDao = new TranslationDao(mDb);
        SentenceDao sentenceDao = new SentenceDao(mDb);
        HintDao hintDao = new HintDao(mDb);
        DefinitionDao definitionDao = new DefinitionDao(mDb);
        deleteWordsFromLesson(lesson, wordDao, translationDao, sentenceDao, hintDao, definitionDao);
    }

    private void changeWordsLesson(Lesson lesson, long newLessonId){
        WordDao wordDao = new WordDao(mDb);
        String where = WordsColumns.LESSON_FK + "=?";
        String[] whereArguments = {String.valueOf(lesson.getId())};
        wordDao.update(WordsColumns.LESSON_FK, String.valueOf(newLessonId),where,whereArguments);
    }

    /**
     * Usuwa cały zestaw z bazy danych.
     * Najpierw usuwa wszystkie lekcje które wchodziły w skład zestawu, oraz wszystkie słowka
     * które były przypisane do usuwanych lekcji.
     * Później następuje usunięcie zestawu
     * @param set zestaw który chcemy usunąć
     */
    public void deleteSet(VocabularySet set){
        if(set!=null){

            mDb.beginTransaction();
            deleteLessonsFromSet(set);
            SetDao setDao = new SetDao(mDb);
            setDao.delete(set);

            mDb.setTransactionSuccessful();
            mDb.endTransaction();
        }
    }

    /**
     * MMetoda pomocnicza, która zajmuje się usuwanie wszystkich lekcji w zestawie.
     * Wraz z zestawami usuwane są także wszystkie słówka które wchodza w skład tej lekcji,
     * oraz wszystkie powiązane z nimi informacje(takie jak tłumaczenia, zdania, podpowiedzi)
     * @param set
     */
    private void deleteLessonsFromSet(VocabularySet set){
        LessonDao lessonDao = new LessonDao(mDb);
        WordDao wordDao = new WordDao(mDb);
        TranslationDao translationDao = new TranslationDao(mDb);
        SentenceDao sentenceDao = new SentenceDao(mDb);
        HintDao hintDao = new HintDao(mDb);
        DefinitionDao definitionDao = new DefinitionDao(mDb);
        List<Lesson> lessons = getLessons(set);
        for(Lesson lesson : lessons){
            //najpierw usuwamy wszystkie słówka znajdujące się w danej lekcji
            deleteWordsFromLesson(lesson, wordDao, translationDao, sentenceDao, hintDao, definitionDao);
            //później usuwamy lekcję
            lessonDao.delete(lesson);
        }
    }

    /**
     * Usuwa wszystkie słowka z podanej lekcji.
     * Na początku usuwane jest słówko. Podczas tego procesu zostaną usunięte słówka i rekordy łączące
     * słówko z innymi tabelami. Dzieje się tak dzięki ustawienion ON DELETE CASCADE.
     * Następnie usuwamy wszystkie zdania, tłumaczenia i pomoce które były powiązane z tymi słowkami.
     * Robimy to za pomocą metod deleteUnlinked, które usuwają wartości, które nie zostaną odnalezione
     * w tabelach łączących. Dzięki temu nie usunięmy wartości która jest wykorzystywana przez inne słówko.
     * Dodatkowo możemy usunąć wszystkie niepotrzebne wartośći za pomocą jednego zapytania.
     * W parametrze otrzymujemy potrzebne obiekty dao, ponieważ ta metoda będzie zapewne wykonywana
     * kilkokrotnie podczas usuwania zestawu. Zapobiega to kilkukrotnemu tworzeniu nowych obiektó dao.
     * @param lesson
     */
    private void deleteWordsFromLesson(Lesson lesson, WordDao wordDao, TranslationDao translationDao,
                                      SentenceDao sentenceDao, HintDao hintDao, DefinitionDao definitionDao){


        String selection = WordsColumns.LESSON_FK + "=?";
        String[] selectionArgs = {String.valueOf(lesson.getId())};
        int deletedWords = wordDao.delete(selection, selectionArgs);
        int deletedTranslations = translationDao.deleteUnlinked();
        int deletedSentences = sentenceDao.deleteUnlinked();
        int deletedHints = hintDao.deleteUnlinked();
        int deletedDefinitions = definitionDao.deleteUnlinked();
        Log.d(TAG, "DeletedWords : " + deletedWords);
        Log.d(TAG, "DeletedTranslations: " + deletedTranslations);
        Log.d(TAG, "DeletedSentences: " + deletedSentences);
        Log.d(TAG, "DeletedHints: " + deletedHints);
        Log.d(TAG, "DeletedDefinitions: " + deletedDefinitions);
    }

    /**
     * Usuwanie kategorii
     * - ustawienie kategorii słówek na null
     * - usunięcie kategorii
     * @param category kategoria która ma zostać usunięta
     */
    public void deleteCategory(Category category){
        if(category != null){
            mDb.beginTransaction();
            WordDao wordDao = new WordDao(mDb);
            String where = WordsColumns.CATEGORY_FK + "=?";
            String[] whereArguments = {String.valueOf(category.getId())};
            wordDao.update(WordsColumns.CATEGORY_FK, "NULL", where, whereArguments);
            CategoryDao categoryDao = new CategoryDao(mDb);
            categoryDao.delete(category);

            mDb.setTransactionSuccessful();
            mDb.endTransaction();
        }
    }

    public void updateSet(VocabularySet set){
        if(set!=null && set.getId() != Constants.DEFAULT_SET_ID){
            SetDao setDao = new SetDao(mDb);
            setDao.update(set);
        }
    }

    public void updateLesson(Lesson lesson){
        if(lesson != null && lesson.getNumber() != Constants.DEFAULT_LESSON_NUMBER){
            LessonDao lessonDao = new LessonDao(mDb);
            lessonDao.update(lesson);
        }
    }

    public void updateCategory(Category category){
        if(category !=null) {
            CategoryDao categoryDao = new CategoryDao(mDb);
            categoryDao.update(category);
        }
    }

    public int getWordsCountInLesson(long lessonId){
        String where = WordsColumns.LESSON_FK +"=?";
        String[] whereArguments = {String.valueOf(lessonId)};
        WordDao wordDao = new WordDao(mDb);
        return wordDao.getCount(where, whereArguments);
    }

    public Category getCategoryByName(String name){
        CategoryDao categoryDao = new CategoryDao(mDb);
        String where = CategoriesTable.CategoriesColumns.NAME + "=?";
        String[] whereArguments = {name};
        List<Category> categories = categoryDao.getAll(false ,where, whereArguments,
                null,null,null,"1");
        if(categories.size()!=0){
            return categories.get(0);
        }
        return null;
    }

    public Hint getHintByContent(String content){
        HintDao hintDao = new HintDao(mDb);
        String where = HintsTable.HintsColumns.CONTENT +"=?";
        String[] whereArguments = {content};
        List<Hint> hints = hintDao.getAll(false, where, whereArguments,
                null,null,null,"1");
        if(hints!=null && hints.size()!=0){
            return hints.get(0);
        }
        return null;
    }

    public List<PartOfSpeech> getPartsOfSpeech(){
        PartOfSpeechDao partOfSpeechDao = new PartOfSpeechDao(mDb);
        return partOfSpeechDao.getAll();
    }

    public List<String> getImagesNamesFromLesson(long lessonId){
        WordDao wordDao = new WordDao(mDb);
        String query = "SELECT " + WordsColumns.IMAGE_NAME
                + " FROM " + WordsTable.TABLE_NAME
                + " WHERE " + WordsColumns.LESSON_FK + "=?";
        String[] arguments = {String.valueOf(lessonId)};
        List<String> imageNames = wordDao.getNamesList(query, arguments);
        return imageNames;
    }

    public List<String> getRecordsNamesFromLesson(long lessonId){
        WordDao wordDao = new WordDao(mDb);
        String query = "SELECT " + WordsColumns.RECORD_NAME
                + " FROM " + WordsTable.TABLE_NAME
                + " WHERE " + WordsColumns.LESSON_FK + "=?";
        String[] arguments = {String.valueOf(lessonId)};
        List<String> recordsNames = wordDao.getNamesList(query, arguments);
        return recordsNames;
    }
}
