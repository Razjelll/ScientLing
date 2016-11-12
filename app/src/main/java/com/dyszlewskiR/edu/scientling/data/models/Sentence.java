package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 03.11.2016.
 */

public class Sentence{


    private long id;
    private String sentence;
    private String translation;

    public Sentence(){}
    public Sentence(long id)
    {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence1 = (Sentence) o;

        if (id != sentence1.id) return false;
        if (sentence != null ? !sentence.equals(sentence1.sentence) : sentence1.sentence != null)
            return false;
        return translation != null ? translation.equals(sentence1.translation) : sentence1.translation == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (sentence != null ? sentence.hashCode() : 0);
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        return result;
    }
}
