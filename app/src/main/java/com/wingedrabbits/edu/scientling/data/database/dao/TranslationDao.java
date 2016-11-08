package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.models.Language;
import com.wingedrabbits.edu.scientling.models.Translation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 07.11.2016.
 */

public class TranslationDao extends BaseDao<Translation> {

    private static final String TABLE_NAME = "Translations";
    public static class TranslationsColumns{
        public static final String ID = "id";
        public static final String TRANSLATION = "translation";

        public static final int ID_POSITION = 0;
        public static final int TRANSLATION_POSITION = 1;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            +TranslationsColumns.TRANSLATION + ") VALUES (?)";

    private final String WHERE_ID = TranslationsColumns.ID + " = ?";


    private SQLiteDatabase mDb;
    private SQLiteStatement mInsertStatement;
    private String[] mTableColumns;


    public TranslationDao(SQLiteDatabase db)
    {
        super();
        mDb = db;
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = new String[2];
        mTableColumns[TranslationsColumns.ID_POSITION] = TranslationsColumns.ID;
        mTableColumns[TranslationsColumns.TRANSLATION_POSITION] = TranslationsColumns.TRANSLATION;
    }

    @Override
    public long save(Translation entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getTranslation());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Translation entity) {
        final ContentValues values = new ContentValues();
        values.put(TranslationsColumns.TRANSLATION, entity.getTranslation());
        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME,values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Translation entity) {
        if(entity != null)
        {
            long id = entity.getId();
            if(id > 0)
            {
                String[] whereAttributes = new String[]{String.valueOf(id)};
                mDb.delete(TABLE_NAME, WHERE_ID, whereAttributes);
            }
        }

    }

    @Override
    public Translation get(long id){
        Translation translation = null;
        String[] whereAttributes = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereAttributes,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            translation = this.buildTranslationFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translation;
    }

    private Translation buildTranslationFromCursor(Cursor cursor)
    {
        Translation translation = null;
        if(cursor != null)
        {
            translation = new Translation();
            translation.setId(cursor.getLong(TranslationsColumns.ID_POSITION));
            translation.setTranslation(cursor.getString(TranslationsColumns.TRANSLATION_POSITION));
        }
        return translation;
    }

    @Override
    public List<Translation> getAll() {
        List<Translation> translationsList = new ArrayList<>();
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, null, null,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            Translation translation = null;
            do{
                translation = buildTranslationFromCursor(cursor);
                if(translation != null)
                {
                    translationsList.add(translation);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translationsList;
    }
}
