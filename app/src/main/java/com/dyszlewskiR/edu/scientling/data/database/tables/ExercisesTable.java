package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class ExercisesTable {
    public static final String TABLE_NAME = "Exercises";
    public static final String ALIAS = "E";
    public static final String ALIAS_DOT = ALIAS + ".";

    public static String[] getColumns() {
        String[] columns = new String[ExercisesColumns.COLUMNS_COUNT];
        columns[ExercisesColumns.ID_POSITION] = ExercisesColumns.ID;
        columns[ExercisesColumns.NAME_POSITION] = ExercisesColumns.NAME;

        return columns;
    }

    public static class ExercisesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;

        public static final int COLUMNS_COUNT = 2;
    }

}
