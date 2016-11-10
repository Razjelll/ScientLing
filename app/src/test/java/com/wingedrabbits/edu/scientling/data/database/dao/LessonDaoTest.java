package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.BuildConfig;
import com.wingedrabbits.edu.scientling.data.database.DatabaseHelper;
import com.wingedrabbits.edu.scientling.models.Lesson;
import com.wingedrabbits.edu.scientling.utils.ResourcesFileOpener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Razjelll on 09.11.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class LessonDaoTest {
    private Context mContext = RuntimeEnvironment.application;
    private DatabaseHelper mDbHelper = new DatabaseHelper(mContext);

    private LessonDao mDao;

    private Lesson mLesson1;
    private Lesson mLesson2;

    public LessonDaoTest()
    {
        mLesson1 = new Lesson();
        mLesson1.setId(1);
        mLesson1.setName("Ubrania 1");
        mLesson1.setNumber(1);
        //TODO zastanowić się czy Lesson będzie zawierał id do Setu czy Set będzie zawierał liste Lessonów

        mDbHelper.setFileOpener(new ResourcesFileOpener());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDao = new LessonDao(db);
    }

    @Test
    public void testInsertLesson()
    {

    }
}
