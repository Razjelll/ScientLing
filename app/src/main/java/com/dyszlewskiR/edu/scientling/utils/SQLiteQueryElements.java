package com.dyszlewskiR.edu.scientling.utils;

/**
 * Klasa zawierajace elementy zapytań sqlite. Klasa zawiera stałe stayczne, zawierajace potrzebne fragmenty zapytania. Klasa ma na celu zminimalizowanie
 * możliwości popełnienia błędu podczas wpisywania tych wartości ręcznie przy kazdym zapytaniu. Błąd w składni zapytania podczas programowania Android moze
 * być trudny do zlokalizowania. Najczęściej występuje w wyniku braku odstępu lub niepoprawnie napisanej klauzuli. Wszystkie elementy występujace w środku
 * zapytania mają spacje na początku i na końcu, aby nie było trzeba wstawiać tychdstępów ręcznie, ponieważ w javie jest to dość niewygodne.
 */

public  class SQLiteQueryElements {

    // Typy danych występujące w SQLite
    public static final String TEXT = " TEXT ";
    public static final String INTEGER = " INTEGER ";
    public static final String REAL = " REAL ";
    public static final String BLOB = " BLOB ";

    //
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String DROP_TABLE = "DROP TABLE ";
    public static final String IF_EXISTS = " IF EXISTS ";
    public static final String DROP_TABLE_IF_EXISTS = DROP_TABLE+IF_EXISTS;
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final String FOREIGN_KEY = " FOREIGN KEY ";
    public static final String REFERENCE = " REFERENCE ";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String CHECK = " CHECK ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String NULL = " NULL ";
    public static final String UNIQUE = " UNIQUE ";
}
