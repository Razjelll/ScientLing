package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class TranslationsTable {
    public static final String TABLE_NAME = "Translations";
    public static class TranslationsColumns{
        public static final String ID = "id";
        public static final String TRANSLATION = "translation";

        public static final int ID_POSITION = 0;
        public static final int TRANSLATION_POSITION = 1;

        public static final int COLUMNS_COUNT = 2;
    }

    public static String[] getColumns()
    {
        String[] columns = new String[2];
        columns[TranslationsColumns.ID_POSITION] = TranslationsColumns.ID;
        columns[TranslationsColumns.TRANSLATION_POSITION] = TranslationsColumns.TRANSLATION;

        return columns;
    }
}
