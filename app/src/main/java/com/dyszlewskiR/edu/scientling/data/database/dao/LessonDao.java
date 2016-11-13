package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.*;
/**
 * Created by Razjelll on 09.11.2016.
 */

public class LessonDao extends BaseDao<Lesson> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + LessonsTable.TABLE_NAME + "("
            + LessonsColumns.NAME + ", " + LessonsColumns.NUMBER + ", "
            + LessonsColumns.SET_FK + ") VALUES (?,?,?)";
    private final String WHERE_ID = LessonsColumns.ID +"= ?";


    public LessonDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = LessonsTable.getColumns();
    }

    @Override
    public long save(Lesson entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(LessonsColumns.NAME_POSITION, entity.getName());
        mInsertStatement.bindLong(LessonsColumns.NUMBER_POSITION, entity.getNumber());
        mInsertStatement.bindLong(LessonsColumns.SET_FK_POSITION, entity.getSet().getId());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Lesson entity) {
        final ContentValues values = new ContentValues();
        values.put(LessonsColumns.NAME, entity.getName());
        values.put(LessonsColumns.NUMBER, entity.getNumber());
        values.put(LessonsColumns.SET_FK, entity.getSet().getId());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(LessonsTable.TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Lesson entity) {
        long id = entity.getId();
        if(id>0)
        {
            String[] whereArguments = new String[] {String.valueOf(id)};
            mDb.delete(LessonsTable.TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Lesson get(long id) {
        Lesson lesson = null;
        String[] whereArguments = new String[] {String.valueOf(id)};
        Cursor cursor = mDb.query(LessonsTable.TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            lesson = buildLessonFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return lesson;
    }

    private Lesson buildLessonFromCursor(Cursor cursor)
    {
        Lesson lesson = null;
        if(cursor != null)
        {
            lesson = new Lesson();
            lesson.setId(cursor.getLong(LessonsColumns.ID_POSITION));
            lesson.setName(cursor.getString(LessonsColumns.NAME_POSITION));
            lesson.setNumber(cursor.getLong(LessonsColumns.NUMBER_POSITION));
            long setId = cursor.getLong(LessonsColumns.SET_FK_POSITION);
            if(setId > 0)
            {
                VocabularySet set = new VocabularySet();
                set.setId(setId);
                lesson.setSet(set);
            }
        }
        return lesson;
    }

    @Override
    public List<Lesson> getAll(boolean distinct,String[] columns, String selection, String[] selectionArgs,
                               String groupBy, String having, String orderBy, String limit) {
        List<Lesson> lessonsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, LessonsTable.TABLE_NAME, columns, selection,selectionArgs,
                groupBy,having,orderBy,limit);
        if(cursor.moveToFirst())
        {
            Lesson lesson = null;
            do {
                lesson = buildLessonFromCursor(cursor);
                if(lesson != null)
                {
                    lessonsList.add(lesson);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        assert cursor.isClosed();

        return lessonsList;
    }
}
