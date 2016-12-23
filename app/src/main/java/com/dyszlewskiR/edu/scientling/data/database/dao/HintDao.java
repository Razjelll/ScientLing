package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.HintsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsHintsTable;
import com.dyszlewskiR.edu.scientling.data.models.Hint;
import com.dyszlewskiR.edu.scientling.data.models.creators.HintCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.HintsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.HintsTable.TABLE_NAME;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class HintDao extends BaseDao<Hint> {

    private static final String TABLE_NAME = "Hints";
    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
                    + HintsColumns.CONTENT + ") VALUES (?)";
    private final String SELECT_LINK_STATEMENT =
            "SELECT "+ HintsTable.ALIAS_DOT + "* FROM "
            + WordsHintsTable.TABLE_NAME + " " + WordsHintsTable.ALIAS
            + " LEFT OUTER JOIN " + HintsTable.TABLE_NAME + " " + HintsTable.ALIAS
            +" ON " + WordsHintsTable.ALIAS_DOT + WordsHintsTable.WordsHintsColumns.HINT_FK
            +" = " + HintsTable.ALIAS_DOT + HintsColumns.ID
            + " WHERE " + WordsHintsTable.ALIAS_DOT + WordsHintsTable.WordsHintsColumns.WORD_FK
            + " =?";
    private final String WHERE_ID = HintsColumns.ID + " = ?";

    public HintDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
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
        values.put(HintsColumns.CONTENT, entity.getContent());

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
            HintCreator creator = new HintCreator();
            hint = creator.createFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
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
            HintCreator creator = new HintCreator();
            do {
                hint = creator.createFromCursor(cursor);
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

    public void link(long hintId, long wordId)
    {
        final ContentValues values = new ContentValues();
        values.put(WordsHintsTable.WordsHintsColumns.WORD_FK, wordId);
        values.put(WordsHintsTable.WordsHintsColumns.HINT_FK, hintId);
        mDb.insert(WordsHintsTable.TABLE_NAME, null, values);
    }

    public void unLink(long wordId)
    {
        String where = WordsHintsTable.WordsHintsColumns.WORD_FK + "=?";;
        String[] whereArguments = {String.valueOf(wordId)};
        mDb.delete(WordsHintsTable.TABLE_NAME, where, whereArguments);
    }

    public List<Hint> getLinked(long wordId) {
        List<Hint> hintsList = new ArrayList<>();
        String[] whereArguments = {String.valueOf(wordId)};
        Cursor cursor = mDb.rawQuery(SELECT_LINK_STATEMENT, whereArguments);
        if(cursor.moveToFirst())
        {
            Hint hint = null;
            HintCreator creator = new HintCreator();
            do{
                hint = creator.createFromCursor(cursor);
                if(hint != null)
                {
                    hintsList.add(hint);
                }
            }while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return hintsList;
    }

    public void deleteUnlinked()
    {
        String statement = "DELETE FROM " + TABLE_NAME + " WHERE " + HintsColumns.ID + " IS NOT IN ("
                + " SELECT " + WordsHintsTable.WordsHintsColumns.HINT_FK + " FROM " + WordsHintsTable.TABLE_NAME + " )";
        mDb.execSQL(statement);
    }


}
