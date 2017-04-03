package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetCreator;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Language;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.SetsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.TABLE_NAME;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class SetDao extends BaseDao<VocabularySet> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + SetsTable.TABLE_NAME + "("
                    + SetsColumns.NAME + ", " + SetsColumns.LANGUAGE_L2_FK
                    + ", " + SetsColumns.LANGUAGE_L1_FK
                    + ", " + SetsColumns.CATALOG + ") VALUES (?,?,?,?)";
    private final String WHERE_ID = SetsColumns.ID + "= ?";

    public SetDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = SetsTable.getColumns();
    }

    @Override
    public long save(VocabularySet entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(SetsColumns.NAME_POSITION, entity.getName());
        mInsertStatement.bindLong(SetsColumns.LANGUAGE_L2_FK_POSITION, entity.getLanguageL2().getId());
        mInsertStatement.bindLong(SetsColumns.LANGUAGE_L1_FK_POSITION, entity.getLanguageL1().getId());
        mInsertStatement.bindString(SetsColumns.CATALOG_POSITION, entity.getCatalog());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(VocabularySet entity) {
        final ContentValues values = new ContentValues();
        values.put(SetsColumns.NAME, entity.getName());
        if (entity.getLanguageL2() != null) {
            values.put(SetsColumns.LANGUAGE_L2_FK, entity.getLanguageL2().getId());
        } else {
            values.putNull(SetsColumns.LANGUAGE_L2_FK);
        }
        if (entity.getLanguageL1() != null) {
            values.put(SetsColumns.LANGUAGE_L1_FK, entity.getLanguageL1().getId());
        } else {
            values.putNull(SetsColumns.LANGUAGE_L1_FK);
        }
        values.put(SetsColumns.CATALOG, entity.getCatalog());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);

    }

    @Override
    public void delete(VocabularySet entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public VocabularySet get(long id) {
        VocabularySet set = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            SetCreator creator = new SetCreator();
            set = creator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return set;
    }


    @Override
    public List<VocabularySet> getAll(boolean distinct, String[] columns, String selection, String[] selectionArgs,
                                      String groupBy, String having, String orderBy, String limit) {
        List<VocabularySet> setsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            VocabularySet set = null;
            SetCreator creator = new SetCreator();
            do {
                set = creator.createFromCursor(cursor);
                if (set != null) {
                    setsList.add(set);
                }
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return setsList;
    }
}
