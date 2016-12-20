package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Lesson implements Parcelable {

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
    private long id;
    private long number;
    private String name;
    private VocabularySet set;
    private ArrayList<Exercise> possibleExercises;
    private ArrayList<Word> includedWords;
    private int progress;

    public Lesson() {
    }

    public Lesson(long id) {
        this.id = id;
    }

    protected Lesson(Parcel in) {
        id = in.readLong();
        number = in.readLong();
        name = in.readString();
        set = in.readParcelable(VocabularySet.class.getClassLoader());
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

    public int getProgress() {return progress;}

    public void setProgress(int progress) { this.progress = progress;}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(number);
        dest.writeString(name);
        dest.writeParcelable(set, flags);
    }
    //TODO
}
