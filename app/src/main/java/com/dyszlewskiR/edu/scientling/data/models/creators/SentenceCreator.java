package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.SentencesTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SentencesTable.SentencesColumns.*;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class SentenceCreator implements IModelCreator<Sentence> {
    @Override
    public Sentence createFromCursor(Cursor cursor) {
        Sentence sentence = new Sentence();
        int columnsCount = cursor.getColumnCount();
        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            switch (cursor.getColumnName(columnIndex)){
                case ID:
                    sentence.setId(cursor.getLong(columnIndex)); break;
                case CONTENT:
                    sentence.setContent(cursor.getString(columnIndex)); break;
                case TRANSLATION:
                    sentence.setTranslation(cursor.getString(columnIndex)); break;
            }
        }
        return sentence;
    }
}
