package com.dyszlewskiR.edu.scientling.data.database;

import android.app.DownloadManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import com.almworks.sqlite4java.SQLite;
import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.utils.QueryReader;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.utils.ResourcesFileOpener;
import com.ibm.icu.impl.data.ResourceReader;

import org.apache.tools.ant.taskdefs.BUnzip2;
import org.apache.tools.ant.taskdefs.BuildNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Razjelll on 10.11.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class DataManagerTest {
    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper ;
    private DataManager mDataManager =  new DataManager(mContext);


    public DataManagerTest() throws IOException {
        // wprowadzanie przykładowych danych z pliku
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ResourcesFileOpener fileOpener = new ResourcesFileOpener();
        InputStream stream = fileOpener.getStream("testInsertStatements.sql");
        QueryReader reader = new QueryReader();
        ArrayList<String> statements = reader.readFromStream(stream);
        for(String s: statements)
        {
            db.execSQL(s);
        }

    }

    @Test
    public void testGetWord()
    {
        Word word = mDataManager.getWord(1);
        assertEquals(1, word.getId());
        assertNotNull(word.getTranslations());
        assertEquals("Pies",word.getTranslations().get(0).getTranslation());
        assertNotNull(word.getSentences());
    }

    @Test
    public void testSaveWord()
    {
        Word word = new Word();
        word.setWord("Królik");
        word.setTranscription("OJOJ");
        Definition definition = new Definition();
        definition.setDefinition("To jest derfinicja");
        definition.setTranslation("To jest tłumaczenie");
        word.setDefinition(definition);
        Translation translation1 = new Translation();
        translation1.setTranslation("Pies");
        Translation translation2 = new Translation();
        translation2.setTranslation("Czort");
        word.setTranslations(new ArrayList<Translation>(Arrays.asList(translation1, translation2)));
        Sentence sentence = new Sentence();
        sentence.setSentence("This is sample sentence");
        sentence.setTranslation("To jest przykładowe zdanie");
        word.setSentences(new ArrayList<Sentence>(Arrays.asList(sentence)));
        PartOfSpeech partOfSpeech = new PartOfSpeech(2);
        word.setPartsOfSpeech(partOfSpeech);
        word.setCategory(new Category(1));
        word.setLessonId(1);
        word.setDifficult((byte)5);
        word.setMasterLevel((byte)-1);
        word.setSelected(false);
        long id = mDataManager.saveWord(word);
        Word word2= mDataManager.getWord(id);
        assertTrue(id > 0);
        assertEquals(2, word2.getTranslations().size());
        assertEquals(1, word2.getSentences().size());
        assertEquals("Pies", word2.getTranslations().get(0).getTranslation());
        assertEquals(3, word2.getDefinition().getId());
        assertEquals("To jest derfinicja", word2.getDefinition().getDefinition());
        assertEquals("To jest tłumaczenie", word2.getDefinition().getTranslation());
        assertEquals(1,word2.getCategory().getId());
        assertEquals("Zwierzęta", word2.getCategory().getName());
        assertEquals(1, word2.getLessonId());
        assertEquals(3, word2.getSentences().get(0).getId());
        assertEquals("This is sample sentence", word.getSentences().get(0).getSentence());
        assertEquals("To jest przykładowe zdanie", word.getSentences().get(0).getTranslation());
    }

    @Test
    public void testGetQuestions()
    {
        ArrayList<Word> questions = mDataManager.getQuestions(-1, 1, 1, 3, 5);
        assertTrue(questions.size() != 0);
        assertEquals("lamp", questions.get(0).getWord());
        assertNotNull(questions.get(0).getTranslations());
        assertEquals("Lampa", questions.get(0).getTranslations().get(0).getTranslation() );
    }

}
