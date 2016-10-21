package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 17.10.2016.
 */

public class Category implements Parcelable{

    /**Numer identyfikacyjny kategorii*/
    private long mCategoryId;
    /**Nazwa kategorii*/
    private String mCategoryName;

    public Category(long id, String name)
    {
        mCategoryId = id;
        mCategoryName = name;
    }

    protected Category(Parcel in) {
        mCategoryId = in.readLong();
        mCategoryName = in.readString();
    }

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

    public long getId()
    {
        return mCategoryId;
    }

    public String getName()
    {
        return mCategoryName;
    }

    public void setName(String name)
    {
        mCategoryName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mCategoryId);
        dest.writeString(mCategoryName);
    }
}
