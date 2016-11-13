package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.models.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class TipDao extends BaseDao<Tip> {

    private static final String TABLE_NAME = "Tips";

    public static class TipsColumns {
        public static final String ID = "id";
        public static final String CONTENT = "content";

        public static final int ID_POSITION = 0;
        public static final int CONTENT_POSITION = 1;
    }

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
    public long save(Tip entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getContent());

        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Tip entity) {
        final ContentValues values = new ContentValues();
        values.put(TipsColumns.CONTENT, entity.getContent());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Tip entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Tip get(long id) {
        Tip tip = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            tip = buildTipFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return tip;
    }

    private Tip buildTipFromCursor(Cursor cursor) {
        Tip tip = null;
        if (cursor != null) {
            tip = new Tip();
            tip.setId(cursor.getLong(TipsColumns.ID_POSITION));
            tip.setContent(cursor.getString(TipsColumns.CONTENT_POSITION));
        }
        return tip;
    }

    @Override
    public List<Tip> getAll(boolean distinct,String[] columns, String selection, String[] selectionArgs,
                            String groupBy, String having, String orderBy, String limit) {
        List<Tip> tipsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            Tip tip = null;
            do {
                tip = buildTipFromCursor(cursor);
                if (tip != null) {
                    tipsList.add(tip);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return tipsList;

    }
}
