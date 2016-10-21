package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 21.10.2016.
 */

public class Meaning implements Parcelable{

    private long mMeaningId;
    private String mMeaningContent;

    public Meaning(long id, String content)
    {
        mMeaningId = id;
        mMeaningContent = content;
    }

    public long getId() {
        return mMeaningId;
    }

    public String getContent() {
        return mMeaningContent;
    }

    public void setId(long id) {
        mMeaningId = id;
    }

    public void setmMeaningContent(String content) {
        mMeaningContent = content;
    }

    protected Meaning(Parcel in) {
        mMeaningId = in.readLong();
        mMeaningContent = in.readString();
    }

    public static final Creator<Meaning> CREATOR = new Creator<Meaning>() {
        @Override
        public Meaning createFromParcel(Parcel in) {
            return new Meaning(in);
        }

        @Override
        public Meaning[] newArray(int size) {
            return new Meaning[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mMeaningId);
        dest.writeString(mMeaningContent);
    }
}
