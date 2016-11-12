package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.ExampleSentences;
import com.dyszlewskiR.edu.scientling.data.database.tables.SentencesTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.creators.SentenceCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SentencesTable.*;
/**
 * Created by Razjelll on 08.11.2016.
 */

public class SentenceDao extends BaseDao<Sentence> {



    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            +SentencesColumns.SENTENCE + ", " + SentencesColumns.TRANSLATION
            +") VALUES (?,?)";

    private final String SELECT_LINK_STATEMENT =
            "SELECT S.* FROM " + ExampleSentences.TABLE_NAME+ " ES JOIN "
            + SentencesTable.TABLE_NAME + " S ON ES."
            + ExampleSentences.ExampleSentencesColumns.SENTENCE_FK + "= S."
            + SentencesColumns.ID + " WHERE ES." + ExampleSentences.ExampleSentencesColumns.WORD_FK
            + " = ?";

    public SentenceDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = SentencesTable.getColumns();
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
        values.put(SentencesColumns.SENTENCE, entity.getSentence());
        values.put(SentencesColumns.TRANSLATION, entity.getTranslation());

        String where = SentencesColumns.ID + "= ?";
        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME,values, where, whereArguments);
    }

    @Override
    public void delete(Sentence entity) {
        long id = entity.getId();
        if( id > 0)
        {
            String where = SentencesColumns.ID + "= ?";
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, where, whereArguments);
        }
    }

    @Override
    public Sentence get(long id) {
        Sentence sentence = null;
        String where = SentencesColumns.ID + "= ?";
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns,where,whereArguments,mGroupBy,mHaving, mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            SentenceCreator sentenceCreator = new SentenceCreator();
            sentence = sentenceCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
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
            SentenceCreator sentenceCreator = new SentenceCreator();
            do {
                sentence = sentenceCreator.createFromCursor(cursor);
                if(sentence != null)
                {
                    sentencesList.add(sentence);
                }
            } while(cursor.moveToNext());
        }
        closeCursor(cursor);
        return sentencesList;
    }

    //Tutaj znajdują sie metody, ktróre obsługują tabele ExampelSentences

    public void link(long sentenceId, long wordId)
    {
        final ContentValues values= new ContentValues();
        values.put(ExampleSentences.ExampleSentencesColumns.WORD_FK, wordId);
        values.put(ExampleSentences.ExampleSentencesColumns.SENTENCE_FK, sentenceId);
        mDb.insert(ExampleSentences.TABLE_NAME, null, values);
    }

    //TODO dorobić metodę unlink, zastanowić się jak ma działać

    public List<Sentence> getLinked(long wordId)
    {
        List<Sentence> sentencesList = new ArrayList<>();
        String[] whereArguments = new String[]{String.valueOf(wordId)};
        Cursor cursor = mDb.rawQuery(SELECT_LINK_STATEMENT, whereArguments);
        if(cursor.moveToFirst())
        {
            Sentence sentence;
            SentenceCreator sentenceCreator = new SentenceCreator();
            do{
                sentence =sentenceCreator.createFromCursor(cursor);
                if(sentence != null)
                {
                    sentencesList.add(sentence);
                }
            } while(cursor.moveToNext());
        }
        closeCursor(cursor);
        return sentencesList;
    }

    public Sentence getByContent(String content)
    {
        Sentence sentence = null;
        String where = SentencesColumns.SENTENCE + " = ?";
        String[] whereArguments = new String[]{content};
        Cursor cursor = mDb.query(SentencesTable.TABLE_NAME, mTableColumns, where, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst()){
            SentenceCreator sentenceCreator = new SentenceCreator();
            sentence = sentenceCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return sentence;
    }
}
