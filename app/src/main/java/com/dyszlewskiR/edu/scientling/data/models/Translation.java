package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Translation implements Parcelable{

    private long id;
    private String content;

    public Translation() {
    }

    public Translation(long id) {
        this.id = id;
    }


    protected Translation(Parcel in) {
        id = in.readLong();
        content = in.readString();
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
    }
}
