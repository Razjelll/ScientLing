package com.wingedrabbits.edu.scientling.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Klasa służąca do otwierania plików bazy danych. Klasa sprawdza, czy plik istnieje,
 * jeśli tak otwiera go, jeśli nie tworzy nowy
 *
 * @author Roman Dyszlewski
 * @version 0.05
 */

public class SqlOpenHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "scienceling.sqlite";
    public static final int VERSION =1;

    /**
     * Kontruktor klasy SqlOpenHelper
     * @param context kontekst aplikacji
     */
    public SqlOpenHelper(Context context)
    {
        super(context, DBNAME, null, VERSION);
    }

    /**
     * Metoda służy do tworzenia, a także do zapełnienia bazy danych.
     * Metoda wywoywana jest w momencie, gdy odwoujemy się do bazy danych, ktora jeszcze nie istnieje.
     * @param db baza sqlite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createDatabase(SQLiteDatabase db)
    {

    }
}
