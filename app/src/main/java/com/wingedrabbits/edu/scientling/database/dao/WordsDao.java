package com.wingedrabbits.edu.scientling.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.model.Word;

import java.util.ArrayList;
import java.util.List;

import static com.wingedrabbits.edu.scientling.database.table.WordsTable.*;

/**
 * Created by Razjelll on 18.10.2016.
 */

public class WordsDao implements Dao<Word> {

    private static final String INSERT = String.format("INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
            TABLE_NAME, WordsColumns.WORD, WordsColumns.PRONUNCIATION,
            WordsColumns.TYPE, WordsColumns.CATEGORY, WordsColumns.MASTER_LEVEL,
            WordsColumns.REPETITIONS);


    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public WordsDao(SQLiteDatabase db)
    {
        this.db = db;
        insertStatement = db.compileStatement(INSERT);
    }
    @Override
    public long save(Word entity) {
        insertStatement.clearBindings();
        insertStatement.bindString(1,entity.getContent());
        insertStatement.bindString(2, entity.getPronunciation());
        insertStatement.bindString(3, entity.getType().getName());
        insertStatement.bindString(4, entity.getCategory().getName());
        insertStatement.bindLong(5, entity.getMasterLevel());

        return insertStatement.executeInsert();
    }

    @Override
    public void update(Word entity) {
        final ContentValues values = new ContentValues();
        values.put(WordsColumns.WORD, entity.getContent());
        values.put(WordsColumns.PRONUNCIATION, entity.getPronunciation());
        values.put(WordsColumns.TYPE, entity.getType().getName());
        values.put(WordsColumns.CATEGORY, entity.getCategory().getName());
        values.put(WordsColumns.MASTER_LEVEL, entity.getMasterLevel());

        db.update(TABLE_NAME, values,
                WordsColumns.ID + " = ?",
                new String[] {String.valueOf(entity.getId())});
    }

    @Override
    public void delete(Word entity) {
        if(entity.getId()>0)
        {
            db.delete(TABLE_NAME,
                    WordsColumns.ID + "= ?",
                    new String[]{String.valueOf(entity.getId())});
        }
    }

    @Override
    public Word get(long id) {
        Word word = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{WordsColumns.WORD, WordsColumns.PRONUNCIATION,
                        WordsColumns.TYPE, WordsColumns.CATEGORY,
                        WordsColumns.MASTER_LEVEL, WordsColumns.REPETITIONS},
                WordsColumns.ID + " = ?", new String[]{String.valueOf(id)},
                null,null,null,"1");
        if(cursor.moveToFirst()) {
            word = this.buildWordFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return word;

    }

    @Override
    public List<Word> getAll() {
        List<Word> list = new ArrayList<Word>();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{WordsColumns.ID, WordsColumns.WORD,
                        WordsColumns.PRONUNCIATION, WordsColumns.TYPE,
                        WordsColumns.CATEGORY, WordsColumns.MASTER_LEVEL,
                        WordsColumns.REPETITIONS},
                null,null,null,null, WordsColumns.WORD, null);
        if(cursor.moveToFirst()) {
            do{
                Word word = this.buildWordFromCursor(cursor);
                if(word != null)
                {
                    list.add(word);
                }
            }while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

    public Word find(String word)
    {
        long wordId = 0L;
        String sql = String.format("SELECT %s WHERE UPPER(%s) = ? LIMIT 1", WordsColumns.ID, WordsColumns.WORD);
        Cursor cursor = db.rawQuery(sql, new String[]{word.toUpperCase()});
        if (cursor.moveToFirst()) {

            wordId = cursor.getLong(WordsColPositon.ID);
        }
        if(!cursor.isClosed()) {
            cursor.close();
        }
        return get(wordId);
    }

    private Word buildWordFromCursor(Cursor cursor) {
        Word word = null;
        if(cursor != null) {
            word = new Word();
            word.setId(cursor.getLong(WordsColPositon.ID));
            word.setContent(cursor.getString(WordsColPositon.WORD));
            word.setPronunciation(cursor.getString(WordsColPositon.PRONUNCIATION));
            word.setMasterLevel(cursor.getInt(WordsColPositon.MASTER_LEVEL));

        }
        return word;
    }
}
