package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Translation {

    private long id;
    private String translation;

    public Translation() {}
    public Translation(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
