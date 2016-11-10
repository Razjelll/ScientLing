package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.models.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class DefinitionDao extends BaseDao<Definition> {
    public static final String TABLE_NAME = "Definitions";

    public static class DefinitionsColumns {
        public static final String ID = "id";
        public static final String DEFINITION = "definition";
        public static final String TRANSLATION = "translation";

        public static final int ID_POSITION = 0;
        public static final int DEFINITION_POSITION = 1;
        public static final int TRANSLATION_POSITION = 2;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
                    + DefinitionsColumns.DEFINITION + ", " + DefinitionsColumns.TRANSLATION
                    + ") VALUES (?, ?)";
    private final String WHERE_ID = DefinitionsColumns.ID + "= ?";

    public DefinitionDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = new String[3];
        mTableColumns[DefinitionsColumns.ID_POSITION] = DefinitionsColumns.ID;
        mTableColumns[DefinitionsColumns.DEFINITION_POSITION] = DefinitionsColumns.DEFINITION;
        mTableColumns[DefinitionsColumns.TRANSLATION_POSITION] = DefinitionsColumns.TRANSLATION;
    }

    @Override
    public long save(Definition entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(DefinitionsColumns.DEFINITION_POSITION, entity.getDefinition());
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
        values.put(DefinitionsColumns.DEFINITION, entity.getDefinition());
        values.put(DefinitionsColumns.TRANSLATION, entity.getTranslation());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Definition entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }

    }

    @Override
    public Definition get(long id) {
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        Definition definition = null;
        if (cursor.moveToFirst()) {
            definition = buildDefinitionFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return definition;
    }

    private Definition buildDefinitionFromCursor(Cursor cursor) {
        Definition definition = null;
        if (cursor != null) {
            definition = new Definition();
            definition.setId(cursor.getLong(DefinitionsColumns.ID_POSITION));
            definition.setDefinition(cursor.getString(DefinitionsColumns.DEFINITION_POSITION));
            definition.setTranslation(cursor.getString(DefinitionsColumns.TRANSLATION_POSITION));
        }
        return definition;
    }

    @Override
    public List<Definition> getAll() {
        List<Definition> definitionsList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, mSelection, mSelectionArgs,
                mGroupBy, mHaving, mOrderBy, mLimit);
        if (cursor.moveToFirst()) {
            Definition definition = null;
            do {
                definition = buildDefinitionFromCursor(cursor);
                if (definition != null) {
                    definitionsList.add(definition);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        assert cursor.isClosed();

        return definitionsList;
    }
}
