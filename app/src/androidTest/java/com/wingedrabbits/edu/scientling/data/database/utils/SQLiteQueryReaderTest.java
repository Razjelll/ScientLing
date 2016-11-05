package com.wingedrabbits.edu.scientling.data.database.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Razjelll on 04.11.2016.
 */

@RunWith(AndroidJUnit4.class)
public class SQLiteQueryReaderTest {

    private Context mContext;
    private SQLiteQueryReader reader;

    private final String correctQuery1 = "CREATE TABLE Languages ( "+
            "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "name TEXT NOT NULL, "+
            "abbreviation TEXT CHECK (length(abreviation)<=4), "+
            "code TEXT CHECK(length(code) = 5) UNIQUE "+
            ");";

    private final String correctQuery2 = "ALTER TABLE Languages;";

    private final String correctQuery3 = "DROP TABLE Languages;";
    @Before
    public void initialize()
    {
        mContext = InstrumentationRegistry.getTargetContext();
        reader = new SQLiteQueryReader();
    }

    @Test
    public void read_isCorrect() throws IOException {
        assertNotNull(reader);

        InputStream inputStream = mContext.getAssets().open("testSqlQuery.sql");
        ArrayList<String> sqlQueries = reader.readFromStream(inputStream);
        assertEquals(sqlQueries.get(0), correctQuery1);
        assertEquals(sqlQueries.get(1), correctQuery2);
        assertEquals(sqlQueries.get(2), correctQuery3);
    }

    @Test
    public void read_EmptyStream() throws IOException {
        SQLiteQueryReader reader = new SQLiteQueryReader();
        ArrayList<String> sqlQueries = reader.readFromStream(null);
        assertEquals(sqlQueries.size(), 0);
    }


}
