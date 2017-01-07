package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;

import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class DefinitionCreator implements IModelCreator<Definition> {

    @Override
    public Definition createFromCursor(Cursor cursor) {
        Definition definition = null;
        if (cursor != null) {
            definition = new Definition();
            definition.setId(cursor.getLong(DefinitionsColumns.ID_POSITION));
            definition.setContent(cursor.getString(DefinitionsColumns.CONTENT_POSITION));
            definition.setTranslation(cursor.getString(DefinitionsColumns.TRANSLATION_POSITION));
        }
        return definition;
    }
}
