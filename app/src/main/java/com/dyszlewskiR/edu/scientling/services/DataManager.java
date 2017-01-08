package com.dyszlewskiR.edu.scientling.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.database.dao.CategoryDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.DefinitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.HintDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LanguageDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LessonDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SentenceDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SetDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.TranslationDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.models.others.RepetitionDate;
import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Hint;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Language;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.LessonsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.WordsColumns;

/**
 * Created by Razjelll on 10.11.2016.
 */

public class DataManager {

    private Context mContext;
    private SQLiteDatabase mDb;

    private WordDao mWordDao; //TODO przetestować, gdzie bedzie działało lepiej
    private TranslationDao mTranslationDao;
    private SentenceDao mSentenceDao;
    private DefinitionDao mDefinitionDao;
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
        mTranslationDao = new TranslationDao(mDb);
        mSentenceDao = new SentenceDao(mDb);
        mDefinitionDao = new DefinitionDao(mDb);
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
            getAndSetTranslations(word, mDb);
            getAndSetSentences(word, mDb);
            getAndSetHints(word, mDb);
        }
    }

    private void getAndSetTranslations(Word word, SQLiteDatabase db) {
        TranslationDao translationDao = new TranslationDao(db);
        ArrayList<Translation> translations = translationDao.getLinked(word.getId());
        if (translations != null) {
            word.setTranslations(translations);
        }
    }

    private void getAndSetSentences(Word word, SQLiteDatabase db) {
        SentenceDao sentenceDao = new SentenceDao(db);
        ArrayList<Sentence> sentences = (ArrayList<Sentence>) sentenceDao.getLinked(word.getId());
        if (sentences != null) {
            word.setSentences(sentences);
        }
    }

    private void getAndSetHints(Word word, SQLiteDatabase db) {
        HintDao hintDao = new HintDao(db);
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
        //zapisywanie kategorii
        // TODO prawdopodobnie lepiej będzie leśli do zapisywania będziemy tworzyć nowe dao
        long categoryId = saveCategory(word.getCategory());
        if (categoryId > 0) {
            word.getCategory().setId(categoryId);
        }

        //Nie istnieje p[otrzeba aby zapisywać PartOfSpeech
        //Zapusywanie tłumaczeń, raczej nie przyjmujemy, że nie będzie przypisanych tłuamczeń do słówka
        assert word.getTranslations() != null;

        WordDao wordDao = new WordDao(mDb);
        long wordId = wordDao.save(word);
        saveTranslations(word.getTranslations(), wordId);
        if (word.getSentences() != null) {
            saveSentences(word.getSentences(), wordId);
        }


        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return wordId;
    }

    private long saveDefinition(Definition definition) {
        //TODO sprawdzić, czy trzeba sprawdzić czy defninicja istnieje w bazie.
        //Tearaz przyjumujemy, że każda daefinicja jest różna, więc jesli model ma definicje
        //zapisujemy ją
        if (definition != null) {
            long definitionId = mDefinitionDao.save(definition);
            return definitionId;
        }
        return -1;
    }

    private long saveCategory(Category category) {
        if (category != null) {
            //TODO ogarnąć jak powinno to wyglądać
            //sprawdzamy czy kategoria istnieje
            Category existingCategory = mCategoryDao.get(category.getId());
            if (existingCategory == null) {
                long categoryId = mCategoryDao.save(category);
                return categoryId;
            } else {
                return existingCategory.getId();
            }
        }
        return -1;
    }

    private void saveTranslations(ArrayList<Translation> translationsList, long wordId) {
        //TODO pozmieniać nazwy translation i t
        Translation existentTranslation = null;
        for (Translation translation : translationsList) {
            existentTranslation = mTranslationDao.getByContent(translation.getContent());
            long translationId;
            if (existentTranslation == null) {
                translationId = mTranslationDao.save(translation);
            } else {
                translationId = existentTranslation.getId();
            }
            mTranslationDao.link(translationId, wordId);
        }
    }

    public void saveSentences(ArrayList<Sentence> sentencesList, long wordId) {
        Sentence sentence = null;
        for (Sentence s : sentencesList) {
            sentence = mSentenceDao.getByContent(s.getContent());
            long sentenceId;
            if (sentence == null) {
                sentenceId = mSentenceDao.save(s);
            } else {
                sentenceId = sentence.getId();
            }
            mSentenceDao.link(sentenceId, wordId);
        }
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
                    "RANDOM{}",null,null,String.valueOf(newLimit));
            words.addAll(newWords);

        }
        assert params.getLimit() <=0 || words.size() == params.getLimit();
        for (Word word : words) {
            getAndSetTranslations(word, mDb);
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

    public List<Category> getCategoriesByLanguage(long languageId) {

        String where = CategoriesTable.CategoriesColumns.LANGUAGE_FK + "=?";
        String[] whereArguments = new String[]{String.valueOf(languageId)};
        return mCategoryDao.getAll(true, CategoriesTable.getColumn(), where, whereArguments,
                null, null, CategoriesTable.CategoriesColumns.NAME, null);
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
            whereArgumentsList.add(String.valueOf(params.getLessonId()));
        }
        if (params.getDifficult() > 0) {
            //słówka muszą mieć podany lub wyższy stopień trudności
            whereBuilder.append(" AND " + WordsColumns.MASTER_LEVEL + ">=?");
            whereArgumentsList.add(String.valueOf(params.getDifficult()));
        }
        String where = whereBuilder.toString();
        String[] whereArguments = whereArgumentsList.toArray(new String[0]);
        int limit = params.getLimit();

        String order = WordsColumns.LESSON_FK + ", " + WordsColumns.DIFFICULT;

        List<Word> words = mWordDao.getAllWithJoins(true, where, whereArguments, null, null, order, String.valueOf(limit));
        for (Word word : words) {
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

    public int getRepetitionsCount(long setId, int repetitionMonth, int repetitionDay){
        String where = new WordSelectionBuilder().append(WordSelectionBuilder.Parts.SET_PART)
                .append(WordSelectionBuilder.Parts.AND).append(WordSelectionBuilder.Parts.REPETITION_PART)
                .toString();
        String[] whereArguments = {String.valueOf(setId), String.valueOf(repetitionMonth), String.valueOf(repetitionDay)};
        int count = new WordDao(mDb).getCount(where, whereArguments);
        return count;
    }

    public void release() {
        mDb.close();
    }

}
