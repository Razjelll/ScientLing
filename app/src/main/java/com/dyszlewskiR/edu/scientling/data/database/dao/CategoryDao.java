package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.creators.CategoryCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable.*;

/**
 * Created by Razjelll on 07.11.2016.
 */

public class CategoryDao extends BaseDao<Category> {

    private final String INSERT_STATEMENT =
            "INSERT INTO " + CategoriesTable.TABLE_NAME + "("
                    + CategoriesColumns.NAME + ", " + CategoriesColumns.LANGUAGE_FK
                    + ") VALUES (?,?)";

    public CategoryDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = CategoriesTable.getColumn();
    }

    @Override
    public long save(Category entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getName());
        mInsertStatement.bindLong(2, entity.getLanguage().getId());

        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Category entity) {
        final ContentValues values = new ContentValues();
        values.put(CategoriesColumns.NAME, entity.getName());
        //TODO może język też dodać, przenieśc where do stałej
        String where = CategoriesColumns.ID + " =?";
        String[] whereAttributes = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, where, whereAttributes);
    }

    @Override
    public void delete(Category entity) {
        long id = entity.getId();
        if (id > 0) {
            String where = CategoriesColumns.ID + " =?";
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(CategoriesTable.TABLE_NAME, where, whereArguments);
        }
    }

    @Override
    public Category get(long id) {
        Category category = null;
        String where = CategoriesColumns.ID + " =?";
        String[] whereAttributes = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, where, whereAttributes,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            CategoryCreator categoryCreator = new CategoryCreator();
            category = categoryCreator.createFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return category;
    }

    @Override
    public List<Category> getAll(boolean distinct,String[] columns, String selection, String[] selectionArgs,
                                 String groupBy, String having, String orderBy, String limit) {

        List<Category> categoriesList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);// TODO zobaczyć co oznacza nazwa
        if (cursor.moveToFirst()) {
            CategoryCreator categoryCreator = new CategoryCreator();
            do {
                Category category = categoryCreator.createFromCursor(cursor);
                if (category != null) {
                    categoriesList.add(category);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return categoriesList;
    }
}
