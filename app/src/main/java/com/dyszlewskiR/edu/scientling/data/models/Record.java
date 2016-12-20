package com.dyszlewskiR.edu.scientling.data.models;

import java.io.File;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Record {

    private long id;
    private String name;
    private String catalog;
    private String extension;
    private File soundFile; //TODO zastanowić się

    public Record() {
    }

    public Record(long id) {
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

    public File getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(File soundFile) {
        this.soundFile = soundFile;
    }
}
