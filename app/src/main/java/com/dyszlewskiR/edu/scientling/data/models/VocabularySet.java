package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class VocabularySet implements Parcelable {


    public static final Creator<VocabularySet> CREATOR = new Creator<VocabularySet>() {
        @Override
        public VocabularySet createFromParcel(Parcel in) {
            return new VocabularySet(in);
        }

        @Override
        public VocabularySet[] newArray(int size) {
            return new VocabularySet[size];
        }
    };
    private long id;
    private String name;
    private Language languageL2;
    private Language languageL1;

    public VocabularySet() {
    }


    public VocabularySet(long id) {
        this.id = id;
    }

    protected VocabularySet(Parcel in) {
        id = in.readLong();
        name = in.readString();
        languageL2 = in.readParcelable(Language.class.getClassLoader());
        languageL1 = in.readParcelable(Language.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(languageL2, flags);
        dest.writeParcelable(languageL1, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Language getLanguageL2() {
        return languageL2;
    }

    public void setLanguageL2(Language language) {
        this.languageL2 = language;
    }

    public Language getLanguageL1() {
        return languageL1;
    }

    public void setLanguageL1(Language language) {
        languageL1 = language;
    }

}
