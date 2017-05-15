package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class SetsTable {
    public static final String TABLE_NAME = "Sets";
    public static final String ALIAS = "SE";
    public static final String ALIAS_DOT = ALIAS + ".";

    public static String[] getColumns() {
        String[] columns = new String[SetsColumns.COLUMNS_COUNT];
        columns[SetsColumns.ID_POSITION] = SetsColumns.ID;
        columns[SetsColumns.NAME_POSITION] = SetsColumns.NAME;
        columns[SetsColumns.LANGUAGE_L2_FK_POSITION] = SetsColumns.LANGUAGE_L2_FK;
        columns[SetsColumns.LANGUAGE_L1_FK_POSITION] = SetsColumns.LANGUAGE_L1_FK;
        columns[SetsColumns.CATALOG_POSITION] = SetsColumns.CATALOG;
        columns[SetsColumns.GLOBAL_ID_POSITION] = SetsColumns.GLOBAL_ID;
        columns[SetsColumns.UPLOADED_POSITION] = SetsColumns.UPLOADED;
        columns[SetsColumns.IMAGES_DOWNLOADED_POSITION] = SetsColumns.IMAGES_DOWNLOADED;
        columns[SetsColumns.RECORDS_DOWNLOADED_POSITION] = SetsColumns.RECORDS_DOWNLOADED;
        return columns;
    }

    public static class SetsColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LANGUAGE_L2_FK = "language_l2_fk";
        public static final String LANGUAGE_L1_FK = "language_l1_fk";
        public static final String CATALOG = "catalog";
        public static final String GLOBAL_ID = "global_id";
        public static final String UPLOADED = "uploaded";
        public static final String IMAGES_DOWNLOADED = "images_downloaded";
        public static final String RECORDS_DOWNLOADED = "records_downloaded";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;
        public static final int LANGUAGE_L2_FK_POSITION = 2;
        public static final int LANGUAGE_L1_FK_POSITION = 3;
        public static final int CATALOG_POSITION = 4;
        public static final int GLOBAL_ID_POSITION = 5;
        public static final int UPLOADED_POSITION = 6;
        public static final int IMAGES_DOWNLOADED_POSITION = 7;
        public static final int RECORDS_DOWNLOADED_POSITION = 8;

        public static final int COLUMNS_COUNT =9 ;
    }
}
