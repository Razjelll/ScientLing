package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.tables.ExercisesTable;
import com.dyszlewskiR.edu.scientling.data.models.Exercise;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.ExercisesTable.ExercisesColumns;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class ExerciseDao extends BaseDao<Exercise> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + ExercisesTable.TABLE_NAME + "("
                    + ExercisesColumns.NAME + ") VALUES (?)";
    private final String WHERE_ID = ExercisesColumns.ID + "= ?";

    public ExerciseDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = ExercisesTable.getColumns();
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
        mDb.update(ExercisesTable.TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Exercise entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(ExercisesTable.TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Exercise get(long id) {
        Exercise exercise = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(ExercisesTable.TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            exercise = buildExerciseFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return exercise;
    }

    private Exercise buildExerciseFromCursor(Cursor cursor) {
        Exercise exercise = null;
        if (cursor != null) {
            exercise = new Exercise();
            exercise.setId(cursor.getLong(ExercisesColumns.ID_POSITION));
            exercise.setName(cursor.getString(ExercisesColumns.NAME_POSITION));
        }
        return exercise;
    }

    @Override
    public List<Exercise> getAll(boolean distinct, String[] columns, String selection, String[] selectionArgs,
                                 String groupBy, String having, String orderBy, String limit) {
        List<Exercise> exercisesList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, ExercisesTable.TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            Exercise exercise = null;
            do {
                exercise = buildExerciseFromCursor(cursor);
                if (exercise != null) {
                    exercisesList.add(exercise);
                }
            } while (cursor.moveToNext());
        }
        return exercisesList;
    }
}
