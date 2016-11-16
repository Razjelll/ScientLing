package com.dyszlewskiR.edu.scientling.data.database;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.BuildConfig;
import com.dyszlewskiR.edu.scientling.data.database.dao.CategoryDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.DefinitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SentenceDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.TranslationDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.utils.ResourcesFileOpener;

import java.util.ArrayList;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.*;
import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.*;

/**
 * Created by Razjelll on 10.11.2016.
 */

public class DataManager {

    private Context mContext;
    private SQLiteDatabase mDb;

    private WordDao mWordDao; //TODO przetestować, gdzie bedzie działało lepiej
    private TranslationDao mTranslationDao;
    private SentenceDao mSentenceDao;
    private DefinitionDao mDefinitionDao;
    private CategoryDao mCategoryDao;

    public DataManager(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);

        if (BuildConfig.DEBUG) //TODO sppawdzić dokładnie jak działa BuildConfig.DEBUG
        {
            dbHelper.setFileOpener(new ResourcesFileOpener());
        }

        mDb = dbHelper.getWritableDatabase();

        mWordDao = new WordDao(mDb);
        mTranslationDao = new TranslationDao(mDb);
        mSentenceDao = new SentenceDao(mDb);
        mDefinitionDao = new DefinitionDao(mDb);
        mCategoryDao = new CategoryDao(mDb);
    }


    public Word getWord(long id) {
        Word word = mWordDao.get(id);
        completeWord(word);
        return word;
    }

    private void completeWord(Word word) {
        if (word != null) {
            ArrayList<Translation> translations = mTranslationDao.getLinked(word.getId());
            if (translations != null) {
                word.setTranslations(translations);
            }
            ArrayList<Sentence> sentences = (ArrayList<Sentence>) mSentenceDao.getLinked(word.getId());
            if (sentences != null) {
                word.setSentences(sentences);
            }
        }
    }

    public long saveWord(Word word) {
        mDb.beginTransaction();

        //zapisywanie definicji
        long definitionId = saveDefinition(word.getDefinition());
        if (definitionId > 0) {
            word.getDefinition().setId(definitionId);
        }
        //zapisywanie kategorii
        // TODO prawdopodobnie lepiej będzie leśli do zapisywania będziemy tworzyć nowe dao
        long categoryId = saveCategory(word.getCategory());
        if (categoryId > 0) {
            word.getCategory().setId(categoryId);
        }

        //Nie istnieje p[otrzeba aby zapisywać PartOfSpeech
        //Zapusywanie tłumaczeń, raczej nie przyjmujemy, że nie będzie przypisanych tłuamczeń do słówka
        assert word.getTranslations() != null;

        long wordId = mWordDao.save(word);
        saveTranslations(word.getTranslations(), wordId);
        saveSentences(word.getSentences(), wordId);

        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return wordId;
    }

    private long saveDefinition(Definition definition) {
        //TODO sprawdzić, czy trzeba sprawdzić czy defninicja istnieje w bazie.
        //Tearaz przyjumujemy, że każda daefinicja jest różna, więc jesli model ma definicje
        //zapisujemy ją
        if (definition != null) {
            long definitionId = mDefinitionDao.save(definition);
            return definitionId;
        }
        return -1;
    }

    private long saveCategory(Category category) {
        if (category != null) {
            //TODO ogarnąć jak powinno to wyglądać
            //sprawdzamy czy kategoria istnieje
            Category existingCategory = mCategoryDao.get(category.getId());
            if (existingCategory == null) {
                long categoryId = mCategoryDao.save(category);
                return categoryId;
            } else {
                return existingCategory.getId();
            }
        }
        return -1;
    }

    private void saveTranslations(ArrayList<Translation> translationsList, long wordId) {
        Translation translation = null;
        for (Translation t : translationsList) {
            translation = mTranslationDao.getByContent(t.getContent());
            long translationId;
            if (t == null) {
                translationId = mTranslationDao.save(translation);
            } else {
                translationId = translation.getId();
            }
            mTranslationDao.link(translationId, wordId);
        }
    }

    public void saveSentences(ArrayList<Sentence> sentencesList, long wordId) {
        Sentence sentence = null;
        for (Sentence s : sentencesList) {
            sentence = mSentenceDao.getByContent(s.getContent());
            long sentenceId;
            if (sentence == null) {
                sentenceId = mSentenceDao.save(s);
            } else {
                sentenceId = sentence.getId();
            }
            mSentenceDao.link(sentenceId, wordId);
        }
    }

    public ArrayList<Word> getQuestions(long set, long lesson, long category, long difficult, int howMuch) {
        assert lesson != -1 || set != -1;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> queryArguments = new ArrayList<>();
        stringBuilder.append(WordsTable.WordsColumns.LESSON_FK);
        if (lesson != -1) //jeśli ograniczamy się do jednej lekcji
        {
            stringBuilder.append(" = ").append("?");
            queryArguments.add(String.valueOf(lesson));
        } else {
            stringBuilder.append("IN (");
            stringBuilder.append(" SELECT ").append("L.").append(LessonsColumns.ID);
            stringBuilder.append(" FROM ").append(LessonsTable.TABLE_NAME).append(" L");
            stringBuilder.append(" JOIN").append(SetsTable.TABLE_NAME).append(" S");
            stringBuilder.append(" ON ").append("L.").append(LessonsColumns.SET_FK);
            stringBuilder.append(" = ").append("S.").append(SetsColumns.ID);
            stringBuilder.append(" WHERE ").append(SetsColumns.ID).append(" = ").append("?");
            stringBuilder.append(")");
            queryArguments.add(String.valueOf(set));
        }
        if (category != -1) {
            stringBuilder.append(" AND ").append(WordsColumns.CATEGORY_FK).append(" = ").append("?");
            queryArguments.add(String.valueOf(category));
        }
        if (difficult != -1) {
            stringBuilder.append(" AND ").append(WordsColumns.DIFFICULT).append(" = ").append("?");
            queryArguments.add(String.valueOf(difficult));
        }

        stringBuilder.append(" AND ").append(WordsColumns.MASTER_LEVEL).append("<0");

        String where = stringBuilder.toString();
        String[] whereArguments = queryArguments.toArray(new String[0]);
        String limit = String.valueOf(howMuch);

        List<Word> words = mWordDao.getAll(false, WordsTable.getColumns(), where, whereArguments,
                null, null, null, limit); //TODO chyba dorobić random
        for (Word word : words) {
            completeWord(word);
        }

        return (ArrayList<Word>) words;
    }

    /**
     * Metoda wyszukująca słówka które posłużą za odpowiedzi w teście. Metoda powinna zwracać o jedno
     * słówko mniej, niż jest możliwych odpowiedzi w teście, ponieważ jedno słówko będzie prawidłowe.
     * Metoda zwraca obiekty posiadające tylko numer identyfikacyjny i słówko. Numer identyfikacyjny
     * potrzebny jest w razie, kiedy użytkownik odpowie źle i będzie chciał sprawdzić tłumaczenie
     * słówka które wskazał. Wtedy na podstawie numeru identyfikacyjnego będzie można pobrać
     * tłumaczenia z bazy(niezaimplementowane w tej wersji).
     * Metoda wybiera słówka z konkretnego zestawu. Może istnieć problem, gdy zestaw ma zbyt mało
     * słowek, ale bedzie to dotyczyło tylko początkowego stadium towrzenia zestawu przez użytkownika.
     * Dlatego postanowiono nie wyszukiwać słówek z zakresu wszystkich słowek w bazie w danym języku.
     * Metoda wyszukuj losowe słowka dzięki wykorzystaniu funkcji RANDOM umieszczonej w klauzuli
     * ORDER BY.
     * Metoda wyszukuje słówka w L2
     * Zazwyczaj słówka będą wybierane z tego samego zestawu, ale moze istnieć sytuacja,
     * kiedy w danym zestawie jest zbyt mała liczba słówek Nie mam pojęcia
     *
     * @param set           numer identyfikacyjny zbioruz którego pochodząszukane słówka
     * @param category      kategoria z jakiej mają być szukane słówka
     * @param difficult     poziom trudności jaki mają mieć szukane słówka
     * @param howMuch       ile słówek należy wyszukać
     * @param differentFrom słówka jakie mają nie być wyszukiwane
     * @return
     */
    public ArrayList<Word> getAnswersL2(long set, long category, long difficult, int howMuch, String[] differentFrom) {
        assert (difficult >= -1 && difficult <= 5) && difficult != 0;

        StringBuilder queryBuilder = new StringBuilder();
        /*  SELECT id, content
            FROM Words
            WHERE lesson_fk IN (
                SELECT id
                FROM Lessons
                WHERE set_fk = ?
            )
         */
        queryBuilder.append(WordsColumns.LESSON_FK).append(" IN (");
        queryBuilder.append(" SELECT ").append(LessonsColumns.ID);
        queryBuilder.append(" FROM ").append(LessonsTable.TABLE_NAME);
        queryBuilder.append(" WHERE ").append(LessonsColumns.SET_FK).append(" = ").append("?");
        queryBuilder.append(" ) ");
        ArrayList<String> queryArguments = new ArrayList<>(); //TODO można wstawić wartość od razu do zapytania
        queryArguments.add(String.valueOf(String.valueOf(set)));
        if (category > 0) {
            queryBuilder.append(" AND ").append(WordsColumns.CATEGORY_FK).append(" = ").append("?");
            queryArguments.add(String.valueOf(category));
        }
        if (difficult > 0) {
            queryBuilder.append(" AND ").append(WordsColumns.DIFFICULT).append(" = ").append("?");
            queryArguments.add(String.valueOf(difficult));
        }
        for (int i = 0; i < differentFrom.length; ++i) {
            if (differentFrom[i].equals("")) {
                queryBuilder.append(" AND ").append(WordsColumns.CONTENT).append("<>").append("?");
                queryArguments.add(differentFrom[i]);
            }
        }

        String orderBy = "RANDOM()";
        ArrayList<Word> wordsList = mWordDao.getSimpleWords(queryBuilder.toString(),
                queryArguments.toArray(new String[0]), orderBy, String.valueOf(howMuch));


        return wordsList;
    }

    public ArrayList<Translation> getAnswersL1(long set, long category, long difficult, int howMuch, String[] differentFrom) {
        //TODO Refaktoryzacja
        /*
            id IN (
                SELECT WT.translation_fk
                FROM WordsTranslation WT LEFT OUTER JOIN Words W ON WT.word_fk = W.id
                    LEFT OUTER JOIN Lessons LE ON W.lesson_fk = LE.id
                    WHERE LE.set_fk = ?
             )
         */
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(WordsColumns.ID).append(" IN (")
                .append(" SELECT ").append(WordsTranslationsTable.ALIAS_DOT)
                .append(WordsTranslationsTable.WordsTranslationsColumns.TRANSLATION_FK)
                .append(" FROM ").append(WordsTranslationsTable.TABLE_NAME).append(" ")
                .append(WordsTranslationsTable.ALIAS).append(" LEFT OUTER JOIN ")
                .append(WordsTable.TABLE_NAME).append(" ").append(WordsTable.ALIAS)
                .append(" ON ").append(WordsTranslationsTable.ALIAS_DOT)
                .append(WordsTranslationsTable.WordsTranslationsColumns.WORD_FK)
                .append(" = ").append(WordsTable.ALIAS_DOT).append(WordsColumns.ID)
                .append(" LEFT OUTER JOIN ").append(LessonsTable.TABLE_NAME).append(" ")
                .append(LessonsTable.ALIAS).append(" ON ").append(WordsTable.ALIAS_DOT)
                .append(WordsColumns.LESSON_FK).append(" = ").append(LessonsTable.ALIAS_DOT)
                .append(LessonsColumns.ID).append(" WHERE ").append(LessonsTable.ALIAS_DOT)
                .append(LessonsColumns.SET_FK).append(" = ?"); //TODO można argumenty wrzucić prosto do zapytania
        ArrayList<String> queryArguments = new ArrayList<>();
        queryArguments.add(String.valueOf(set));
        if (category > 0) {
            queryBuilder.append(" AND ").append(WordsTable.ALIAS_DOT).append(WordsColumns.CATEGORY_FK)
                    .append(" = ").append("?");
            queryArguments.add(String.valueOf(category));
        }
        if (difficult > 0) {
            queryBuilder.append(" AND ").append(WordsTable.ALIAS_DOT).append(WordsColumns.DIFFICULT)
                    .append(" = ").append("?");
            queryArguments.add(String.valueOf(difficult));
        }
        if(differentFrom !=null)
        {
            for (String s : differentFrom) {
                queryBuilder.append(" AND ").append(WordsTable.ALIAS_DOT).append(WordsColumns.CONTENT)
                        .append("<>").append("?");
                queryArguments.add(String.valueOf(s));
            }
        }
        queryBuilder.append(")");// zamknięcie zapytania wewnętrzenego
        ArrayList<Translation> translationsList = (ArrayList<Translation>) mTranslationDao.getAll(false, TranslationsTable.getColumns(),
                queryBuilder.toString(), queryArguments.toArray(new String[0]), null, null, null, String.valueOf(howMuch));
        return translationsList;
    }


}
