package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.models.Translation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

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

    private static final String CONTENT = "c";

    public static Translation createFromJson(JsonNode node){
        Translation translation = new Translation();
        translation.setContent(node.path(CONTENT).asText());

        return translation;
    }

    @JsonCreator
    public Translation createFromJson(@JsonProperty(CONTENT) String content){
        Translation translation = new Translation();
        translation.setContent(content);
        return translation;
    }
}


