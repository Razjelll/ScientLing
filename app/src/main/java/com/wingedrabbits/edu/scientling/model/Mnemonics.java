package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 21.10.2016.
 */

public class Mnemonics implements Parcelable{

    private long mMnemonicsId;
    private String mMnemonicsContent;

    public Mnemonics(long id, String content) {
        mMnemonicsId = id;
        mMnemonicsContent = content;
    }

    protected Mnemonics(Parcel in) {
        mMnemonicsId = in.readLong();
        mMnemonicsContent = in.readString();
    }

    public long getId() {return mMnemonicsId;}

    public String getContent() {return mMnemonicsContent;}

    public void setId(long id) {mMnemonicsId = id;}

    public void setContent(String content) {mMnemonicsContent = content;}

    public static final Creator<Mnemonics> CREATOR = new Creator<Mnemonics>() {
        @Override
        public Mnemonics createFromParcel(Parcel in) {
            return new Mnemonics(in);
        }

        @Override
        public Mnemonics[] newArray(int size) {
            return new Mnemonics[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mMnemonicsId);
        dest.writeString(mMnemonicsContent);
    }
}
