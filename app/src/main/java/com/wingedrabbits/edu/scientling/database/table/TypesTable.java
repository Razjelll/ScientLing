package com.wingedrabbits.edu.scientling.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DisplayContext;

/**
 * Klasa zajmująca się tworzeniem i aktualizowaniem tabeli Types.
 * Tabela określa jaką częścią mowy jest słówko
 */

public class TypesTable {

    public static final String TABLE_NAME = "Types";

    public static class TypesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append(TableHelper.CREATE + TABLE_NAME + "( ");
        sb.append(TypesColumns.ID + TableHelper.INT + TableHelper.PRIMARY_KEY + ", ");
        sb.append(TypesColumns.NAME + TableHelper.TEXT);
        sb.append(")");

        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(TableHelper.DROP_IF+ TABLE_NAME);
        onCreate(db);
    }
}
