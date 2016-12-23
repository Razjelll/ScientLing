package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Hint implements Parcelable {

    private long id;
    private String content;

    public Hint() {
    }

    public Hint(long id) {
        this.id = id;
    }

    protected Hint(Parcel in) {
        id = in.readLong();
        content = in.readString();
    }

    public static final Creator<Hint> CREATOR = new Creator<Hint>() {
        @Override
        public Hint createFromParcel(Parcel in) {
            return new Hint(in);
        }

        @Override
        public Hint[] newArray(int size) {
            return new Hint[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hint hint = (Hint) o;

        if (id != hint.id) return false;
        return content != null ? content.equals(hint.content) : hint.content == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
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
