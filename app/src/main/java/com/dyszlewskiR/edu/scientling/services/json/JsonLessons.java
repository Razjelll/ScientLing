package com.dyszlewskiR.edu.scientling.services.json;

import android.database.Cursor;
import android.os.health.PackageHealthStats;

import com.dyszlewskiR.edu.scientling.data.models.creators.LessonCreator;
import com.dyszlewskiR.edu.scientling.data.models.models.Lesson;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Razjelll on 28.04.2017.
 */

public class JsonLessons {

    private long mSetId;
    private DataManager mDataManager;
    private Cursor mCursor;

    public JsonLessons(long setId, DataManager dataManager){
        mSetId = setId;
        mDataManager = dataManager;
    }

    public boolean start(){
        mCursor = mDataManager.getAllLessonsCursor(mSetId);
        return mCursor!=null && mCursor.getCount() >0;
    }

    private final String ID = "id";
    private final String NAME = "name";
    private final String NUMBEr = "number";

    public String getLessonJson() throws JSONException {
        JSONObject node = getLessonNode();
        if(node != null){
            return node.toString();
        }
        return null;
    }

    public JSONObject getLessonNode() throws JSONException {
        if(cursorNext()){
            Lesson lesson = LessonCreator.createFromCursor(mCursor);
            JSONObject node = getLessonNode(lesson);
            return node;
        }
        return null;
    }

    private boolean cursorNext(){
        if(mCursor.isBeforeFirst()){
            return mCursor.moveToFirst();
        } else {
            return mCursor.moveToNext();
        }
    }

    private JSONObject getLessonNode(Lesson lesson) throws JSONException {
        if(lesson != null){
            JSONObject node = new JSONObject();
            node.put(ID,lesson.getId());
            node.put(NAME, lesson.getName());
            node.put(NUMBEr,lesson.getNumber());
            return node;
        }
        return null;
    }

    public String getLessonArrayJson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        while (cursorNext()){
            node = getLessonNode();
            if(node != null){
                jsonArray.put(node);
            }
        }
        return jsonArray.toString();
    }

    public void release(){
        mCursor.close();
    }
}
