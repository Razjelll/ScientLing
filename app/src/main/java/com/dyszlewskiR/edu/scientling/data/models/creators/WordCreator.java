package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.models.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;

import static com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable.CategoriesColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable.DefinitionsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.PartsOfSpeechTable.PartsOfSpeechColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.WordsColumns;

/**
 * Created by Razjelll on 13.11.2016.
 */

public class WordCreator implements IModelCreator<Word> {

    @Override
    public Word createFromCursor(Cursor cursor) {
        Word word = new Word();
        Definition definition = null;
        Category category = null;
        PartOfSpeech partOfSpeech = null;
        int columnsCount = cursor.getColumnCount();
        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            switch (cursor.getColumnName(columnIndex)) {
                case WordsColumns.ID:
                    word.setId(cursor.getLong(columnIndex));
                    break;
                case WordsColumns.CONTENT:
                    word.setContent(cursor.getString(columnIndex));
                    break;
                case WordsColumns.DEFINITION_FK:
                    if (!cursor.isNull(columnIndex)) {
                        if (definition == null) {
                            definition = new Definition(cursor.getLong(columnIndex));
                        } else {
                            definition.setId(cursor.getLong(columnIndex));
                        }
                    }
                    break;
                case WordsColumns.LESSON_FK:
                    word.setLessonId(cursor.getLong(columnIndex));
                    break;
                case WordsColumns.PART_OF_SPEECH_FK:
                    if (!cursor.isNull(columnIndex)) {
                        if (partOfSpeech == null) {
                            partOfSpeech = new PartOfSpeech(cursor.getLong(columnIndex));
                        } else {
                            partOfSpeech.setId(cursor.getLong(columnIndex));
                        }
                    }
                    break;
                case WordsColumns.CATEGORY_FK:
                    if (!cursor.isNull(columnIndex)) {
                        if (category == null) {
                            category = new Category(cursor.getLong(columnIndex));
                        } else {
                            category.setId(cursor.getLong(columnIndex));
                        }
                    }
                    break;
                case WordsColumns.DIFFICULT:
                    word.setDifficult((byte) cursor.getInt(columnIndex));
                    break;
                case WordsColumns.MASTER_LEVEL:
                    word.setMasterLevel((byte) cursor.getInt(columnIndex));
                    break;
                case WordsColumns.SELECTED:
                    word.setSelected(cursor.getInt(columnIndex) == 1);
                    break;
                case WordsColumns.OWN:
                    word.setOwn(cursor.getInt(columnIndex) == 1);
                    break;
                case WordsColumns.LEARNING_DATE:
                    if (!cursor.isNull(columnIndex)) {
                        word.setLearningDate(cursor.getInt(columnIndex));
                    }
                    break;
                case DefinitionsColumns.CONTENT_ALIAS:
                    if (!cursor.isNull(columnIndex)) {
                        if (definition == null) {
                            definition = new Definition();
                        }
                        definition.setContent(cursor.getString(columnIndex));
                    }
                    break;
                case DefinitionsColumns.TRANSLATION_ALIAS:
                    if (!cursor.isNull(columnIndex)) {
                        if (definition == null) {
                            definition = new Definition();
                        }
                        definition.setTranslation(cursor.getString(columnIndex));
                    }
                    break;
                case PartsOfSpeechColumns.NAME_ALIAS:
                    if (!cursor.isNull(columnIndex)) {
                        if (partOfSpeech == null) {
                            partOfSpeech = new PartOfSpeech();
                        }
                        partOfSpeech.setName(cursor.getString(columnIndex));
                    }
                    break;
                case CategoriesColumns.NAME_ALIAS:
                    if (!cursor.isNull(columnIndex)) {
                        if (category == null) {
                            category = new Category();
                        }
                        category.setName(cursor.getString(columnIndex));
                    }
                    break;
                case WordsColumns.IMAGE_NAME:
                    if (!cursor.isNull(columnIndex)) {
                        word.setImageName(cursor.getString(columnIndex));
                    }
                    break;
                case WordsColumns.RECORD_NAME:
                    if (!cursor.isNull(columnIndex)) {
                        word.setRecordName(cursor.getString(columnIndex));
                    }
                    break;
            }
            word.setDefinition(definition);
            word.setPartsOfSpeech(partOfSpeech);
            word.setCategory(category);
        }
        return word;
    }
}
