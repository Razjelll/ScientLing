package com.wingedrabbits.edu.scientling.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Klasa umożliwiająca tworznie i aktualizaowanie tabeli z kategoriami słówek
 */

public class CategoryTable {

    public static final String TABLE_NAME = "Categories";

    public static class CategoriesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static void onCreate(SQLiteDatabase db)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + "(");
        sb.append(CategoriesColumns.ID + " INTEGER PRIMARY KEY, ");
        sb.append(CategoriesColumns.NAME + " TEXT ");
        sb.append(")");

        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
