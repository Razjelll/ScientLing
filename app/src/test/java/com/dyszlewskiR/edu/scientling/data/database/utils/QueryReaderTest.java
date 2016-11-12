package com.dyszlewskiR.edu.scientling.data.database.utils;

import android.content.Context;

import com.dyszlewskiR.edu.scientling.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Razjelll on 07.11.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class QueryReaderTest {

    private Context mContext;
    private QueryReader mReader;

    private final String correctString1 = "CREATE TABLE Languages (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " name TEXT NOT NULL," +
            " abbreviation TEXT CHECK (length(abbreviation)<=4) NOT NULL," +
            " code TEXT CHECK(length(code) = 5)NOT NULL UNIQUE" +
            ");";

    @Before
    public void setUp()
    {
        mContext = RuntimeEnvironment.application;
        mReader = new QueryReader();
    }

    @Test
    public void testReadQuery() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("createDb.sql");
        ArrayList<String> sqlQueries = mReader.readFromStream(inputStream);
        assertEquals(3, sqlQueries.size());
        assertEquals(correctString1, sqlQueries.get(0));
    }

}
