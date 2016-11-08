package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.BuildConfig;
import com.wingedrabbits.edu.scientling.data.database.DatabaseHelper;
import com.wingedrabbits.edu.scientling.models.Translation;
import com.wingedrabbits.edu.scientling.utils.ResourcesFileOpener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Razjelll on 07.11.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class TranslationDaoTest {

    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper = new DatabaseHelper(mContext);

    private TranslationDao mDao;
    private Translation mTranslation1;
    private Translation mTranslation2;

    public TranslationDaoTest()
    {
        mTranslation1 = new Translation();
        mTranslation1.setId(1);
        mTranslation1.setTranslation("Pies");

        mTranslation2 = new Translation();
        mTranslation2.setId(2);
        mTranslation2.setTranslation("Kaczka");

        mDbHelper.setFileOpener(new ResourcesFileOpener());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDao = new TranslationDao(db);

    }

    @Test
    public void testInsertTranslation()
    {
        long id = mDao.save(mTranslation1);
        assertEquals(1, id);
        id = mDao.save(mTranslation2);
        assertEquals(2,id);
    }

    @Test
    public void testGetTranslation()
    {
        mDao.save(mTranslation1);
        Translation translation = mDao.get(1);
        assertEquals(mTranslation1.getId(), translation.getId());
        assertEquals(mTranslation1.getTranslation(), translation.getTranslation());
    }

    @Test
    public void testDeleteTranslation()
    {
        long id = mDao.save(mTranslation1);
        Translation translation = mDao.get(id);
        mDao.delete(translation);
        translation = mDao.get(id);
        assertNull(translation);
    }

    @Test
    public void testGetAllTranslation()
    {
        mDao.save(mTranslation1);
        mDao.save(mTranslation2);
        ArrayList<Translation> translationList = (ArrayList<Translation>)mDao.getAll();
        assertEquals(2, translationList.size());
    }

    @Test
    public void testUpdateTranslation()
    {
        long id = mDao.save(mTranslation1);
        Translation translation = mDao.get(id);
        translation.setTranslation("Ptak");
        mDao.update(translation);
        translation = mDao.get(id);
        assertEquals("Ptak", translation.getTranslation());
    }
}
