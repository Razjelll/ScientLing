package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class ExampleSentences {
    public static final String TABLE_NAME = "ExampleSentences";
    public static final String ALIAS = "ES";
    public static final String ALIAS_DOT = ALIAS + ".";
    public static class ExampleSentencesColumns {
        public static final String WORD_FK = "word_fk";
        public static final String SENTENCE_FK = "sentence_fk";

        public static final int WORD_FK_POSITION = 0;
        public static final int SENTENCE_FK_POSITION = 1;

        public static final int COLUMNS_COUNT = 2;
    }

    public static final String[] getColumns()
    {
        String[] columns = new String[ExampleSentencesColumns.COLUMNS_COUNT];
        columns[ExampleSentencesColumns.WORD_FK_POSITION] = ExampleSentencesColumns.WORD_FK;
        columns[ExampleSentencesColumns.SENTENCE_FK_POSITION] = ExampleSentencesColumns.SENTENCE_FK;

        return columns;
    }
}
