package com.dyszlewskiR.edu.scientling.services.exercises;

/**
 * Created by Razjelll on 17.11.2016.
 */

public class Answer {

    private long mId;
    private String mAnswer;

    public Answer(long id, String answer)
    {
        setId(id);
        setAnswer(answer);
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }
}
