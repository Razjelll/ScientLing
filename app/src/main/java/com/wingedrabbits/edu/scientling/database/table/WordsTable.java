package com.wingedrabbits.edu.scientling.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Klasa służąca do tworzenia i aktualizacji tabeli ze słówkami
 */

public final class WordsTable {

    public static final String TABLE_NAME = "Words";

    /**
     * Klasa zawierająca nazwy kolumn tabeli Words
     */
    public static class WordsColumns {
        public static final String ID = "id";
        public static final String WORD = "word";
        public static final String PRONUNCIATION = "pronunciation";
        public static final String TYPE = "wordType";
        public static final String CATEGORY = "category";
        public static final String MASTER_LEVEL = "masterProcent";
        public static final String REPETITIONS = "repetitionsNumber";
    }

    public static class WordsColPositon {
        public static final int ID = 0;
        public static final int WORD = 1;
        public static final int PRONUNCIATION = 2;
        public static final int TYPE = 3;
        public static final int CATEGORY =4;
        public static final int MASTER_LEVEL = 5;
        public static final int REPETITIONS = 6;
    }

    /**
     * Metoda służąca do tworzenia tabeli Words
     * @param db baza danych
     */
    public static void onCreate(SQLiteDatabase db)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(TableHelper.CREATE + TABLE_NAME + "(");
        sb.append(WordsColumns.ID + TableHelper.INT+  TableHelper.PRIMARY_KEY+TableHelper.AUTOINCREMENT + ", ");
        sb.append(WordsColumns.WORD + TableHelper.TEXT + ", ");
        sb.append(WordsColumns.PRONUNCIATION + TableHelper.TEXT + ", ");
        sb.append(WordsColumns.TYPE + TableHelper.INT + ", "); //TODO FOREIGN KEY
        sb.append(WordsColumns.CATEGORY + TableHelper.INT + ", "); //TODO FOREIGN KEY
        sb.append(WordsColumns.MASTER_LEVEL + TableHelper.INT + ", "); //TODO CHECK 0-100
        sb.append(WordsColumns.REPETITIONS + TableHelper.INT + ", ");
        sb.append(TableHelper.FOREIGN_KEY + WordsColumns.TYPE + TableHelper.REFERENCES + TypesTable.TABLE_NAME + "(" + TypesTable.TypesColumns.ID+")");
        sb.append(TableHelper.FOREIGN_KEY + WordsColumns.CATEGORY+ TableHelper.REFERENCES + CategoryTable.TABLE_NAME + "(" + CategoryTable.CategoriesColumns.ID+")");

        sb.append(")");

        db.execSQL(sb.toString());
    }

    /**
     * Metoda służąca do akutalizowania tabeli Words
     * @param db baza danych
     * @param oldVersion numer starej wersji bazy danych
     * @param newVersion numer nowej wersji bazy danych
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
