package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Exercise;
import com.dyszlewskiR.edu.scientling.utils.ResourcesFileOpener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;
/**
 * Created by Razjelll on 08.11.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk= 17)
public class ExerciseDaoTest {
    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper = new DatabaseHelper(mContext);

    private ExerciseDao mDao;

    private Exercise mExercise1;
    private Exercise mExercise2;

    public ExerciseDaoTest()
    {
        mExercise1 = new Exercise();
        mExercise1.setId(1);
        mExercise1.setName("Dyktando");

        mExercise2 = new Exercise();
        mExercise2.setId(2);
        mExercise2.setName("Wybór tłumaczenia");

        mDbHelper.setFileOpener(new ResourcesFileOpener());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDao = new ExerciseDao(db);
    }

    @Test
    public void testInsertExercise()
    {
        long id = mDao.save(mExercise1);
        assertEquals(1,id);
        id = mDao.save(mExercise2);
        assertEquals(2, id);

    }

    @Test
    public void testGetExercise()
    {
        //TODO uzupełnić testy
    }
}
