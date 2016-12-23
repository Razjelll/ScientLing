package com.dyszlewskiR.edu.scientling.data.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Image implements Parcelable {

    private long id;
    private String name;
    private Bitmap bitmap;

    public Image() {
    }

    public Image(long id) {
        this.id = id;
    }


    protected Image(Parcel in) {
        id = in.readLong();
        name = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

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


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(bitmap, flags);
    }
}
