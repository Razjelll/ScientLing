package com.wingedrabbits.edu.scientling.model;

/**
 * Created by Razjelll on 17.10.2016.
 */

public class Category {

    /**Numer identyfikacyjny kategorii*/
    private long categoryId;
    /**Nazwa kategorii*/
    private String categoryName;

    public Category(long id, String name)
    {
        categoryId = id;
        categoryName = name;
    }

    public long getId()
    {
        return categoryId;
    }

    public String getName()
    {
        return categoryName;
    }

    public void setName(String name)
    {
        categoryName = name;
    }
}
