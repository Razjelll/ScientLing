package com.wingedrabbits.edu.scientling.model;

import android.hardware.SensorEvent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Model słówka
 */

public class Word implements Parcelable{

    private long mWordId;
    private String mWordContent;
    private String mPronunciation;
    private Type mWordType;
    private Category mWordCategory;
    private int mMasterLevel;
    private int mRepetitionsCount;
    private ArrayList<Meaning> mTranslations;
    private ArrayList<Word> mSynonimes;
    private ArrayList<Sentence> mSentences;
    private ArrayList<Mnemonics> mMneminics;

    public Word()
    {

    }

    public Word(long id, String content)
    {
        mWordId = id;
        mWordContent = content;
    }

    public Word(long id, String content, String pronunciation, Type type, Category category, int masterLevel , int repetitionsCount )
    {
        mWordId = id;
        mWordContent = content;
        mPronunciation = pronunciation;
        mWordType = type;
        mWordCategory = category;
        mMasterLevel = masterLevel;
        mRepetitionsCount = repetitionsCount;
    }
    //TODO przyjrzeć się konstruktorom

    protected Word(Parcel in) {
        mWordId = in.readLong();
        mWordContent = in.readString();
        mPronunciation = in.readString();
        mWordType = in.readParcelable(Type.class.getClassLoader());
        mWordCategory = in.readParcelable(Category.class.getClassLoader());
        mMasterLevel = in.readInt();
        mRepetitionsCount = in.readInt();
        mTranslations = in.createTypedArrayList(Meaning.CREATOR);
        mSynonimes = in.createTypedArrayList(Word.CREATOR);
        mSentences = in.createTypedArrayList(Sentence.CREATOR);
        mMneminics = in.createTypedArrayList(Mnemonics.CREATOR);
    }

    public long getId() {return mWordId;}

    public String getContent() {return mWordContent;}

    public String getPronunciation() {return mPronunciation;}

    public Type getType() {return mWordType;}

    public Category getCategory() {return mWordCategory;}

    public int getMasterLevel() {return mMasterLevel;}

    public int getRepetitionsCount() {return mRepetitionsCount;}

    public ArrayList<Meaning> getTranslations() {return mTranslations;}

    public ArrayList<Word> getSynonimes() {return mSynonimes;}

    public ArrayList<Sentence> getmSentences() {return mSentences;}

    public ArrayList<Mnemonics> getMneminics() {return mMneminics;}

    public Meaning getMeaning(int position) {return mTranslations.get(position);}

    public Word getSynonime(int position) {return mSynonimes.get(position);}

    public Sentence getSentence(int position) {return mSentences.get(position);}

    public Mnemonics getMnemonic(int position) {return mMneminics.get(position);}


    public void setId(long id) {mWordId = id;}

    public void setContent(String content) {mWordContent = content;}

    public void setPronunciation(String pronunciation) {mPronunciation = pronunciation;}

    public void setType(Type type) {mWordType = type;}

    public void setCategory(Category category) {mWordCategory = category;}

    public void setMasterLevel(int masterLevel) {mMasterLevel = masterLevel;}

    public void setRepetitionsCount(int repetitions) {mRepetitionsCount = repetitions;}

    public void setTranslations(ArrayList<Meaning> translations) { mTranslations = translations;}

    public void setSynonimes(ArrayList<Word> synonimes) {mSynonimes = synonimes;}

    public void setSentences(ArrayList<Sentence> sentences) {mSentences = sentences;}

    public void setMneminics(ArrayList<Mnemonics> mneminics) {mMneminics = mneminics;}

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mWordId);
        dest.writeString(mWordContent);
        dest.writeString(mPronunciation);
        dest.writeParcelable(mWordType, flags);
        dest.writeParcelable(mWordCategory, flags);
        dest.writeInt(mMasterLevel);
        dest.writeInt(mRepetitionsCount);
        dest.writeTypedList(mTranslations);
        dest.writeTypedList(mSynonimes);
        dest.writeTypedList(mSentences);
        dest.writeTypedList(mMneminics);
    }
}
