package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.Translation;

import java.util.ArrayList;
import java.util.List;
import static com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable.*;
import static  com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable.TABLE_NAME;

/**
 * Created by Razjelll on 07.11.2016.
 */

public class TranslationDao extends BaseDao<Translation> {



    private final String INSERT_STATEMENT =
            "INSERT INTO " + TranslationsTable.TABLE_NAME + "("
            +TranslationsColumns.TRANSLATION + ") VALUES (?)";
    private final String SELECT_LINK_STATEMENT =
            "SELECT T.* FROM " + WordsTranslationsTable.TABLE_NAME
            + " WT JOIN "+ TranslationsTable.TABLE_NAME + " T ON WT."
            + WordsTranslationsColumns.TRANSLATION_FK +" = T." + TranslationsColumns.ID
            + " WHERE WT." +WordsTranslationsColumns.WORD_FK + " = ?";
    private final String WHERE_ID = TranslationsColumns.ID + " = ?";

    public TranslationDao(SQLiteDatabase db)
    {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = TranslationsTable.getColumns();
    }

    @Override
    public long save(Translation entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(1, entity.getTranslation());
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(Translation entity) {
        final ContentValues values = new ContentValues();
        values.put(TranslationsColumns.TRANSLATION, entity.getTranslation());
        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TranslationsTable.TABLE_NAME,values, WHERE_ID, whereArguments);
    }

    @Override
    public void delete(Translation entity) {
        if(entity != null)
        {
            long id = entity.getId();
            if(id > 0)
            {
                String[] whereAttributes = new String[]{String.valueOf(id)};
                mDb.delete(TranslationsTable.TABLE_NAME, WHERE_ID, whereAttributes);
            }
        }
    }

    @Override
    public Translation get(long id){
        Translation translation = null;
        String[] whereAttributes = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TranslationsTable.TABLE_NAME, mTableColumns, WHERE_ID, whereAttributes,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            translation = this.buildTranslationFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translation;
    }

    private Translation buildTranslationFromCursor(Cursor cursor)
    {
        Translation translation = null;
        if(cursor != null)
        {
            translation = new Translation();
            translation.setId(cursor.getLong(TranslationsColumns.ID_POSITION));
            translation.setTranslation(cursor.getString(TranslationsColumns.TRANSLATION_POSITION));
        }
        return translation;
    }

    @Override
    public List<Translation> getAll() {
        List<Translation> translationsList = new ArrayList<>();
        Cursor cursor = mDb.query(TranslationsTable.TABLE_NAME, mTableColumns, null, null,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst())
        {
            Translation translation = null;
            do{
                translation = buildTranslationFromCursor(cursor);
                if(translation != null)
                {
                    translationsList.add(translation);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translationsList;
    }

    public Translation getByContent(String content) //TODO też zastanowić się nad nazwą i zmianą nazwy kolumny
    {
        Translation translation = null;
        String where  = TranslationsColumns.TRANSLATION + " =?";
        String[] whereArguments = new String[]{content};
        Cursor cursor = mDb.query(TranslationsTable.TABLE_NAME, mTableColumns, where, whereArguments,
                mGroupBy,mHaving,mOrderBy,mLimit);
        if(cursor.moveToFirst()) {
            translation = buildTranslationFromCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translation;
    }

    //TUTAJ ZNAJDUJĄ SIE METODY , KTÓRE OBSLUGUJA TABLE WORDSTRANSLATION

    public void link(long translationId, long wordId)
    {
       final ContentValues values = new ContentValues();
        values.put(WordsTranslationsColumns.WORD_FK, wordId);
        values.put(WordsTranslationsColumns.TRANSLATION_FK, translationId);
        mDb.insert(WordsTranslationsTable.TABLE_NAME, null, values);
    }

    /**
     * Metoda, która usuwa z tabeli WordsTranslations wszystkie rekordy, gdzie wartość
     * kolumny word_fk jest równa wordID. Przujęto takie rozwiązanie, z powodu implementacji
     * wprowadzania tłumaczeń danego słowka do aplikacji. Gdy użytkownik bedzie wprowadzał
     * słówko do wpisywania tłumaczeń będzie tylko jedno pole tekstowe. Każde tłumaczenie będzie
     * trzeba odzielić pewnym znakiem. Takie podejście wynika z niewielkich ekranów w telefonach
     * z systemm Android, na których nie zmieści się zbyt wiele informacji. Jeśli chcielibyśmy
     * wprowadzać każde tłumaczenie do odzielnego pola tekstowego, zajmowałoby to wiele miejsca
     * na ekranie, dodatkowo wymagany by był przyciesk, który dodawałby kolejne pole do ekranu.
     * Przy pierwszym dodawaniu słówka dodawane są tkaże wszystkie tłumaczenia. Przy aktualizacji
     * tłumaczeń słówka byłoby ciężko rozpoznać, które tłumaczenie słówka użytkownik zmienił.
     * Było by trzeba dokonywać skomlikowanych operacji, aby się tego dowiedzieć,/ Lepszym
     * rozwiązaniem jest usunięcie wszystkich tłumaczeń słowka powiązanego z słówkiem
     * a następnie wprowadzenie wszystkich, które znajdują się w zedytowanym polu użytkownika.
     * @param wordId
     */
    public void unLink(long wordId)
    {
        String where = WordsTranslationsColumns.WORD_FK +" =?";
        String[] whereArguments = new String[] {String.valueOf(wordId)};
        mDb.delete(WordsTranslationsTable.TABLE_NAME, where, whereArguments);
    }

    /**
     * Metoda zwracajaca wszystkie tłumaczenia do danego słówka. Metoda operauje na tablicy
     * WordsTranslations. Metoda dokonuje złączenia WordsTranslations z Translations, otrzymujace\
     * w ten sposób danych tłumaczeń powiązanych z danym słówkiem.
     * @param wordId numer identyfikacyjny słowka do którego chemy uzyskać tłumaczenia
     * @return lista obiektów modelu Translation które są powiązane ze słówkiem
     */
    public ArrayList<Translation> getLinked(long wordId)
    {
        ArrayList<Translation> translationsList = new ArrayList<>();
        String[] whereArguments = new String[]{String.valueOf(wordId)};
        Cursor cursor = mDb.rawQuery(SELECT_LINK_STATEMENT,whereArguments);
        if(cursor.moveToFirst())
        {
            Translation translation = null;
            do {
                translation = buildTranslationFromCursor(cursor);
                if(translation != null)
                {
                    translationsList.add(translation);
                }
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        return translationsList;
    }

    public void deleteUnlinked()
    {
        String statement = "DELETE FROM "
                +TABLE_NAME + "WHERE " + TranslationsColumns.ID + "IS NO IN ("
                + "SELECT "  + WordsTranslationsColumns.TRANSLATION_FK+ " FROM "
                + WordsTranslationsTable.TABLE_NAME;
        mDb.execSQL(statement);
    }



}
