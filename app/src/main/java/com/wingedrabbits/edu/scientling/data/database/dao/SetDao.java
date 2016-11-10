package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.models.Language;
import com.wingedrabbits.edu.scientling.models.VocabularySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class SetDao extends BaseDao<VocabularySet>{

    private static final  String TABLE_NAME = "Sets";
    public static class SetsColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LANGUAGE_FK = "language_fk";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;
        public static final int LANGUAGE_FK_POSITION = 2;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            + SetsColumns.NAME + ", " + SetsColumns.LANGUAGE_FK
            +") VALUES (?,?)";
    private final String WHERE_ID = SetsColumns.ID + "= ?";

    public SetDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = new String[3];
        mTableColumns[SetsColumns.ID_POSITION] = SetsColumns.ID;
        mTableColumns[SetsColumns.NAME_POSITION] = SetsColumns.NAME;
        mTableColumns[SetsColumns.LANGUAGE_FK_POSITION] = SetsColumns.LANGUAGE_FK;
    }

    @Override
    public long save(VocabularySet entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getName());
        mInsertStatement.bindLong(2, entity.getLanguage().getId());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(VocabularySet entity) {
        final ContentValues values = new ContentValues();
        values.put(SetsColumns.NAME, entity.getName());
        if(entity.getLanguage() != null)
        {
            values.put(SetsColumns.LANGUAGE_FK, entity.getLanguage().getId());
        } else {
            values.putNull(SetsColumns.LANGUAGE_FK);
        }

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);

    }

    @Override
    public void delete(VocabularySet entity) {
        long id = entity.getId();
        if(id > 0)
        {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public VocabularySet get(long id) {
        VocabularySet set = null;
        String[] whereArguments = new String[] {String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            set = buildSetFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return set;
    }

    private VocabularySet buildSetFromCursor(Cursor cursor)
    {
        VocabularySet set = null;
        if(cursor != null)
        {
            set = new VocabularySet();
            set.setId(cursor.getLong(SetsColumns.ID_POSITION));
            set.setName(cursor.getString(SetsColumns.NAME_POSITION));
            long languageId = cursor.getLong(SetsColumns.LANGUAGE_FK_POSITION);
            if(languageId > 0)
            {
                Language language = new Language(); //TODO sprawdzić w praniu
                language.setId(languageId);
                set.setLanguage(language);
            }
        }
        return set;
    }

    @Override
    public List<VocabularySet> getAll() {
        List<VocabularySet> setsList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct,TABLE_NAME,mTableColumns,mSelection,mSelectionArgs,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            VocabularySet set = null;
            do {
                set = buildSetFromCursor(cursor);
                if (set != null)
                {
                    setsList.add(set);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return setsList;
    }
}
