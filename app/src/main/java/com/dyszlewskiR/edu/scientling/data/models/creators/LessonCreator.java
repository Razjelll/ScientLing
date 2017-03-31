package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.LessonsColumns.*;

/**
 * Created by Razjelll on 19.12.2016.
 */

public class LessonCreator implements IModelCreator<Lesson> {
    @Override
    public Lesson createFromCursor(Cursor cursor) {
        Lesson lesson = new Lesson();
        int columnsCount = cursor.getColumnCount();
        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            switch (cursor.getColumnName(columnIndex)){
                case ID:
                    lesson.setId(cursor.getLong(columnIndex)); break;
                case NAME:
                    lesson.setName(cursor.getString(columnIndex)); break;
                case NUMBER:
                    lesson.setNumber(cursor.getInt(columnIndex)); break;
                case SET_FK:
                    VocabularySet set = new VocabularySet(cursor.getLong(columnIndex));
                    lesson.setSet(set); break;
                default: //tutaj trafi postęp
                    lesson.setProgress(cursor.getInt(columnIndex));

            }
        }
        return lesson;
    }
}
