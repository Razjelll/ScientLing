package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class LanguagesTable {
    public static final String TABLE_NAME = "Languages";
    public static class LanguagesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String ABBREVIATION = "abbreviation";
        public static final String CODE = "code";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION =1;
        public static final int ABBREVIATION_POSITION = 2;
        public static final int CODE_POSITION = 3;

        public static final int COLUMNS_COUNT = 4;
    }

    public static String[] getColumns()
    {
        String[] columns = new String[LanguagesColumns.COLUMNS_COUNT];
        columns[LanguagesColumns.ID_POSITION] = LanguagesColumns.ID;
        columns[LanguagesColumns.NAME_POSITION] = LanguagesColumns.NAME;
        columns[LanguagesColumns.ABBREVIATION_POSITION] = LanguagesColumns.ABBREVIATION;
        columns[LanguagesColumns.CODE_POSITION] = LanguagesColumns.CODE;

        return columns;
    }

}
