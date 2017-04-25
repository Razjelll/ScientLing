package com.dyszlewskiR.edu.scientling.data.models.models;


import java.util.Date;

/**
 * Created by Razjelll on 12.04.2017.
 */

public class SetItem {
    private long mId;
    private String mName;
    private String mLanguageL1;
    private String mLanguageL2;
    private int mWordsCount;
    private String mDescription;
    private String mAuthor;
    private int mBasicSize;
    private float mRating;
    private int mDownloads;
    private int mImagesSize;
    private int mRecordsSize;
    private Date mAddedDate;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLanguageL1() {
        return mLanguageL1;
    }

    public void setLanguageL1(String language) {
        mLanguageL1 = language;
    }

    public String getLanguageL2() {
        return mLanguageL2;
    }

    public void setLanguageL2(String language) {
        mLanguageL2 = language;
    }

    public int getWordsCount() {
        return mWordsCount;
    }

    public void setWordsCount(int count) {
        mWordsCount = count;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public int getBasicSize() {
        return mBasicSize;
    }

    public void setBasicSize(int size) {
        mBasicSize = size;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public int getDownloads() {
        return mDownloads;
    }

    public void setDownloads(int downloads) {
        mDownloads = downloads;
    }

    public int getImagesSize() {
        return mImagesSize;
    }

    public void setImagesSize(int size) {
        mImagesSize = size;
    }

    public int getRecordsSize() {
        return mRecordsSize;
    }

    public void setRecordsSize(int size) {
        mRecordsSize = size;
    }

    public Date getAddedDate(){
        return mAddedDate;
    }
    public void setAddedDate(Date date){
        mAddedDate = date;
    }
}
