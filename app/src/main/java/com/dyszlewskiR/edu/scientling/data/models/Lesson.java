package com.dyszlewskiR.edu.scientling.data.models;

import java.util.ArrayList;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Lesson {

    private long id;
    private long number;
    private String name;
    private VocabularySet set;
    private ArrayList<Exercise> possibleExercises;
    private ArrayList<Word> includedWords;


    public Lesson(){}
    public Lesson(long id)
    {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getPossibleExercises() {
        return possibleExercises;
    }

    public void setPossibleExercises(ArrayList<Exercise> possibleExercises) {
        this.possibleExercises = possibleExercises;
    }

    public ArrayList<Word> getIncludedWords() {
        return includedWords;
    }

    public void setIncludedWords(ArrayList<Word> includedWords) {
        this.includedWords = includedWords;
    }

    /*public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }*/

    public VocabularySet getSet() {
        return set;
    }

    public void setSet(VocabularySet set) {
        this.set = set;
    }
    //TODO
}
