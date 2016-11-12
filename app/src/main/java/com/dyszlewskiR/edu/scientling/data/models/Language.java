package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Language {

    private long id;
    private String name;
    private String abbreviation;
    private String code;

    public Language(){}
    public Language(long id)
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (id != language.id) return false;
        if (!name.equals(language.name)) return false;
        if (!abbreviation.equals(language.abbreviation)) return false;
        return code.equals(language.code);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + abbreviation.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
