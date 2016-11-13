package com.dyszlewskiR.edu.scientling.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.dao.CategoryDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.DefinitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SentenceDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.TranslationDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.utils.ResourcesFileOpener;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.*;
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

    public DataManager(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);

        if (BuildConfig.DEBUG) //TODO sppawdzić dokładnie jak działa BuildConfig.DEBUG
        {
            dbHelper.setFileOpener(new ResourcesFileOpener());
        }

        mDb = dbHelper.getWritableDatabase();

        mWordDao = new WordDao(mDb);
        mTranslationDao = new TranslationDao(mDb);
        mSentenceDao = new SentenceDao(mDb);
        mDefinitionDao = new DefinitionDao(mDb);
        mCategoryDao = new CategoryDao(mDb);
    }


    public Word getWord(long id) {
        Word word = mWordDao.get(id);
        completeWord(word);
        return word;
    }

    private void completeWord(Word word)
    {
        if (word != null) {
            ArrayList<Translation> translations = mTranslationDao.getLinked(word.getId());
            if (translations != null) {
                word.setTranslations(translations);
            }
            ArrayList<Sentence> sentences = (ArrayList<Sentence>) mSentenceDao.getLinked(word.getId());
            if (sentences != null) {
                word.setSentences(sentences);
            }
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

        long wordId = mWordDao.save(word);
        saveTranslations(word.getTranslations(), wordId);
        saveSentences(word.getSentences(), wordId);

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
        Translation translation = null;
        for (Translation t : translationsList) {
            translation = mTranslationDao.getByContent(t.getTranslation());
            long translationId;
            if (t == null) {
                translationId = mTranslationDao.save(translation);
            } else {
                translationId = translation.getId();
            }
            mTranslationDao.link(translationId, wordId);
        }
    }

    public void saveSentences(ArrayList<Sentence> sentencesList, long wordId)
    {
        Sentence sentence = null;
        for(Sentence s : sentencesList)
        {
            sentence = mSentenceDao.getByContent(s.getSentence());
            long sentenceId;
            if(sentence == null)
            {
                sentenceId = mSentenceDao.save(s);
            } else {
                sentenceId = sentence.getId();
            }
            mSentenceDao.link(sentenceId, wordId);
        }
    }

    public ArrayList<Word> getQuestions(long set, long lesson, long category, long difficult, int howMuch)
    {
        assert lesson != -1 || set != -1;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> queryArguments = new ArrayList<>();
        stringBuilder.append(WordsTable.WordsColumns.LESSON_FK);
        if(lesson != -1) //jeśli ograniczamy się do jednej lekcji
        {
            stringBuilder.append(" = ").append("?");
            queryArguments.add(String.valueOf(lesson));
        }
        else
        {
            stringBuilder.append("IN (");
            stringBuilder.append(" SELECT ").append("L.").append(LessonsColumns.ID);
            stringBuilder.append(" FROM ").append(LessonsTable.TABLE_NAME).append(" L");
            stringBuilder.append(" JOIN").append(SetsTable.TABLE_NAME).append(" S");
            stringBuilder.append(" ON ").append("L.").append(LessonsColumns.SET_FK);
            stringBuilder.append(" = ").append("S.").append(SetsColumns.ID);
            stringBuilder.append(" WHERE ").append(SetsColumns.ID).append( " = ").append("?");
            stringBuilder.append(")");
            queryArguments.add(String.valueOf(set));
        }
        if(category != -1)
        {
            stringBuilder.append(" AND ").append(WordsColumns.CATEGORY_FK).append(" = ").append("?");
            queryArguments.add(String.valueOf(category));
        }
        if(difficult != -1)
        {
            stringBuilder.append(" AND ").append(WordsColumns.DIFFICULT).append(" = ").append("?");
            queryArguments.add(String.valueOf(difficult));
        }

        stringBuilder.append(" AND ").append(WordsColumns.MASTER_LEVEL).append("<0");

        String where = stringBuilder.toString();
        String[] whereArguments = queryArguments.toArray(new String[0]);
        String limit = String.valueOf(howMuch);

        List<Word> words  = mWordDao.getAll(false, WordsTable.getColumns(), where,whereArguments,
                null,null,null, limit);
        for(Word word: words)
        {
            completeWord(word);
        }

        return (ArrayList<Word>)words;
    }


}
