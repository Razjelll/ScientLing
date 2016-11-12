package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class LessonsTable {
    public static final String TABLE_NAME = "Lessons";

    public static class LessonsColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String NUMBER = "number";
        public static final String SET_FK = "set_fk";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION =1;
        public static final int NUMBER_POSITION = 2;
        public static final int SET_FK_POSITION =3;

        public static final int COLUMNS_COUNT = 4;
    }

    public static String[] getColumns()
    {
        String[] columns = new String[LessonsColumns.COLUMNS_COUNT];
        columns[LessonsColumns.ID_POSITION] = LessonsColumns.ID;
        columns[LessonsColumns.NAME_POSITION] = LessonsColumns.NAME;
        columns[LessonsColumns.NUMBER_POSITION] = LessonsColumns.NUMBER;
        columns[LessonsColumns.SET_FK_POSITION] = LessonsColumns.SET_FK;

        return columns;
    }
}
