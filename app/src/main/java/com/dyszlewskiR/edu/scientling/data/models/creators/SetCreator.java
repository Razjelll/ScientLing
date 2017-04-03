package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.*;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Language;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;


/**
 * Created by Razjelll on 31.03.2017.
 */

public class SetCreator implements IModelCreator<VocabularySet>{

    @Override
    public VocabularySet createFromCursor(Cursor cursor) {
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
}
