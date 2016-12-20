package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.Word;

/**
 * Created by Razjelll on 13.11.2016.
 */

public class WordCreator implements IModelCreator<Word> {

    private final int DEFINITION_DEFINITION_POSITION = WordsTable.WordsColumns.COLUMNS_COUNT;
    private final int DEFINITION_TRANSLATION_POSITION = WordsTable.WordsColumns.COLUMNS_COUNT + 1;
    private final int PART_NAME_POSITION = WordsTable.WordsColumns.COLUMNS_COUNT + 2;
    private final int CATEGORY_NAME_POSITION = WordsTable.WordsColumns.COLUMNS_COUNT + 3;

    @Override
    public Word createFromCursor(Cursor cursor) {
        Word word = null;
        if (cursor != null) {
            word = new Word();
            word.setId(cursor.getLong(WordsTable.WordsColumns.ID_POSITION));
            word.setContent(cursor.getString(WordsTable.WordsColumns.CONTENT_POSITION));
            if (cursor.getColumnCount() > 2) {
                word.setTranscription(cursor.getString(WordsTable.WordsColumns.TRANSCRIPTION_POSITION));
                long definitionId = cursor.getLong(WordsTable.WordsColumns.DEFINITION_FK_POSITION);
                if (definitionId > 0) {
                    Definition definition = new Definition();
                    definition.setId(definitionId);
                    definition.setContent(cursor.getString(DEFINITION_DEFINITION_POSITION));
                    definition.setTranslation(cursor.getString(DEFINITION_TRANSLATION_POSITION));
                    word.setDefinition(definition);
                }
                word.setLessonId(cursor.getLong(WordsTable.WordsColumns.LESSON_FK_POSITION));
                long partOfSpeechId = cursor.getLong(WordsTable.WordsColumns.PART_OF_SPEECH_FK_POSITION);
                if (partOfSpeechId > 0) {
                    PartOfSpeech partOfSpeech = new PartOfSpeech();
                    partOfSpeech.setId(partOfSpeechId);
                    partOfSpeech.setName(cursor.getString(PART_NAME_POSITION));
                    word.setPartsOfSpeech(partOfSpeech);
                }
                long categoryId = cursor.getLong(WordsTable.WordsColumns.CATEGORY_FK_POSITION);
                if (categoryId > 0) {
                    Category category = new Category();
                    category.setId(categoryId);
                    category.setName(cursor.getString(CATEGORY_NAME_POSITION));
                    word.setCategory(category);
                }
                word.setDifficult((byte) cursor.getLong(WordsTable.WordsColumns.DIFFICULT_POSITION));
                word.setMasterLevel((byte) cursor.getLong(WordsTable.WordsColumns.MASTER_LEVEL_POSITION));
                long selectedValue = cursor.getLong(WordsTable.WordsColumns.SELECTED_POSITION);
                boolean selected = selectedValue != 0;
                word.setSelected(selected);
            }
        }
        return word;
    }
}
