package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.utils.DateCalculator;

import java.util.Date;

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
            word = createBasicWordFromCursor(cursor);
            if (cursor.getColumnCount() > 2) {
                word.setTranscription(cursor.getString(WordsTable.WordsColumns.TRANSCRIPTION_POSITION));
                word.setDefinition(getDefinitionFromCursor(cursor));
                word.setLessonId(cursor.getLong(WordsTable.WordsColumns.LESSON_FK_POSITION));
                word.setPartsOfSpeech(getPartOfSpeechFromCursor(cursor));
                word.setCategory(getCategoryFromCursor(cursor));
                word.setDifficult((byte) cursor.getLong(WordsTable.WordsColumns.DIFFICULT_POSITION));
                word.setMasterLevel((byte) cursor.getLong(WordsTable.WordsColumns.MASTER_LEVEL_POSITION));
                word.setSelected(getSelectedFromCursor(cursor));
                word.setOwn(getOwnFromCursor(cursor));
                word.setLearningDate(getLearningDateFromCursor(cursor));
            }
        }
        return word;
    }

    private Word createBasicWordFromCursor(Cursor cursor) {
        Word word = new Word();
        word.setId(cursor.getLong(WordsTable.WordsColumns.ID_POSITION));
        word.setContent(cursor.getString(WordsTable.WordsColumns.CONTENT_POSITION));
        return word;
    }

    private Definition getDefinitionFromCursor(Cursor cursor) {
        Definition definition = null;
        long definitionId = cursor.getLong(WordsTable.WordsColumns.DEFINITION_FK_POSITION);
        if (definitionId > 0) {
            definition = new Definition();
            definition.setId(definitionId);
            definition.setContent(cursor.getString(DEFINITION_DEFINITION_POSITION));
            definition.setTranslation(cursor.getString(DEFINITION_TRANSLATION_POSITION));
        }
        return definition;
    }

    private PartOfSpeech getPartOfSpeechFromCursor(Cursor cursor) {
        PartOfSpeech partOfSpeech = null;
        long partOfSpeechId = cursor.getLong(WordsTable.WordsColumns.PART_OF_SPEECH_FK_POSITION);
        if (partOfSpeechId > 0) {
            partOfSpeech = new PartOfSpeech();
            partOfSpeech.setId(partOfSpeechId);
            partOfSpeech.setName(cursor.getString(PART_NAME_POSITION));
        }
        return partOfSpeech;
    }

    private Category getCategoryFromCursor(Cursor cursor) {
        Category category = null;
        long categoryId = cursor.getLong(WordsTable.WordsColumns.CATEGORY_FK_POSITION);
        if (categoryId > 0) {
            category = new Category();
            category.setId(categoryId);
            category.setName(cursor.getString(CATEGORY_NAME_POSITION));
        }
        return category;
    }

    private boolean getSelectedFromCursor(Cursor cursor) {
        long selectedValue = cursor.getLong(WordsTable.WordsColumns.SELECTED_POSITION);
        boolean selected = selectedValue != 0;
        return selected;
    }

    private boolean getOwnFromCursor(Cursor cursor) {
        long ownValue = cursor.getInt(WordsTable.WordsColumns.OWN_POSITION);
        boolean own = ownValue != 0;
        return own;
    }

    private Date getLearningDateFromCursor(Cursor cursor) {
        int dateValue = cursor.getInt(WordsTable.WordsColumns.LEARNING_DATE_POSITION);
        Date date = DateCalculator.intToDate(dateValue);
        return date;
    }

}
