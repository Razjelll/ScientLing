package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.models.Translation;

import static com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable.TranslationsColumns.*;

/**
 * Created by Razjelll on 22.04.2017.
 */

public class TranslationCreator {

    public static Translation createFromCursor(Cursor cursor){
        Translation translation = null;
        if (cursor != null) {
            translation = new Translation();
            translation.setId(cursor.getLong(ID_POSITION));
            translation.setContent(cursor.getString(CONTENT_POSITION));
        }
        return translation;
    }
}
