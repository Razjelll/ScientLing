package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.SentencesTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class SentenceCreator implements IModelCreator<Sentence> {
    @Override
    public Sentence createFromCursor(Cursor cursor) {
        Sentence sentence = null;
        if (cursor != null) {
            sentence = new Sentence();
            sentence.setId(cursor.getLong(SentencesTable.SentencesColumns.ID_POSITION));
            sentence.setContent(cursor.getString(SentencesTable.SentencesColumns.CONTENT_POSITION));
            sentence.setTranslation(cursor.getString(SentencesTable.SentencesColumns.TRANSLATION_POSITION));
        }
        return sentence;
    }
}
