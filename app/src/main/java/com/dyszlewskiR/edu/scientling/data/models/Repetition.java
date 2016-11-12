package com.dyszlewskiR.edu.scientling.data.models;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class Repetition {

    private long id;
    private long wordId;
    private int day;
    private int month;

    public Repetition(){}
    public Repetition(long id)
    {
        this.id = id;
    }

    public long getId() {
        return id;

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if(day > 0 && day <= 31)
        {
            this.day = day;
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if(month >0 && month <= 12)
        {
            this.month = month;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repetition that = (Repetition) o;

        if (id != that.id) return false;
        if (wordId != that.wordId) return false;
        if (day != that.day) return false;
        return month == that.month;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (wordId ^ (wordId >>> 32));
        result = 31 * result + day;
        result = 31 * result + month;
        return result;
    }
}
