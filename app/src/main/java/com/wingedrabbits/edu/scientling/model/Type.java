package com.wingedrabbits.edu.scientling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Razjelll on 21.10.2016.
 */

public class Type implements Parcelable{

    private long typeId;
    private String typeName;

    public Type(long id, String name)
    {
        typeId = id;
        typeName = name;
    }

    public Type(Parcel in)
    {
        typeId = in.readLong();
        typeName = in.readString();
    }



    public long getId() {
        return typeId;
    }

    public String getName()
    {
        return typeName;
    }

    public void setId(long id)
    {
        typeId = id;
    }

    public void setName(String name)
    {
        typeName = name;
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(typeId);
        dest.writeString(typeName);
    }
}
