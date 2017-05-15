package com.dyszlewskiR.edu.scientling.services.speech;

import android.content.Context;
import android.net.Uri;

import com.dyszlewskiR.edu.scientling.data.file.FileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;

import java.io.IOException;

/**
 * Created by Razjelll on 10.04.2017.
 */

public class SpeechPlayer {
    private TextToSpeech mTextToSpeech;
    private RecordMediaPlayer mMediaPlayer;
    private Word mWord;
    private VocabularySet mSet;
    private Uri mRecordUri;
    private Context mContext;

    private String mLanguageCode;
    private ISpeechCallback mCallback;

    public SpeechPlayer(Context context) {
        mContext = context;
    }

    public void setCallback(ISpeechCallback callback) {
        mCallback = callback;
    }

    public void setWord(Word word) {
        mRecordUri = FileSystem.getRecordUri(word.getRecordName(), mSet.getCatalog(), mContext);
        mWord = word;
    }

    public void setWord(String content, String recordName) {
        mWord = new Word();
        mWord.setContent(content);
        mWord.setRecordName(recordName);
    }

    public void setSet(VocabularySet set) {
        mSet = set;
    }

    public boolean isRecord() {
        return mRecordUri != null;
    }

    public void speech() throws IOException {
        if (mRecordUri != null) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new RecordMediaPlayer();
            }
            if (!mMediaPlayer.isInit()) {
                mMediaPlayer.init(mContext);
            }
            mMediaPlayer.play(mRecordUri);
        } else {
            if (mTextToSpeech == null) {
                mTextToSpeech = new TextToSpeech(mContext, mSet.getLanguageL2().getCode());
                mTextToSpeech.setCallback(mCallback);
                if (mLanguageCode != null) {
                    mTextToSpeech.setLanguage(mLanguageCode);
                }
            }
            mTextToSpeech.notifyNewMessage(mWord.getContent());
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        if (mTextToSpeech != null) {
            mTextToSpeech.setCallback(null);
            mTextToSpeech.release();
        }
        mCallback = null;
    }

    public void setTextToSpeechLanguage(String code) {
        if (mTextToSpeech != null) {
            mTextToSpeech.setLanguage(code);
            mLanguageCode = code;
        } else {
            mLanguageCode = code;
        }
    }
}
