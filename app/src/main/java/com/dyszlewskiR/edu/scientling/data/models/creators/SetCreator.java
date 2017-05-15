package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.Set;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.SetsColumns;

public class SetCreator  {

    public static VocabularySet createFromCursor(Cursor cursor) {
        if(cursor != null){
            VocabularySet set = new VocabularySet();
            for(int i=0; i<cursor.getColumnCount(); i++){
                switch (cursor.getColumnName(i)){
                    case SetsColumns.ID:
                        set.setId(cursor.getLong(i)); break;
                    case SetsColumns.NAME:
                        set.setName(cursor.getString(i)); break;
                    case SetsColumns.LANGUAGE_L2_FK:
                        if(!cursor.isNull(i)){
                            long languageL2 = cursor.getLong(i);
                            if(languageL2 > 0){
                                set.setLanguageL2(new Language(languageL2));
                            }
                        }
                        break;
                    case SetsColumns.LANGUAGE_L1_FK:
                        if(!cursor.isNull(i)){
                            long languageL1 = cursor.getLong(i);
                            if(languageL1 > 0){
                                set.setLanguageL1(new Language(languageL1));
                            }
                        }
                        break;
                    case SetsColumns.CATALOG:
                        set.setCatalog(cursor.getString(i)); break;
                    case SetsColumns.GLOBAL_ID:
                        if(!cursor.isNull(i)){
                            set.setGlobalId(cursor.getLong(i));
                        }
                        break;
                    case SetsColumns.UPLOADED:
                        if(!cursor.isNull(i)){
                            set.setUploaded(cursor.getInt(i));
                        }
                        break;
                    case SetsColumns.IMAGES_DOWNLOADED:
                        if(!cursor.isNull(i)){
                            set.setImagesDownloaded(cursor.getInt(i));
                        }
                        break;
                    case SetsColumns.RECORDS_DOWNLOADED:
                        if(!cursor.isNull(i)){
                            set.setRecordsDownloaded(cursor.getInt(i));
                        }
                }
            }
            return set;
        }
        return null;
    }

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String L1 = "l1";
    private static final String L2 = "l2";

    public static VocabularySet createFromJson(JsonNode object) throws JSONException {
        VocabularySet set = new VocabularySet();
        set.setGlobalId(object.path(ID).asLong());
        set.setName(object.path(NAME).asText());
        set.setLanguageL1(new Language(object.path(L1).asLong()));
        set.setLanguageL2(new Language(object.path(L2).asLong()));

        return set;
    }
}
