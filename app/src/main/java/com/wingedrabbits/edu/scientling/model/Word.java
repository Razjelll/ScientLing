package com.wingedrabbits.edu.scientling.model;

import java.util.ArrayList;

/**
 * Model słówka
 */

public class Word {

    /**Number identyfikacyjny słówka*/
    private long wordId;
    /**Słówko*/
    private String word;
    /**Wymowa słówka*/
    private String  pronunciation;
    /**Identyfikator typu słówka*/
    private long typeId;
    /**Typ słówka*/
    private String type;
    /**Identyfikator kategorii słowka*/
    private long categoryId;
    /**Kategoria słówka*/
    private String category;
    /**Procent opanowania słówka*/
    private int masterLevel;
    /**Ilość powtórek słówka*/
    private int repetitions;
    /**Lista tłumaczeń danego słowka*/
    private ArrayList<String> translations;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(int masterLevel) {
        this.masterLevel = masterLevel;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getId() {
        return wordId;
    }

    public void setId(long wordId) {
        this.wordId = wordId;
    }
}
