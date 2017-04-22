package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.Category;

import static com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable.CategoriesColumns.ID;
import static com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable.CategoriesColumns.NAME;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class CategoryCreator  {

    public static Category createFromCursor(Cursor cursor) {
        Category category = new Category();
        int columnsCount = cursor.getColumnCount();
        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            switch (cursor.getColumnName(columnIndex)) {
                case ID:
                    category.setId(cursor.getLong(columnIndex));
                    break;
                case NAME:
                    category.setName(cursor.getString(columnIndex));
                    break;
            }
        }
        return category;
    }
}
