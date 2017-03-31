package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;
import com.dyszlewskiR.edu.scientling.data.models.creators.DefinitionCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.TABLE_NAME;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class DefinitionDao extends BaseDao<Definition> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + DefinitionsTable.TABLE_NAME + "("
                    + DefinitionsColumns.CONTENT + ", " + DefinitionsColumns.TRANSLATION
                    + ") VALUES (?, ?)";

    public DefinitionDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = DefinitionsTable.getColumns();

    }

    @Override
    public long save(Definition entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(DefinitionsColumns.CONTENT_POSITION, entity.getContent());
        if (entity.getTranslation() != null) {
            mInsertStatement.bindString(DefinitionsColumns.TRANSLATION_POSITION, entity.getTranslation());

        } else {
            mInsertStatement.bindNull(DefinitionsColumns.TRANSLATION_POSITION);
        }
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Definition entity) {
        final ContentValues values = new ContentValues();
        values.put(DefinitionsColumns.CONTENT, entity.getContent());
        values.put(DefinitionsColumns.TRANSLATION, entity.getTranslation());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(DefinitionsTable.TABLE_NAME, values, getWhereStatement(), getWhereArguments(entity));
    }

    private String getWhereStatement() {
        return DefinitionsColumns.ID + "=?";
    }

    private String[] getWhereArguments(Definition entity) {
        return new String[]{String.valueOf(entity.getId())};
    }

    @Override
    public void delete(Definition entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, getWhereStatement(), getWhereArguments(entity));
        }
    }

    @Override
    public Definition get(long id) {
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(DefinitionsTable.TABLE_NAME, mTableColumns, getWhereStatement(), getWhereArguments(id),
                null, null, null, null);
        Definition definition = null;
        if (cursor.moveToFirst()) {
            DefinitionCreator definitionCreator = new DefinitionCreator();
            definition = definitionCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return definition;
    }

    private String[] getWhereArguments(long id) {
        return new String[]{String.valueOf(id)};
    }


    @Override
    public List<Definition> getAll(boolean distinct, String[] columns, String selection, String[] selectionArgs,
                                   String groupBy, String having, String orderBy, String limit) {
        List<Definition> definitionsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, DefinitionsTable.TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            Definition definition = null;
            DefinitionCreator definitionCreator = new DefinitionCreator();
            do {
                definition = definitionCreator.createFromCursor(cursor);
                if (definition != null) {
                    definitionsList.add(definition);
                }
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return definitionsList;
    }

    public long getIdByContentAndTranslation(String content, String translation){
        String where = DefinitionsColumns.CONTENT +"=?"
                + " AND " + DefinitionsColumns.TRANSLATION  + "=?";
        String[] whereArguments = {content, translation};
        Cursor cursor = mDb.query(false, DefinitionsTable.TABLE_NAME,new String[]{DefinitionsColumns.ID}, where, whereArguments,
                null,null,null,"1");
        if(cursor.moveToFirst()){
            return cursor.getLong(0);
        }
        return -1;
    }
}
