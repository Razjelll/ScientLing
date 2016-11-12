package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.models.Tip;
import com.dyszlewskiR.edu.scientling.utils.ResourcesFileOpener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Razjelll on 08.11.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class TipDaoTest {

    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper = new DatabaseHelper(mContext);

    private TipDao mDao;

    private Tip mTip1;
    private Tip mTip2;

    public TipDaoTest()
    {
        mTip1 = new Tip();
        mTip1.setId(1);
        mTip1.setContent("Odmiana 'to be' I am you are...");

        mTip2 = new Tip();
        mTip2.setId(2);
        mTip2.setContent(""); //TODO coś tutaj wpisać

        mDbHelper.setFileOpener(new ResourcesFileOpener());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDao = new TipDao(db);
    }

    @Test
    public void testInsertTip()
    {
        long id = mDao.save(mTip1);
        assertEquals(1,id);
        id = mDao.save(mTip2);
        assertEquals(2, id);
        try
        {
            id = mDao.save(mTip1);
            fail("Złamano ograniczenie unikalnej wartości");
        } catch(SQLiteConstraintException ex )
        {
            assertTrue(true);
        }
    }

    @Test
    public void testGetTip()
    {
        mDao.save(mTip1);
        Tip tip = mDao.get(1);
        assertEquals(1, tip.getId());
        assertEquals("Odmiana 'to be' I am you are...", tip.getContent());
    }

    @Test
    public void testDeleteTip()
    {
        long id = mDao.save(mTip1);
        Tip tip = mDao.get(id);
        mDao.delete(tip);
        tip = mDao.get(id);
        assertNull(tip);
    }

    @Test
    public void testGetAllTips()
    {
        mDao.save(mTip1);
        mDao.save(mTip2);
        ArrayList<Tip> tipsList = (ArrayList<Tip>)mDao.getAll();
        assertEquals(2, tipsList.size());
        assertTrue(tipsList.get(0).equals(mTip1));
        assertTrue(tipsList.get(1).equals(mTip2));
    }

    @Test
    public void testUpdateTip()
    {
        long id = mDao.save(mTip1);
        Tip tip = mDao.get(id);
        tip.setContent("Brak");
        mDao.update(tip);
        tip = mDao.get(id);
        assertEquals("Brak", tip.getContent());
    }

}
