package com.dyszlewskiR.edu.scientling.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.database.dao.CategoryDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.DefinitionDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.HintDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LanguageDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.LessonDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SentenceDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.SetDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.TranslationDao;
import com.dyszlewskiR.edu.scientling.data.database.dao.WordDao;
import com.dyszlewskiR.edu.scientling.data.database.tables.CategoriesTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.TranslationsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable;
import com.dyszlewskiR.edu.scientling.data.database.tables.WordsTranslationsTable;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.Hint;
import com.dyszlewskiR.edu.scientling.data.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dyszlewskiR.edu.scientling.data.database.tables.LessonsTable.LessonsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.SetsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.WordsTable.WordsColumns;

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
    private SetDao mSetDao;


    public DataManager(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);

        /*if (BuildConfig.DEBUG) //TODO sppawdzić dokładnie jak działa BuildConfig.DEBUG
        {
            dbHelper.setFileOpener(new ResourcesFileOpener());
        }*/

        mDb = dbHelper.getWritableDatabase();

        mWordDao = new WordDao(mDb);
        mTranslationDao = new TranslationDao(mDb);
        mSentenceDao = new SentenceDao(mDb);
        mDefinitionDao = new DefinitionDao(mDb);
        mCategoryDao = new CategoryDao(mDb);
        mSetDao = new SetDao(mDb);
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
            HintDao hintDao = new HintDao(mDb);
            ArrayList<Hint> hints = (ArrayList<Hint>)hintDao.getLinked(word.getId());
            if(hints != null)
            {
                word.setHints(hints);
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
        if(word.getSentences() != null)
        {
            saveSentences(word.getSentences(), wordId);
        }


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
        //TODO pozmieniać nazwy translation i t
        Translation existentTranslation = null;
        for (Translation translation : translationsList) {
            existentTranslation= mTranslationDao.getByContent(translation.getContent());
            long translationId;
            if (existentTranslation == null) {
                translationId = mTranslationDao.save(translation);
            } else {
                translationId = existentTranslation.getId();
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

    public List<Word> getQuestions(long set, long lesson, long category, long difficult, int howMuch) {
        assert lesson != -1 || set != -1;
        StringBuilder stringBuilder = new StringBuilder();
        List<String> queryArguments = new ArrayList<>();
        stringBuilder.append(WordsTable.WordsColumns.LESSON_FK);
        if (lesson != -1) //jeśli ograniczamy się do jednej lekcji
        {
            stringBuilder.append(" = ").append("?");
            queryArguments.add(String.valueOf(lesson));
        } else {
            stringBuilder.append(" IN (");
            stringBuilder.append(" SELECT ").append("L.").append(LessonsColumns.ID);
            stringBuilder.append(" FROM ").append(LessonsTable.TABLE_NAME).append(" L");
            stringBuilder.append(" JOIN ").append(SetsTable.TABLE_NAME).append(" S");
            stringBuilder.append(" ON ").append("L.").append(LessonsColumns.SET_FK);
            stringBuilder.append(" = ").append("S.").append(SetsColumns.ID);
            stringBuilder.append(" WHERE ").append("S.").append(SetsColumns.ID).append(" = ").append("?");
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
                "RANDOM()", null, null, limit); //TODO chyba dorobić random
        for (Word word : words) {
            completeWord(word);
        }

        return (List<Word>) words;
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
    public List<Word> getAnswersL2(long set, long category, long difficult, int howMuch, String[] differentFrom) {
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
        List<Word> wordsList = mWordDao.getSimpleWords(queryBuilder.toString(),
                queryArguments.toArray(new String[0]), orderBy, String.valueOf(howMuch));

        return wordsList;
    }

    public List<Translation> getAnswersL1(long set, long category, long difficult, int howMuch, String[] differentFrom) {
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
        if (differentFrom != null) {
            for (String s : differentFrom) {
                queryBuilder.append(" AND ").append(WordsTable.ALIAS_DOT).append(WordsColumns.CONTENT)
                        .append("<>").append("?");
                queryArguments.add(String.valueOf(s));
            }
        }
        queryBuilder.append(")");// zamknięcie zapytania wewnętrzenego
        List<Translation> translationsList = mTranslationDao.getAll(false, TranslationsTable.getColumns(),
                queryBuilder.toString(), queryArguments.toArray(new String[0]), null, null, null, String.valueOf(howMuch));
        return translationsList;
    }

    public List<VocabularySet> getSets() {
        List<VocabularySet> sets = mSetDao.getAll();
        //TODO niezbyt dobre rozwiązanie, chyba lepiej będzie pobierać bezpośrednio w zapytaniu języki
        //tutaj weźmiemy stworzymy obiekt lokalnie, ponieważ raczej nie ma potrzeby przechowywać to w pamięci
        LanguageDao languageDao = new LanguageDao(mDb);
        for (VocabularySet s : sets) {
            s.setLanguageL2(languageDao.get(s.getLanguageL2().getId()));
        }
        return sets;
    }

    public VocabularySet getSetById(long id) {
        return mSetDao.get(id);
    }

    public List<Category> getCategories() {
        return mCategoryDao.getAll();
    }

    public List<Category> getCategoriesByLanguage(long languageId) {

        String where = CategoriesTable.CategoriesColumns.LANGUAGE_FK + "=?";
        String[] whereArguments = new String[]{String.valueOf(languageId)};
        return mCategoryDao.getAll(true, CategoriesTable.getColumn(), where, whereArguments,
                null, null, CategoriesTable.CategoriesColumns.NAME, null);
    }

    public List<Language> getLanguages() {
        LanguageDao dao = new LanguageDao(mDb);
        return dao.getAll();
    }

    public long saveSet(VocabularySet set) {
        mDb.beginTransaction();
        long id = mSetDao.save(set);
        if (id > 0) {
            mDb.setTransactionSuccessful();
        }
        mDb.endTransaction();
        return id;
    }

    public List<Lesson> getLessons(VocabularySet set) {
        LessonDao lessonDao = new LessonDao(mDb);
        if (set == null) {
            return lessonDao.getAll();
        }
        String where = LessonsColumns.SET_FK + " = ?";
        String orderBy = LessonsColumns.NUMBER;
        return lessonDao.getAll(true, LessonsTable.getColumns(), where, new String[]{String.valueOf(set.getId())}, null, null, orderBy, null);
    }

    public List<Lesson> getLessonsWithProgress(VocabularySet set)
    {
        List<Lesson> lessons = new ArrayList<>();
        LessonDao lessonDao = new LessonDao(mDb);
        if(set == null) return lessons;
        ArrayList<String> queryColumns  = new ArrayList<>(Arrays.asList(LessonsTable.getColumns()));
        String queryPart = "(SELECT COUNT(1) FROM " + WordsTable.TABLE_NAME
                + " WHERE " + WordsColumns.LESSON_FK + " = " + LessonsTable.TABLE_NAME +"."+ LessonsColumns.ID;
        String progressColumn = queryPart + " AND " + WordsColumns.MASTER_LEVEL + "> -1 )*1.0/"
                + queryPart + ")*100";
        queryColumns.add(progressColumn);

        String where = LessonsColumns.SET_FK + " =?";
        String[] whereArguments = {String.valueOf(set.getId())};
        String groupBy = LessonsColumns.ID;
        String orderBy = LessonsColumns.NUMBER;
        lessons = lessonDao.getAll(false,queryColumns.toArray(new String[0]),where, whereArguments,
                groupBy,null,orderBy,null);
        return lessons;
    }

    public long saveLesson(Lesson lesson) {
        LessonDao dao = new LessonDao(mDb);
        mDb.beginTransaction();
        long id = dao.save(lesson);
        if (id > 0) {
            mDb.setTransactionSuccessful();
        }
        mDb.endTransaction();
        return id;
    }



    public List<Word> getAllWords()
    {
        List<Word> words = mWordDao.getAll();
        for(Word word : words)
        {
            completeWord(word);
        }
        return words;
    }

    public void release() {
        mDb.close();
    }

}
