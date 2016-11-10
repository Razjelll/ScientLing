package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.models.PartOfSpeech;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class PartOfSpeechDao extends BaseDao<PartOfSpeech> {

    public static final String TABLE_NAME = "PartsOfSpeech";
    public static class PartsOfSpeechColumns {
        public static final String ID = "id";
        public static final String NAME = "name";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;

        public static final int COLUMNS_COUNT = 2;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            +PartsOfSpeechColumns.NAME
            + ") VALUES (?)";
    private final String WHERE_ID = PartsOfSpeechColumns.ID + "= ?";


    public PartOfSpeechDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = new String[PartsOfSpeechColumns.COLUMNS_COUNT];
        mTableColumns[PartsOfSpeechColumns.ID_POSITION] = PartsOfSpeechColumns.ID;
        mTableColumns[PartsOfSpeechColumns.NAME_POSITION] = PartsOfSpeechColumns.NAME;
    }

    @Override
    public long save(PartOfSpeech entity){
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(PartsOfSpeechColumns.NAME_POSITION, entity.getName());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(PartOfSpeech entity) {
        final ContentValues values = new ContentValues();
        values.put(PartsOfSpeechColumns.NAME, entity.getName());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME,values,WHERE_ID,whereArguments);
    }

    @Override
    public void delete(PartOfSpeech entity) {
        long id = entity.getId();
        if(id > 0)
        {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME,WHERE_ID,whereArguments);
        }
    }

    @Override
    public PartOfSpeech get(long id) {
        PartOfSpeech partOfSpeech = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            partOfSpeech = buildPartOfSpeechFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }

        assert cursor.isClosed();
        return partOfSpeech;
    }

    private PartOfSpeech buildPartOfSpeechFromCursor(Cursor cursor)
    {
        PartOfSpeech partOfSpeech = null;
        if(cursor != null)
        {
            partOfSpeech = new PartOfSpeech();
            partOfSpeech.setId(cursor.getLong(PartsOfSpeechColumns.ID_POSITION));
            partOfSpeech.setName(cursor.getString(PartsOfSpeechColumns.NAME_POSITION));
        }
        return partOfSpeech;
    }

    @Override
    public List<PartOfSpeech> getAll() {
        List<PartOfSpeech> partsOfSpeechList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, mSelection,mSelectionArgs,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            PartOfSpeech partOfSpeech = null;
            do {
                partOfSpeech = buildPartOfSpeechFromCursor(cursor);
                if(partOfSpeech != null)
                {
                    partsOfSpeechList.add(partOfSpeech);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }

        assert cursor.isClosed();

        return partsOfSpeechList;
    }
}
