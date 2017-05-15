package com.dyszlewskiR.edu.scientling.services.json;

import android.util.Log;

import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import org.json.JSONException;

/**
 * Created by Razjelll on 28.04.2017.
 */

public class JsonSetParts {

    private final int START = 0;
    private final int SET = 1;
    private final int START_LESSONS = 2;
    private final int LESSON = 3;
    private final int END_LESSONS = 4;
    private final int START_WORDS =5;
    private final int WORDS = 6;
    private final int END_WORDS = 7;
    private final int NUM_WORDS = 8;
    private final int FINISH = 9;

    private final String SET_OBJECT = "set";
    private final String LESSONS_OBJECT = "lessons";
    private final String WORDS_OBJECT = "words";
    private final String WORDS_COUNT_OBJECT = "words_count";

    private final String QUOTATION = "\"";

    private long mSetId;
    private String mDescription;
    private DataManager mDataManager;

    private int mPosition;

    private JsonLessons mLessonJsonCreator;
    private JsonWords mWordsJsonCreator;

    private boolean mFirstLesson;
    private boolean mFirstWord;

    private int mWordsCount;

    public JsonSetParts(long setId, DataManager dataManager){
        mSetId = setId;
        mDataManager = dataManager;
        mPosition = START;

        mFirstLesson = true;
        mFirstWord = true;
    }

    public void setSetDescription(String description){
        mDescription = description;
    }

    public String getNext() throws JSONException {
        switch (mPosition){
            case START:
                Log.d(getClass().getSimpleName(), "START");
                return getStartPart();
            case SET:
                Log.d(getClass().getSimpleName(), "SET");
                return getStartPartSet();
            case START_LESSONS:
                Log.d(getClass().getSimpleName(), "START_LESSON");
                return getStartLessonPart();
            case LESSON:
                Log.d(getClass().getSimpleName(), "LESSON");
                String lessonPart = getLessonPart();
                if(lessonPart != null){
                    return lessonPart;
                }
                //celowo przepuszczamy do kolejnego casa, żeby nie zwrócić nulla który zakończyłby pętle pobierającą części
            case END_LESSONS:
                Log.d(getClass().getSimpleName(), "END_LESSONS");
                return getEndLessonPart();
            case START_WORDS:
                Log.d(getClass().getSimpleName(), "START_WORDS");
                return getStartWordsPart();
            case WORDS:
                Log.d(getClass().getSimpleName(), "WORDS");
                String wordPart = getWordPart();
                if(wordPart != null){
                    return wordPart;
                }
            case END_WORDS:
                Log.d(getClass().getSimpleName(), "END_WORDS");
                return getEndWordsPart();
            case NUM_WORDS:
                Log.d(getClass().getSimpleName(), "NUM_WORDS");
                return getWordCountPart();
            case FINISH:
                Log.d(getClass().getSimpleName(), "FINISH");
                return getEndObjectPart();


        }
        return null;
    }

    private String getStartPart(){
        mPosition++;
        return "{";
    }

    private String getStartPartSet() throws JSONException {
        mPosition++;
        //"set": {...}
        return new StringBuilder().append(QUOTATION).append(SET_OBJECT).append(QUOTATION).append(":")
                .append(getSetPart()).toString();
    }

    private String getSetPart() throws JSONException {
        JsonSet jsonSet = new JsonSet(mSetId,mDescription, mDataManager);
        if(jsonSet.start()){
            return jsonSet.getSetJson();
        }
        return "";
    }

    private String getStartLessonPart() throws JSONException {
        mPosition++;
        //na początku dodajemy przecinek, który odziela obiekt lekcji od obiektu zestawu
        StringBuilder stringBuilder = new StringBuilder(",");
        stringBuilder.append(QUOTATION).append(LESSONS_OBJECT).append(QUOTATION).append(":");
        stringBuilder.append("[");
        //jeżeli zakładamy, ze pobieramy wszystkie lekcje naraz
        /*JsonLessons jsonLessons = new JsonLessons(mSetId, mDataManager);
        if(jsonLessons.start()){
            String json = jsonLessons.getLessonArrayJson();
            return json;
        }*/
        return stringBuilder.toString();
    }

    private String getLessonPart() throws JSONException {
        if(mLessonJsonCreator == null){
            mLessonJsonCreator = new JsonLessons(mSetId, mDataManager);
            if(!mLessonJsonCreator.start()){
                mPosition++;
                releaseLessonJsonCreator();
                return null;
            }
        }

        //TODO napisać to jakoś zgrabniej
        String result = mLessonJsonCreator.getLessonJson();

        if(result == null){
            mPosition++;
            releaseLessonJsonCreator();
        }
        if(mFirstLesson){
            mFirstLesson = false;
        } else if(result != null){
            return ","+result;
        }
        return result;
    }

    private void releaseLessonJsonCreator(){
        mLessonJsonCreator.release();
        mLessonJsonCreator = null;
    }

    private String getEndLessonPart(){
        mPosition++;
        return "]";
    }

    private String getStartWordsPart(){
        mPosition++;
        return new StringBuilder().append(", ").append(QUOTATION).append(WORDS_OBJECT).append(QUOTATION)
                .append(":")
                .append("[").toString();
    }

    private String getWordPart() throws JSONException {
        if(mWordsJsonCreator == null){
            mWordsJsonCreator = new JsonWords(mSetId, mDataManager);
            if(!mWordsJsonCreator.start()){
                mPosition++;
                releaseWordJsonCreator();
                return null;
            }
        }
        String result = mWordsJsonCreator.getWordJson();
        if(result == null){
            mPosition++;
            releaseWordJsonCreator();
        }
        mWordsCount++;
        if(mFirstWord){
            mFirstWord = false;
            return result;
        } else if(result !=null){

            return ","+result;
        }
        return null;
    }

    private void releaseWordJsonCreator(){
        mWordsJsonCreator.release();
        mWordsJsonCreator = null;
    }

    private String getEndWordsPart(){
        mPosition++;
        return "]";
    }

    private String getWordCountPart(){
        mPosition++;
        return new StringBuilder().append(", ").append(QUOTATION).append(WORDS_COUNT_OBJECT).append(QUOTATION)
                .append(":").append(String.valueOf(mWordsCount)).toString();
    }

    private String getEndObjectPart(){
        mPosition++;
        return "}";
    }
}
