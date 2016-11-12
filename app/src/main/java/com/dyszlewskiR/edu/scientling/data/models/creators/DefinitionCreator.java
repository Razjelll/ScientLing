package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable;
import com.dyszlewskiR.edu.scientling.data.models.Definition;

import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.*;
/**
 * Created by Razjelll on 12.11.2016.
 */

public class DefinitionCreator implements IModelCreator<Definition>{

    @Override
    public Definition createFromCursor(Cursor cursor) {
        Definition definition = null;
        if (cursor != null) {
            definition = new Definition();
            definition.setId(cursor.getLong(DefinitionsColumns.ID_POSITION));
            definition.setDefinition(cursor.getString(DefinitionsColumns.DEFINITION_POSITION));
            definition.setTranslation(cursor.getString(DefinitionsColumns.TRANSLATION_POSITION));
        }
        return definition;
    }
}
