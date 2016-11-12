package com.dyszlewskiR.edu.scientling.data.models;

import android.icu.text.MessagePattern;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class PartOfSpeech {

    private long id;
    private String name;

    public PartOfSpeech(){}
    public PartOfSpeech(long id)
    {
        this.id = id;
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

}
