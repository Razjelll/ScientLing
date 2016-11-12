package com.dyszlewskiR.edu.scientling.data.database.dao;

import java.util.List;

/**
 * Interfejs obiektów IDao, które ukrywają SQL-owe instrukcje.
 */

public interface IDao<T> {
    long save(T entity);
    void update(T entity);
    void delete(T entity);
    T get(long id);
    List<T> getAll();
}
