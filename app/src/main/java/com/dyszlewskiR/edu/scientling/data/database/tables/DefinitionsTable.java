package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class DefinitionsTable {
    public static final String TABLE_NAME = "Definitions";

    public static class DefinitionsColumns {
        public static final String ID = "id";
        public static final String DEFINITION = "definition";
        public static final String TRANSLATION = "translation";

        public static final int ID_POSITION = 0;
        public static final int DEFINITION_POSITION = 1;
        public static final int TRANSLATION_POSITION = 2;

        public static final int COLUMNS_COUNT = 3;
    }

    public static String[] getColumns()
    {
        String[] columns = new String[DefinitionsColumns.COLUMNS_COUNT];
        columns[DefinitionsColumns.ID_POSITION] = DefinitionsColumns.ID;
        columns[DefinitionsColumns.DEFINITION_POSITION] = DefinitionsColumns.DEFINITION;
        columns[DefinitionsColumns.TRANSLATION_POSITION] = DefinitionsColumns.TRANSLATION;

        return columns;
    }
}

