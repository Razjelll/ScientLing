package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Category implements Parcelable {

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    private long id;
    private String name;
    private Language language;

    public Category() {
    }

    public Category(long id) {
        this.id = id;
    }

    protected Category(Parcel in) {
        id = in.readLong();
        name = in.readString();
        language = in.readParcelable(Language.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(language, flags);
    }
}
