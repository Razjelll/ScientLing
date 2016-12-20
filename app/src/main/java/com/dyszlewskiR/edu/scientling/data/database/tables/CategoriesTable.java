package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class CategoriesTable {
    public static final String TABLE_NAME = "Categories";
    public static final String ALIAS = "C";
    public static final String ALIAS_DOT = ALIAS + ".";

    public static String[] getColumn() {
        String[] columns = new String[CategoriesColumns.COLUMNS_COUNT];
        columns[CategoriesColumns.ID_POSITION] = CategoriesColumns.ID;
        columns[CategoriesColumns.NAME_POSITION] = CategoriesColumns.NAME;
        columns[CategoriesColumns.LANGUAGE_FK_POSITION] = CategoriesColumns.LANGUAGE_FK;
        return columns;
    }

    public static class CategoriesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LANGUAGE_FK = "language_fk";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;
        public static final int LANGUAGE_FK_POSITION = 2;

        public static int COLUMNS_COUNT = 3;
    }
}
