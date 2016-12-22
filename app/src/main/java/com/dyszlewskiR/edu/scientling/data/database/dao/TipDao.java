package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.models.Hint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class TipDao extends BaseDao<Hint> {

    private static final String TABLE_NAME = "Tips";
    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
                    + TipsColumns.CONTENT + ") VALUES (?)";
    private final String WHERE_ID = TipsColumns.ID + " = ?";

    public TipDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = new String[2];
        mTableColumns[TipsColumns.ID_POSITION] = TipsColumns.ID;
        mTableColumns[TipsColumns.CONTENT_POSITION] = TipsColumns.CONTENT;
    }

    @Override
    public long save(Hint entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getContent());

        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Hint entity) {
        final ContentValues values = new ContentValues();
        values.put(TipsColumns.CONTENT, entity.getContent());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Hint entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Hint get(long id) {
        Hint hint = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            hint = buildTipFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return hint;
    }

    private Hint buildTipFromCursor(Cursor cursor) {
        Hint hint = null;
        if (cursor != null) {
            hint = new Hint();
            hint.setId(cursor.getLong(TipsColumns.ID_POSITION));
            hint.setContent(cursor.getString(TipsColumns.CONTENT_POSITION));
        }
        return hint;
    }

    @Override
    public List<Hint> getAll(boolean distinct, String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {
        List<Hint> tipsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            Hint hint = null;
            do {
                hint = buildTipFromCursor(cursor);
                if (hint != null) {
                    tipsList.add(hint);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return tipsList;

    }

    public static class TipsColumns {
        public static final String ID = "id";
        public static final String CONTENT = "content";

        public static final int ID_POSITION = 0;
        public static final int CONTENT_POSITION = 1;
    }
}
