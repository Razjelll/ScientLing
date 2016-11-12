package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class VocabularySet {

    private long id;
    private String name;
    private Language language;

    public VocabularySet(){}
    public VocabularySet(long id )
    {
        this.id= id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
