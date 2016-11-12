package com.dyszlewskiR.edu.scientling.data.database.tables;

/**
 * Created by Razjelll on 11.11.2016.
 */

public class WordsTranslationsTable {
    public static final String TABLE_NAME = "WordsTranslations";

    public static class WordsTranslationsColumns {
        public static final String WORD_FK = "word_fk";
        public static final String TRANSLATION_FK = "translation_fk";

        public static final int WORD_FK_POSITION = 0;
        public static final int TRANSLATION_FK_POSTIION = 1;
    }
}
