package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wingedrabbits.edu.scientling.models.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class ExerciseDao extends BaseDao<Exercise> {

    private static final String TABLE_NAME = "Exercises";
    public static class ExercisesColumns{
        public static final String ID = "id";
        public static final String NAME = "name";

        public static final int ID_POSITION = 0;
        public static final int NAME_POSITION = 1;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            + ExercisesColumns.NAME + ") VALUES (?)";
    private final String WHERE_ID = ExercisesColumns.ID + "= ?";

    private SQLiteDatabase mDb;
    private SQLiteStatement mInsertStatement;
    private String[] mTableColumns;

    public ExerciseDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = new String[2];
        mTableColumns[ExercisesColumns.ID_POSITION] = ExercisesColumns.ID;
        mTableColumns[ExercisesColumns.NAME_POSITION] = ExercisesColumns.NAME;
    }

    @Override
    public long save(Exercise entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getName());

        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Exercise entity) {
        final ContentValues values = new ContentValues();
        values.put(ExercisesColumns.NAME, entity.getName());

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Exercise entity) {
        long id = entity.getId();
        if(id >0 )
        {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Exercise get(long id) {
        Exercise exercise = null;
        String[] whereArguments = new String[] {String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            exercise = buildExerciseFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return exercise;
    }

    private Exercise buildExerciseFromCursor(Cursor cursor)
    {
        Exercise exercise = null;
        if(cursor != null)
        {
            exercise = new Exercise();
            exercise.setId(cursor.getLong(ExercisesColumns.ID_POSITION));
            exercise.setName(cursor.getString(ExercisesColumns.NAME_POSITION));
        }
        return exercise;
    }

    @Override
    public List<Exercise> getAll() {
        List<Exercise> exercisesList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, mSelection, mSelectionArgs,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            Exercise exercise = null;
            do{
                exercise = buildExerciseFromCursor(cursor);
                if(exercise != null)
                {
                    exercisesList.add(exercise);
                }
            } while (cursor.moveToNext());
        }
        return exercisesList;
    }
}
