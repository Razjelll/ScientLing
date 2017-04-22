package com.dyszlewskiR.edu.scientling.services.speech;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Klasa odtwarzająca pliki
 */

public class RecordMediaPlayer {

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private boolean mIsInit;

    public void init(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.reset();
            }
        });
        mIsInit = true;
    }

    public void play(Uri uri) throws IOException {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mContext, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } else {
            mMediaPlayer.stop();
            ;
            mMediaPlayer.reset();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    public boolean isInit() {
        return mIsInit;
    }
}
