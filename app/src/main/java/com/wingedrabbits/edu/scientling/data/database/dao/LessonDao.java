package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.models.Lesson;
import com.wingedrabbits.edu.scientling.models.VocabularySet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class LessonDao extends BaseDao<Lesson> {

    private final String TABLE_NAME = "Lessons"; //TODO sprawdzić czy static przyniesie jakieś korzyści
    public static class LessonsColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String NUMBER = "number";
        public static final String SET_FK = "set_fk";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION =1;
        public static final int NUMBER_POSITION = 2;
        public static final int SET_FK_POSITION =3;
    }
    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            + LessonsColumns.NAME + ", " + LessonsColumns.NUMBER + ", "
            + LessonsColumns.SET_FK + ") VALUES (?,?,?)";
    private final String WHERE_ID = LessonsColumns.ID +"= ?";


    public LessonDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = new String[4];
        mTableColumns[LessonsColumns.ID_POSITION] = LessonsColumns.ID;
        mTableColumns[LessonsColumns.NAME_POSITION] = LessonsColumns.NAME;
        mTableColumns[LessonsColumns.NUMBER_POSITION] = LessonsColumns.NUMBER;
        mTableColumns[LessonsColumns.SET_FK_POSITION] = LessonsColumns.SET_FK;
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
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Lesson entity) {
        long id = entity.getId();
        if(id>0)
        {
            String[] whereArguments = new String[] {String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Lesson get(long id) {
        Lesson lesson = null;
        String[] whereArguments = new String[] {String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
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
    public List<Lesson> getAll() {
        List<Lesson> lessonsList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, mSelection,mSelectionArgs,
                mGroupBy,mHaving,mOrderBy,mLimit);
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
