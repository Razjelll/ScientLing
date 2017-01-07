package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;

/**
 * Created by Razjelll on 12.11.2016.
 */

public class CategoryCreator implements IModelCreator<Category> {
    @Override
    public Category createFromCursor(Cursor cursor) {
        Category category = null;
        if (cursor != null) {
            category = new Category();
            category.setId(cursor.getLong(CategoriesTable.CategoriesColumns.ID_POSITION));
            category.setName(cursor.getString(CategoriesTable.CategoriesColumns.NAME_POSITION));
        }
        return category;
    }
}
