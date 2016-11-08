package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.models.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class SentenceDao extends BaseDao<Sentence> {

    public static final String TABLE_NAME = "Sentences";
    public static class SentenceColumns{
        public static final String ID = "id";
        public static final String SENTENCE = "sentence";
        public static final String TRANSLATION = "translation";

        public static final int ID_POSITION = 0;
        public static final int SENTENCE_POSITION =1;
        public static final int TRANSLATION_POSITION = 2;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            +SentenceColumns.SENTENCE + ", " + SentenceColumns.TRANSLATION
            +") VALUES (?,?)";

    private SQLiteDatabase mDb;
    private SQLiteStatement mInsertStatement;
    private String[] mTableColumns;

    public SentenceDao(SQLiteDatabase db)
    {
        mDb = db;
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = new String[3];
        mTableColumns[SentenceColumns.ID_POSITION] = SentenceColumns.ID;
        mTableColumns[SentenceColumns.SENTENCE_POSITION] = SentenceColumns.SENTENCE;
        mTableColumns[SentenceColumns.TRANSLATION_POSITION] = SentenceColumns.TRANSLATION;
    }
    @Override
    public long save(Sentence entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getSentence());
        mInsertStatement.bindString(2, entity.getTranslation());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Sentence entity) {
        final ContentValues values = new ContentValues();
        values.put(SentenceColumns.SENTENCE, entity.getSentence());
        values.put(SentenceColumns.TRANSLATION, entity.getTranslation());

        String where = SentenceColumns.ID + "= ?";
        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME,values, where, whereArguments);
    }

    @Override
    public void delete(Sentence entity) {
        long id = entity.getId();
        if( id > 0)
        {
            String where = SentenceColumns.ID + "= ?";
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, where, whereArguments);
        }
    }

    @Override
    public Sentence get(long id) {
        Sentence sentence = null;
        String where = SentenceColumns.ID + "= ?";
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns,where,whereArguments,mGroupBy,mHaving, mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            sentence = buildSentenceFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return sentence;
    }

    private Sentence buildSentenceFromCursor(Cursor cursor)
    {
        Sentence sentence = null;
        if(cursor != null)
        {
            sentence = new Sentence();
            sentence.setId(cursor.getLong(SentenceColumns.ID_POSITION));
            sentence.setSentence(cursor.getString(SentenceColumns.SENTENCE_POSITION));
            sentence.setTranslation(cursor.getString(SentenceColumns.TRANSLATION_POSITION));
        }
        return sentence;
    }

    @Override
    public List<Sentence> getAll(){
        List<Sentence> sentencesList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, null,null,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            Sentence sentence = null;
            do {
                sentence = buildSentenceFromCursor(cursor);
                if(sentence != null)
                {
                    sentencesList.add(sentence);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return sentencesList;
    }
}
