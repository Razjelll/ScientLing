package com.wingedrabbits.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wingedrabbits.edu.scientling.models.Category;
import com.wingedrabbits.edu.scientling.models.Definition;
import com.wingedrabbits.edu.scientling.models.PartOfSpeech;
import com.wingedrabbits.edu.scientling.models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 09.11.2016.
 */

public class WordDao extends BaseDao<Word> {

    private final String TABLE_NAME = "Words";
    public static class WordsColumns{
        public static final String ID = "id";
        public static final String WORD = "word";
        public static final String TRANSCRIPTION = "transcription";
        public static final String DEFINITION_FK = "definition_fk";
        public static final String LESSON_FK = "lesson_fk";
        public static final String PART_OF_SPEECH_FK = "part_of_speech_fk";
        public static final String CATEGORY_FK = "category_fk";
        public static final String DIFFICULT = "difficult";
        public static final String MASTER_LEVEL = "master_level";
        public static final String SELECTED = "selected";
        //TODO zobaczyć, czy nie dodać notatki

        public static final int ID_POSITION = 0;
        public static final int WORD_POSITION = 1;
        public static final int TRANSCRIPTION_POSITION = 2;
        public static final int DEFINITION_FK_POSITION = 3;
        public static final int LESSON_FK_POSITION = 4;
        public static final int PART_OF_SPEECH_FK_POSITION = 5;
        public static final int CATEGORY_FK_POSITION = 6;
        public static final int DIFFICULT_POSITION = 7;
        public static final int MASTER_LEVEL_POSITION = 8;
        public static final int SELECTED_POSITION = 9;
    }

    private final String INSERT_STATEMENT =
            "INSERT INTO " + TABLE_NAME + "("
            + WordsColumns.WORD + ", " + WordsColumns.TRANSCRIPTION + ", "
            + WordsColumns.DEFINITION_FK + ", "
            + WordsColumns.LESSON_FK + ", " + WordsColumns.PART_OF_SPEECH_FK +", "
            + WordsColumns.CATEGORY_FK + ", " + WordsColumns.DIFFICULT + ", "
            + WordsColumns.MASTER_LEVEL + " , " + WordsColumns.SELECTED
            +") VALUES (?,?,?,?,?,?,?,?,?)";
    /*private final String SELECT_STATEMENT =
            "SELECT W."+*/
    private final String WHERE_ID = WordsColumns.ID + "= ?";

    public WordDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);

        mTableColumns = new String[10];
        mTableColumns[WordsColumns.ID_POSITION] = WordsColumns.ID;
        mTableColumns[WordsColumns.WORD_POSITION] = WordsColumns.WORD;
        mTableColumns[WordsColumns.TRANSCRIPTION_POSITION] = WordsColumns.TRANSCRIPTION;
        mTableColumns[WordsColumns.DEFINITION_FK_POSITION] = WordsColumns.DEFINITION_FK;
        mTableColumns[WordsColumns.LESSON_FK_POSITION] = WordsColumns.LESSON_FK;
        mTableColumns[WordsColumns.PART_OF_SPEECH_FK_POSITION] = WordsColumns.PART_OF_SPEECH_FK;
        mTableColumns[WordsColumns.CATEGORY_FK_POSITION] = WordsColumns.CATEGORY_FK;
        mTableColumns[WordsColumns.DIFFICULT_POSITION] = WordsColumns.DIFFICULT;
        mTableColumns[WordsColumns.MASTER_LEVEL_POSITION] = WordsColumns.MASTER_LEVEL;
        mTableColumns[WordsColumns.SELECTED_POSITION] = WordsColumns.SELECTED;
    }

    @Override
    public long save(Word entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(WordsColumns.WORD_POSITION, entity.getWord());
        if(entity.getTranscription() != null) {
            mInsertStatement.bindString(WordsColumns.TRANSCRIPTION_POSITION, entity.getTranscription());

        } else {
            mInsertStatement.bindNull(WordsColumns.TRANSCRIPTION_POSITION);
        }
        if(entity.getDefinition() != null)
        {
            mInsertStatement.bindLong(WordsColumns.DEFINITION_FK_POSITION, entity.getDefinition().getId());
        } else {
            mInsertStatement.bindNull(WordsColumns.DEFINITION_FK_POSITION);
        }
        mInsertStatement.bindLong(WordsColumns.LESSON_FK_POSITION, entity.getLessonId());
        if(entity.getPartsOfSpeech() != null) //TODO może będzie trzeba dodać sprawdzanie ID
        {
            mInsertStatement.bindLong(WordsColumns.PART_OF_SPEECH_FK_POSITION, entity.getPartsOfSpeech().getId());
        } else
        {
            mInsertStatement.bindNull(WordsColumns.PART_OF_SPEECH_FK_POSITION);
        }
        if(entity.getCategory() != null)
        {
            mInsertStatement.bindLong(WordsColumns.CATEGORY_FK_POSITION, entity.getCategory().getId());
        } else
        {
            mInsertStatement.bindNull(WordsColumns.CATEGORY_FK_POSITION);
        }
        mInsertStatement.bindLong(WordsColumns.DIFFICULT_POSITION, entity.getDifficult());
        mInsertStatement.bindLong(WordsColumns.MASTER_LEVEL_POSITION, entity.getMasterLevel());
        long selected = entity.isSelected() ? 1 : 0;
        mInsertStatement.bindLong(WordsColumns.SELECTED_POSITION, selected);

        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Word entity) {
        final ContentValues values = new ContentValues();
        values.put(WordsColumns.WORD, entity.getWord());
        values.put(WordsColumns.TRANSCRIPTION, entity.getTranscription());

        if(entity.getDefinition() != null)
        {
            values.put(WordsColumns.DEFINITION_FK, entity.getDefinition().getId());
        } else {
            values.putNull(WordsColumns.DEFINITION_FK);
        }

        values.put(WordsColumns.LESSON_FK, entity.getLessonId());

        if(entity.getPartsOfSpeech() != null) {
            values.put(WordsColumns.PART_OF_SPEECH_FK, entity.getPartsOfSpeech().getId());
        } else {
            values.putNull(WordsColumns.PART_OF_SPEECH_FK);
        }

        if(entity.getCategory() != null)
        {
            values.put(WordsColumns.CATEGORY_FK, entity.getCategory().getId());
        } else
        {
            values.putNull(WordsColumns.CATEGORY_FK);
        }

        values.put(WordsColumns.DIFFICULT, entity.getDifficult());
        values.put(WordsColumns.MASTER_LEVEL, entity.getMasterLevel());

        long selected = entity.isSelected() ? 1: 0;
        values.put(WordsColumns.SELECTED, selected);

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME,values,WHERE_ID,whereArguments);

    }

    @Override
    public void delete(Word entity) {
        long id = entity.getId();
        if(id >0)
        {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Word get(long id) {

        Word word = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            word = buildWordFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        assert cursor.isClosed();

        return word;
    }

    private Word buildWordFromCursor(Cursor cursor)
    {
        Word word = null;
        if(cursor != null)
        {
            word = new Word();
            word.setId(cursor.getLong(WordsColumns.ID_POSITION));
            word.setWord(cursor.getString(WordsColumns.WORD_POSITION));
            word.setTranscription(cursor.getString(WordsColumns.TRANSCRIPTION_POSITION));
            long definitionId = cursor.getLong(WordsColumns.DEFINITION_FK_POSITION);
            if(definitionId > 0)
            {
                Definition definition = new Definition();
                definition.setId(definitionId);
                word.setDefinition(definition);
            }
            word.setLessonId(cursor.getLong(WordsColumns.LESSON_FK_POSITION));
            long partOfSpeechId = cursor.getLong(WordsColumns.PART_OF_SPEECH_FK_POSITION);
            if(partOfSpeechId > 0)
            {
                PartOfSpeech partsOfSpeech = new PartOfSpeech();
                partsOfSpeech.setId(partOfSpeechId);
                word.setPartsOfSpeech(partsOfSpeech);
            }
            long categoryId = cursor.getLong(WordsColumns.CATEGORY_FK_POSITION);
            if(categoryId > 0)
            {
                Category category = new Category();
                category.setId(categoryId);
                word.setCategory(category);
            }
            word.setDifficult((byte)cursor.getLong(WordsColumns.DIFFICULT_POSITION));
            word.setMasterLevel((byte)cursor.getLong(WordsColumns.MASTER_LEVEL_POSITION));
            long selectedValue = cursor.getLong(WordsColumns.SELECTED_POSITION);
            boolean selected = selectedValue == 0 ? false : true;
            word.setSelected(selected);
        }
        return word;
    }



    @Override
    public List<Word> getAll() {

        List<Word> wordsList = new ArrayList<>();
        Cursor cursor = mDb.query(mDistinct, TABLE_NAME, mTableColumns, mSelection, mSelectionArgs,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            Word word = null;
            do {
                word = buildWordFromCursor(cursor);
                if(word != null)
                {
                    wordsList.add(word);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        assert cursor.isClosed();

        return wordsList;
    }
}
