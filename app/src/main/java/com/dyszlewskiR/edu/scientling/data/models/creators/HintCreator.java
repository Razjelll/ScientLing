package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.HintsTable;
import com.dyszlewskiR.edu.scientling.data.models.Hint;

/**
 * Created by Razjelll on 22.12.2016.
 */

public class HintCreator  implements IModelCreator<Hint>{
    @Override
    public Hint createFromCursor(Cursor cursor) {
        Hint hint = null;
        if (cursor != null) {
            hint = new Hint();
            hint.setId(cursor.getLong(HintsTable.HintsColumns.ID_POSITION));
            hint.setContent(cursor.getString(HintsTable.HintsColumns.CONTENT_POSITION));
        }
        return hint;
    }
}
