package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.*;
/**
 * Created by Razjelll on 08.11.2016.
 */

public class SetDao extends BaseDao<VocabularySet>{



    private final String INSERT_STATEMENT =
            "INSERT INTO " + SetsTable.TABLE_NAME + "("
            + SetsColumns.NAME + ", " + SetsColumns.LANGUAGE_FK
            +") VALUES (?,?)";
    private final String WHERE_ID = SetsColumns.ID + "= ?";

    public SetDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = SetsTable.getColumns();

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
