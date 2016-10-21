package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Razjelll on 21.10.2016.
 */

public class Lesson implements Parcelable{

    private long mLessonId;
    private String mLessonTopic;
    private String mLessonDescription;
    private ArrayList<Word> mWords;

    public Lesson(long id, String topic, String description, ArrayList<Word> words) {
        mLessonId = id;
        mLessonTopic = topic;
        mLessonDescription = description;
        mWords = words;
    }

    protected Lesson(Parcel in) {
        mLessonId = in.readLong();
        mLessonTopic = in.readString();
        mLessonDescription = in.readString();
    }

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

    public long getId() {return mLessonId;}

    public String getTopic() {return mLessonTopic;}

    public String getDescription() {return mLessonDescription;}

    public ArrayList<Word> getWords() {return mWords;}

    public Word getWord(int position) {return mWords.get(position);}

    public void setId(long id) {mLessonId = id;}

    public void setTopic(String topic) {mLessonTopic = topic;}

    public void setDescription(String description) {mLessonDescription = description;}

    public void setWords(ArrayList<Word> words) {mWords = words;}

    public void addWord(Word word) {mWords.add(word);}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mLessonId);
        dest.writeString(mLessonTopic);
        dest.writeString(mLessonDescription);
    }
}
