package com.dyszlewskiR.edu.scientling.services.speech;

import android.content.Context;
import android.os.Build;

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

public class TextToSpeech implements android.speech.tts.TextToSpeech.OnInitListener {


    private final String TAG = "TextToSpeech";
    //do kolejkowania wiadomości zanim nastąpi inicjacja silnika TTS
    private final ConcurrentLinkedQueue<String> mBufferedMessages;
    private android.speech.tts.TextToSpeech mTextToSpeech;
    private Context mContext;
    private String mLanguageCode;
    private boolean mIsReady;

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

    @Override
    public void onInit(int status) {
        if (status == android.speech.tts.TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.ENGLISH);
            synchronized (this) {
                mIsReady = true;
                for (String bufferedMessage : mBufferedMessages) {
                    speak(bufferedMessage);
                }
                mBufferedMessages.clear();
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
                    mBufferedMessages.add(message);
                }
            }

        }
    }

    private void speak(String message) {

        HashMap<String, String> params = new HashMap<>();
        // params.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM, "STREAM_NOTIFICATION");
        params.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //LILLIPOP, nie można użyć stałej gdy nie ma zainstalowanych nowszych wersji
            String utteranceId = this.hashCode() + "";
            mTextToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            mTextToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, params);
        }
        //mTextToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_ADD, params);
        //mTextToSpeech.playSilence(100, android.speech.tts.TextToSpeech.QUEUE_ADD, params);
    }


}
