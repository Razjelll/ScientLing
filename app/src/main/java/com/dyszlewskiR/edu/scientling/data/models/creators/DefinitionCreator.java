package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.Definition;

import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns.CONTENT;
import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns.ID;
import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns.TRANSLATION;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class DefinitionCreator implements IModelCreator<Definition> {

    @Override
    public Definition createFromCursor(Cursor cursor) {
        Definition definition = null;
        int columnsCount = cursor.getColumnCount();
        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            switch (cursor.getColumnName(columnIndex)) {
                case ID:
                    definition.setId(cursor.getLong(columnIndex));
                    break;
                case CONTENT:
                    definition.setContent(cursor.getString(columnIndex));
                    break;
                case TRANSLATION:
                    if (!cursor.isNull(columnIndex)) {
                        definition.setTranslation(cursor.getString(columnIndex));
                    }
                    break;
            }
        }
        return definition;
    }
}
