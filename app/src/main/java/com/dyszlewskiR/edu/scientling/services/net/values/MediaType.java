package com.dyszlewskiR.edu.scientling.services.net.values;

public enum MediaType {

    IMAGES("images"),
    RECORDS("records");

    private String mValue;
    public String getValue(){return mValue;}
    MediaType(String value){mValue = value;}
}
