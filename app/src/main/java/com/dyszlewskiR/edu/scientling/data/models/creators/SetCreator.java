package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.simple.JSONObject;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.SetsColumns;


/**
 * Created by Razjelll on 31.03.2017.
 */

public class SetCreator  {

    public static VocabularySet createFromCursor(Cursor cursor) {
        VocabularySet set = null;
        if (cursor != null) {
            set = new VocabularySet();
            set.setId(cursor.getLong(SetsColumns.ID_POSITION));
            set.setName(cursor.getString(SetsColumns.NAME_POSITION));
            long languageL2Id = cursor.getLong(SetsColumns.LANGUAGE_L2_FK_POSITION);
            if (languageL2Id > 0) {
                Language language = new Language();
                language.setId(languageL2Id);
                set.setLanguageL2(language);
            }
            long languageL1Id = cursor.getLong(SetsColumns.LANGUAGE_L1_FK_POSITION);
            if (languageL1Id > 0) {
                Language language = new Language();
                language.setId(languageL1Id);
                set.setLanguageL1(language);
            }
            set.setCatalog(cursor.getString(SetsColumns.CATALOG_POSITION));
        }
        return set;
    }

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String L1 = "l1";
    private static final String L2 = "l2";

    public static VocabularySet createFromJson(JsonNode object) throws JSONException {
        long id = object.path(ID).asLong();
        String name = object.path(NAME).asText();
        long l1 = object.path(L1).asLong();
        long l2 = object.path(L2).asLong();

        VocabularySet set = new VocabularySet();
        set.setName(name);
        set.setLanguageL1(new Language(l1));
        set.setLanguageL2(new Language(l2));

        return set;
    }
}
