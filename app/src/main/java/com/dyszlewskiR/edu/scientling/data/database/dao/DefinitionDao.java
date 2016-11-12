package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.creators.DefinitionCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.*;
/**
 * Created by Razjelll on 09.11.2016.
 */

public class DefinitionDao extends BaseDao<Definition> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + DefinitionsTable.TABLE_NAME + "("
                    + DefinitionsColumns.DEFINITION + ", " + DefinitionsColumns.TRANSLATION
                    + ") VALUES (?, ?)";
    private final String WHERE_ID = DefinitionsColumns.ID + "= ?";

    public DefinitionDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = DefinitionsTable.getColumns();

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
        mDb.update(DefinitionsTable.TABLE_NAME, values, WHERE_ID, whereArguments);
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
        Cursor cursor = mDb.query(DefinitionsTable.TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        Definition definition = null;
        if (cursor.moveToFirst()) {
            DefinitionCreator definitionCreator = new DefinitionCreator();
            definition = definitionCreator.createFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return definition;
    }

    @Override
    public List<Definition> getAll() {
        List<Definition> definitionsList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, DefinitionsTable.TABLE_NAME, mTableColumns, mSelection, mSelectionArgs,
                mGroupBy, mHaving, mOrderBy, mLimit);
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
        if (!cursor.isClosed()) {
            cursor.close();
        }
        assert cursor.isClosed();

        return definitionsList;
    }
}
