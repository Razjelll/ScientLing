package com.wingedrabbits.edu.scientling.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.BuildConfig;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertTrue;
/**
 * Created by Razjelll on 06.11.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class DatabaseHelperTest {


    private static Context context = RuntimeEnvironment.application;
    private static DatabaseHelper dbHelper = new DatabaseHelper(context);



    @Test
    public void testCreateDb()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
    }

    @Test
    public void testCloseDb()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();
        assertTrue(!db.isOpen());
    }

    @Test
    public void testDeleteDb()
    {
        Context con = RuntimeEnvironment.application;
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.close();
        helper.close();
        assertTrue(con.deleteDatabase(DatabaseHelper.DB_NAME));
    }
}
