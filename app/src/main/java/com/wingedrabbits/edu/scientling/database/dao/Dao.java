package com.wingedrabbits.edu.scientling.database.dao;

import java.util.List;

/**
 * Interfejs obiektów Dao, które ukrywają SQL-owe instrukcje.
 */

public interface Dao<T> {
    long save(T type);
    void update(T type);
    void delete(T type);
    T get(long id);
    List<T> getAll();
}
