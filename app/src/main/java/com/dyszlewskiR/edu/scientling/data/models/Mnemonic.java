package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Mnemonic {

    private long id;
    private String content;

    public Mnemonic() {}
    public Mnemonic(long id)
    {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
