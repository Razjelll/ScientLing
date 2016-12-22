package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Image implements Parcelable {

    private long id;
    private String name;
    private String catalog;
    private String extension;
    private Image image;

    public Image() {
    }

    public Image(long id) {
        this.id = id;
    }

    protected Image(Parcel in) {
        id = in.readLong();
        name = in.readString();
        catalog = in.readString();
        extension = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
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

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(catalog);
        dest.writeString(extension);
        dest.writeParcelable(image, flags);
    }
}
