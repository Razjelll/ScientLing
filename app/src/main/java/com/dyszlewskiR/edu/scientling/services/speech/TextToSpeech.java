package com.dyszlewskiR.edu.scientling.services.speech;

import android.content.Context;
import android.os.Build;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa służąca do zamiany tekstu na mowę. Klasa będzie używana w nauce słówek i ćwiczenia
 * do przeczytania słówka w razie braku nagrania lektora lub wyboru użytkownika.
 * Klasa korzysta z wbudowanej w system Android mechanice zamiany tekstu na mowę.
 * <p>
 * Klasa TextToSpeech systemu Android syntezuje podany tekst do natychmiastowego odtworzenia
 * lub do stworzenia pliku dźwiękowego. Instacja obiektu tej klasy może być użyta tylko raz
 * do syntezy tekstu po zakończeniu inicjalizacji. Po zakończeniu używania obiektu tej klasy
 * należy wywołać metodę shutdown(), która usunie zasoby jakie były potrzebne do działąnia
 * obiektu klasy TextToSpeech
 */

public class TextToSpeech extends UtteranceProgressListener implements android.speech.tts.TextToSpeech.OnInitListener {


    private final String LOG_TAG = "TextToSpeech";
    //do kolejkowania wiadomości zanim nastąpi inicjacja silnika TTS
    private final ConcurrentLinkedQueue<String> mBufferedMessages;
    private android.speech.tts.TextToSpeech mTextToSpeech;
    private Context mContext;
    private String mLanguageCode;
    private boolean mIsReady;
    private boolean mFirstEnd;
    private String mMessage;

    private ISpeechCallback mCallback;
    private boolean mInitialized;

    private float mPitch;
    private float mRate;

    public boolean isInitialized(){
        return mInitialized;
    }

    /**
     * Konstruktor klasy TextToSpeech. Konstruktor inicjalizuje obiekt klasy TextToSpeech systemu Android.
     * Aby to zrobić potrzebuje instancji klasy context, która zostaje przekazana z fragmentów lub
     * aktywności które korzystają z zamiany tekstu na mowę. Drugim parameterem jakim przyjmuje
     * konstruktor TextToSpeech Androida jest obiekt OnInitListener, który posiada jedną metodę.
     * W tej metodzie ustawiono język, w jakim ma być wypowiadany syntezowany głos. Ustawiony
     * język może wpływać na to, czy aplikacja będzie musiała mieć dostęp do internetu podczas
     * syntezowania mowy, ponieważ Android tylko dla niewielkiej liczby języków potrafi syntezować
     * mowę w trybie offline. //TODO tutaj wymienić języki i podać źródło
     * Kod języka zostaje przekazany przez fragment lub aktywność, które posiadają go z bazy danych.
     *
     * @param context
     * @param languageCode
     */
    public TextToSpeech(Context context, final String languageCode) {
        mTextToSpeech = new android.speech.tts.TextToSpeech(context, this);
        mContext = context;
        mLanguageCode = languageCode;
        mBufferedMessages = new ConcurrentLinkedQueue<>();
    }

    public void setCallback(ISpeechCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onInit(int status) {

        if (status == android.speech.tts.TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.ENGLISH);
            synchronized (this) {
                Log.d(LOG_TAG, "onInit");
                mIsReady = true;
                mTextToSpeech.setOnUtteranceProgressListener(this);
                /*for (String bufferedMessage : mBufferedMessages) {
                    speak(bufferedMessage);
                }
                mBufferedMessages.clear();*/
                if (mMessage != null) {
                    speak(mMessage);
                }
                mMessage = null;
            }
        }
    }

    public void release() {
        synchronized (this) {
            mTextToSpeech.shutdown();
            mIsReady = false;
        }
    }

    public void notifyNewMessage(String message) {
        synchronized (this) {
            if (!mTextToSpeech.isSpeaking()) {
                if (mIsReady) {
                    speak(message);
                } else {
                    mMessage = message;
                }
            }
        }
    }

    public void speak(final String message) {
        if (mIsReady) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId = this.hashCode() + "";
                        mTextToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    } else {
                        Log.d(LOG_TAG, message);
                        HashMap<String, String> params = new HashMap<>();
                        //params.put(android.speak.tts.TextToSpeech.Engine.KEY_PARAM_STREAM, "STREAM_NOTIFICATION");
                        params.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        mTextToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, params);
                    }
                }
            }).start();
        }
        //mTextToSpeech.speak(message, android.speak.tts.TextToSpeech.QUEUE_ADD, params);
        //mTextToSpeech.playSilence(100, android.speak.tts.TextToSpeech.QUEUE_ADD, params);
    }

    public void setLanguage(String code) {
        Locale locale = new Locale(code);
        if (mTextToSpeech.isLanguageAvailable(locale) == android.speech.tts.TextToSpeech.LANG_AVAILABLE) {
            Log.d(LOG_TAG, "Language Available");
            mTextToSpeech.setLanguage(new Locale(code));
        } else {
            Log.d(LOG_TAG, "Language Inavailable");
            mTextToSpeech.setLanguage(null);
        }


    }


    @Override
    public void onStart(String utteranceId) {
        Log.d(LOG_TAG, "onStart");
        if (mCallback != null) {
            mCallback.onSpeechStart();
        }
        mInitialized = true;
    }

    @Override
    public void onDone(String utteranceId) {
        Log.d(LOG_TAG, " onDone");
        if (mCallback != null) {
            mCallback.onSpeechCompleted();
        }
    }

    @Override
    public void onError(String utteranceId) {
        Log.d(LOG_TAG, "onError");
    }
}
