package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 21.10.2016.
 */

public class Sentence implements Parcelable{

    private long mSentenceId;
    private String mSentenceContent;
    private String mSentenceTranslation;

    public Sentence(long id, String sentence, String translation) {
        mSentenceId = id;
        mSentenceContent = sentence;
        mSentenceTranslation = translation;
    }

    protected Sentence(Parcel in) {
        mSentenceId = in.readLong();
        mSentenceContent = in.readString();
        mSentenceTranslation = in.readString();
    }

    public long getId() {return mSentenceId;}

    public String getContent() {return mSentenceContent;}

    public String getTranslation() {return mSentenceTranslation;}

    public void setId(long id) {mSentenceId = id;}

    public void setContent(String content) {mSentenceContent = content;}

    public void setTranslation(String translation) {mSentenceTranslation = translation; }

    public static final Creator<Sentence> CREATOR = new Creator<Sentence>() {
        @Override
        public Sentence createFromParcel(Parcel in) {
            return new Sentence(in);
        }

        @Override
        public Sentence[] newArray(int size) {
            return new Sentence[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mSentenceId);
        dest.writeString(mSentenceContent);
        dest.writeString(mSentenceTranslation);
    }
}
