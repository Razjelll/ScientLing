package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.BuildConfig;
import com.wingedrabbits.edu.scientling.data.database.DatabaseHelper;
import com.wingedrabbits.edu.scientling.models.Category;
import com.wingedrabbits.edu.scientling.models.Language;
import com.wingedrabbits.edu.scientling.models.Lesson;
import com.wingedrabbits.edu.scientling.models.PartOfSpeech;
import com.wingedrabbits.edu.scientling.models.VocabularySet;
import com.wingedrabbits.edu.scientling.models.Word;
import com.wingedrabbits.edu.scientling.utils.ResourcesFileOpener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Razjelll on 09.11.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk =17)
public class WordDaoTest {
    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper = new DatabaseHelper(mContext);

    private WordDao mDao;
    private LessonDao mLessonDao;
    private PartOfSpeechDao mPartOfSpeechDao;
    private CategoryDao mCategoryDao;
    private LanguageDao mLanguageDao;


    private Word mWord1;
    private Word mWord2;

    public WordDaoTest()
    {
        mWord1 = new Word();
        mWord1.setId(1);
        mWord1.setWord("dog");
        mWord1.setTranscription("[ dɔːɡ ]");
        mWord1.setLessonId(1); //TODO może bedzie trzeba zmienić na setLesson

        mWord1.setDifficult((byte)3);
        mWord1.setMasterLevel((byte)-1);
        mWord1.setSelected(false);

        mWord2 = new Word();
        mWord2.setId(2);
        mWord2.setWord("Rabbit");
        mWord2.setLessonId(1);
        mWord2.setDifficult((byte)1);
        mWord2.setMasterLevel((byte)-1);
        mWord1.setSelected(false);

        mDbHelper.setFileOpener(new ResourcesFileOpener());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDao = new WordDao(db);
        mLessonDao = new LessonDao(db);
        mPartOfSpeechDao = new PartOfSpeechDao(db);
        mCategoryDao = new CategoryDao(db);
        mLanguageDao = new LanguageDao(db);
    }

    @Test
    public void testInsertWord()
    {
        long id = mDao.save(mWord1);
        assertEquals(1, id);
        //TODO uzupełnić testy
    }

    @Test
    public void testGetWord()
    {
        long id = mDao.save(mWord1);
        Word word = mDao.get(id);
        assertEquals(id, word.getId());
        assertEquals("dog", word.getWord());
    }

    @Test
    public void testDeleteWord()
    {
        long id = mDao.save(mWord1);
        Word word = mDao.get(id);
        mDao.delete(word);
        word = mDao.get(id);
        assertNull(word);
    }

    @Test
    public void testGetAllWords()
    {
        mDao.save(mWord1);
        mDao.save(mWord2);
        List<Word> wordsList = (ArrayList<Word>) mDao.getAll();
        assertEquals(2, wordsList.size());
    }

    @Test
    public void testUpdateWord()
    {
        //Wstawianie języka
        Language language = new Language();
        language.setName("Angielski");
        language.setAbbreviation("ENG");
        language.setCode("en_EN");
        mLanguageDao.save(language);

        //Wstawianie lekcji
        Lesson lesson = new Lesson();
        lesson.setName("Zwierzęta");
        lesson.setNumber(1);
        lesson.setSet(new VocabularySet());
        mLessonDao.save(lesson);
        //Wstawianie części mowy
        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.setName("Rzeczownik");
        mPartOfSpeechDao.save(partOfSpeech);

        //Wstawianie kategorii
        Category category = new Category();
        category.setName("Zwierzęta");
        category.setLanguage(language);
        mCategoryDao.save(category);

        // Dodawanie powiązania z PartsOfSpeech i Categories
        mWord1.setPartsOfSpeech(partOfSpeech);
        mWord1.setCategory(category);


        long id = mDao.save(mWord1);
        Word word = mDao.get(id);
        word.setSelected(true);
        mDao.update(word);
        word = mDao.get(id);
        assertTrue(word.isSelected());
        assertEquals("Zwierzęta", word.getCategory().getName());
    }




}
