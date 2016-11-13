package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.DefinitionsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.PartsOfSpeechTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.data.models.creators.WordCreator;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.*;
/**
 * Created by Razjelll on 09.11.2016.
 */

public class WordDao extends BaseDao<Word> {



    private final String INSERT_STATEMENT =
            "INSERT INTO " + WordsTable.TABLE_NAME + "("
                    + WordsColumns.WORD + ", " + WordsColumns.TRANSCRIPTION + ", "
                    + WordsColumns.DEFINITION_FK + ", "
                    + WordsColumns.LESSON_FK + ", " + WordsColumns.PART_OF_SPEECH_FK + ", "
                    + WordsColumns.CATEGORY_FK + ", " + WordsColumns.DIFFICULT + ", "
                    + WordsColumns.MASTER_LEVEL + " , " + WordsColumns.SELECTED
                    + ") VALUES (?,?,?,?,?,?,?,?,?)";

    private final String SELECT_STATEMENT =
            "SELECT W." + WordsColumns.ID + ", W." + WordsColumns.WORD
                    + ", W." + WordsColumns.TRANSCRIPTION + ", W." + WordsColumns.DEFINITION_FK
                    + ", W." + WordsColumns.LESSON_FK + ", W." + WordsColumns.PART_OF_SPEECH_FK
                    + ", W." + WordsColumns.CATEGORY_FK + ", W." + WordsColumns.DIFFICULT + ", W." + WordsColumns.MASTER_LEVEL
                    + ", W." + WordsColumns.SELECTED + ", D." + DefinitionsTable.DefinitionsColumns.DEFINITION
                    + ", D." + DefinitionsTable.DefinitionsColumns.TRANSLATION
                    + ", P." + PartsOfSpeechTable.PartsOfSpeechColumns.NAME
                    + ", C." + CategoriesTable.CategoriesColumns.NAME
                    + " FROM " + WordsTable.TABLE_NAME + " W LEFT OUTER JOIN " + DefinitionsTable.TABLE_NAME + " D ON "
                    + "W." + WordsColumns.DEFINITION_FK + " = " + "D." + DefinitionsTable.DefinitionsColumns.ID
                    + " LEFT OUTER JOIN " + PartsOfSpeechTable.TABLE_NAME + " P ON W." + WordsColumns.PART_OF_SPEECH_FK
                    + " = P." + PartsOfSpeechTable.PartsOfSpeechColumns.ID
                    + " LEFT OUTER JOIN " + CategoriesTable.TABLE_NAME + " C ON "
                    + "W." + WordsColumns.CATEGORY_FK + " = C." +CategoriesTable.CategoriesColumns.ID;
                   // + " WHERE W." + WordsColumns.ID + " = ?";



    private final String WHERE_ID = WordsColumns.ID + "= ?";

    public WordDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = WordsTable.getColumns();

    }

    @Override
    public long save(Word entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(WordsColumns.WORD_POSITION, entity.getWord());
        if (entity.getTranscription() != null) {
            mInsertStatement.bindString(WordsColumns.TRANSCRIPTION_POSITION, entity.getTranscription());

        } else {
            mInsertStatement.bindNull(WordsColumns.TRANSCRIPTION_POSITION);
        }
        if (entity.getDefinition() != null) {
            mInsertStatement.bindLong(WordsColumns.DEFINITION_FK_POSITION, entity.getDefinition().getId());
        } else {
            mInsertStatement.bindNull(WordsColumns.DEFINITION_FK_POSITION);
        }
        mInsertStatement.bindLong(WordsColumns.LESSON_FK_POSITION, entity.getLessonId());
        if (entity.getPartsOfSpeech() != null)
        {
            mInsertStatement.bindLong(WordsColumns.PART_OF_SPEECH_FK_POSITION, entity.getPartsOfSpeech().getId());
        } else {
            mInsertStatement.bindNull(WordsColumns.PART_OF_SPEECH_FK_POSITION);
        }
        if (entity.getCategory() != null) {
            mInsertStatement.bindLong(WordsColumns.CATEGORY_FK_POSITION, entity.getCategory().getId());
        } else {
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

        if (entity.getDefinition() != null) {
            values.put(WordsColumns.DEFINITION_FK, entity.getDefinition().getId());
        } else {
            values.putNull(WordsColumns.DEFINITION_FK);
        }

        values.put(WordsColumns.LESSON_FK, entity.getLessonId());

        if (entity.getPartsOfSpeech() != null) {
            values.put(WordsColumns.PART_OF_SPEECH_FK, entity.getPartsOfSpeech().getId());
        } else {
            values.putNull(WordsColumns.PART_OF_SPEECH_FK);
        }

        if (entity.getCategory() != null) {
            values.put(WordsColumns.CATEGORY_FK, entity.getCategory().getId());
        } else {
            values.putNull(WordsColumns.CATEGORY_FK);
        }

        values.put(WordsColumns.DIFFICULT, entity.getDifficult());
        values.put(WordsColumns.MASTER_LEVEL, entity.getMasterLevel());

        long selected = entity.isSelected() ? 1 : 0;
        values.put(WordsColumns.SELECTED, selected);

        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(WordsTable.TABLE_NAME, values, WHERE_ID, whereArguments);

    }

    @Override
    public void delete(Word entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            mDb.delete(WordsTable.TABLE_NAME, WHERE_ID, whereArguments);
        }
    }

    @Override
    public Word get(long id) {
        Word word = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        StringBuilder queryBuilder = new StringBuilder(SELECT_STATEMENT);
        queryBuilder.append(" WHERE W." + WordsColumns.ID + " = ?");
        Cursor cursor = mDb.rawQuery(queryBuilder.toString(), whereArguments);
        if (cursor.moveToFirst()) {
            WordCreator wordCreator = new WordCreator();
            word = wordCreator.createFromCursor(cursor);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if(BuildConfig.DEBUG)
        {
            assert cursor.isClosed();
        }
        return word;
    }

   /* @Override
    public List<Word> getAll(boolean distinct,String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {

        List<Word> wordsList = new ArrayList<>();

        Cursor cursor = mDb.query(distinct, WordsTable.TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            Word word;
            WordCreator wordCreator = new WordCreator();
            do {
                word = wordCreator.createFromCursor(cursor);
                if (word != null) {
                    wordsList.add(word);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        assert cursor.isClosed();

        return wordsList;
    }*/

    @Override
    public List<Word> getAll(boolean distinct,String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {

        List<Word> wordsList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        if(distinct)
        {
            queryBuilder.append("DISTINCT ");
        }
        queryBuilder.append(SELECT_STATEMENT);
        if(selection != null)
        {
            queryBuilder.append(" WHERE ").append(selection);
        }
        if(groupBy != null)
        {
            queryBuilder.append(" GROUP BY ").append(groupBy);
        }
        if(having != null)
        {
            queryBuilder.append(" HAVING ").append(having);
        }
        if(orderBy != null)
        {
            queryBuilder.append(" ORDER BY ").append(orderBy);
        }
        if(limit != null)
        {
            queryBuilder.append(" LIMIT ").append(limit);
        }
        Cursor cursor = mDb.rawQuery(queryBuilder.toString(), selectionArgs);
        if (cursor.moveToFirst()) {
            Word word;
            WordCreator wordCreator = new WordCreator();
            do {
                word = wordCreator.createFromCursor(cursor);
                if (word != null) {
                    wordsList.add(word);
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        assert cursor.isClosed();

        return wordsList;
    }

}
