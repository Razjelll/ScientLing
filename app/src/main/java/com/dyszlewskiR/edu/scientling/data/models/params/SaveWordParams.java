package com.dyszlewskiR.edu.scientling.data.models.params;

import android.net.Uri;

import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;

/**
 * Created by Razjelll on 30.03.2017.
 */

public class SaveWordParams {

    private Word mWord;
    private VocabularySet mSet;
    private boolean mEdit;
    private Uri mImage;
    private Uri mRecord;

    public Word getWord() {
        return mWord;
    }

    public void setWord(Word word) {
        mWord = word;
    }

    public VocabularySet getSet() {
        return mSet;
    }

    public void setSet(VocabularySet set) {
        mSet = set;
    }

    public boolean isEdit() {
        return mEdit;
    }

    public void setEdit(boolean edit) {
        mEdit = edit;
    }

    public Uri getImageUri() {
        return mImage;
    }

    public void setImageUri(Uri imageUri) {
        mImage = imageUri;
    }

    public Uri getRecordUri() {
        return mRecord;
    }

    public void setRecordUri(Uri recordUri) {
        mRecord = recordUri;
    }
}
