package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 07.11.2016.
 */

public class CategoryDao extends BaseDao<Category> {

    private static final String TABLE_NAME = "Categories";
    public static class CategoriesColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LANGUAGE_FK = "language_fk";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;
        public static final int LANGUAGE_FK_POSITION = 2;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            +CategoriesColumns.NAME + ", " + CategoriesColumns.LANGUAGE_FK
            +") VALUES (?,?)";

    private SQLiteDatabase mDB;
    private SQLiteStatement mInsertStatement;
    private String[] mTableColumns;

    public CategoryDao(SQLiteDatabase db)
    {
        super();
        mDB = db;
        mInsertStatement = mDB.compileStatement(INSERT_STATEMENT);

        setTableColumns();
    }

    private void setTableColumns()
    {
        mTableColumns = new String[3];
        mTableColumns[CategoriesColumns.ID_POSITION] = CategoriesColumns.ID;
        mTableColumns[CategoriesColumns.NAME_POSITION] = CategoriesColumns.NAME;
        mTableColumns[CategoriesColumns.LANGUAGE_FK_POSITION] = CategoriesColumns.LANGUAGE_FK;
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
        mDB.update(TABLE_NAME, values, where, whereAttributes);
    }

    @Override
    public void delete(Category entity) {
        long id = entity.getId();
        if(id>0)
        {
            String where = CategoriesColumns.ID + " =?";
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDB.delete(TABLE_NAME, where, whereArguments);
        }
    }

    @Override
    public Category get(long id)
    {
        Category category = null;
        String where = CategoriesColumns.ID + " =?";
        String[] whereAttributes = new String[] {String.valueOf(id)};
        Cursor cursor = mDB.query(TABLE_NAME, mTableColumns, where, whereAttributes,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            category = this.buildCategoryFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return category;
    }

    private Category buildCategoryFromCursor(Cursor cursor)
    {
        Category category = null;
        if(cursor != null)
        {
            category = new Category();
            category.setId(cursor.getLong(CategoriesColumns.ID_POSITION));
            category.setName(cursor.getString(CategoriesColumns.NAME_POSITION));
        }
        return category;
    }

    @Override
    public List<Category> getAll() {

        List<Category> categoriesList = new ArrayList<>();
        Cursor cursor = mDB.query(TABLE_NAME, mTableColumns, null,null,
                mGroupBy,mHaving, mGroupBy, mLimit);// TODO zobaczyć co oznacza nazwa
        if(cursor.moveToFirst())
        {
            do{
                Category category = buildCategoryFromCursor(cursor);
                if(category != null)
                {
                    categoriesList.add(category);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return categoriesList;

    }
}
