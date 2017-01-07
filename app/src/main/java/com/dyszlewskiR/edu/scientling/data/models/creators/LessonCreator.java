package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

/**
 * Created by Razjelll on 19.12.2016.
 */

public class LessonCreator implements IModelCreator<Lesson> {
    @Override
    public Lesson createFromCursor(Cursor cursor) {
        Lesson lesson = null;
        if (cursor != null) {
            lesson = new Lesson();
            lesson.setId(cursor.getLong(LessonsTable.LessonsColumns.ID_POSITION));
            lesson.setName(cursor.getString(LessonsTable.LessonsColumns.NAME_POSITION));
            lesson.setNumber(cursor.getLong(LessonsTable.LessonsColumns.NUMBER_POSITION));
            long setId = cursor.getLong(LessonsTable.LessonsColumns.SET_FK_POSITION);
            if (setId > 0) {
                VocabularySet set = new VocabularySet();
                set.setId(setId);
                lesson.setSet(set);
            }
            //jeżeli poza kolumnami tabeli jest do zapisania postep lekcji
            if(cursor.getColumnCount() > LessonsTable.getColumns().length)
            {
                if(cursor.isNull(cursor.getColumnCount()-1))
                {
                    lesson.setProgress(0);
                }
                else
                {
                    long progress = cursor.getLong(cursor.getColumnCount()-1);
                    lesson.setProgress((int)progress);
                }
            }
        }
        return lesson;
    }
}
