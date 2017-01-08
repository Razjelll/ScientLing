package com.dyszlewskiR.edu.scientling.preferences;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Razjelll on 06.01.2017.
 */

public class Settings {

    private static  final String CURRENT_SET = "settingCurrentSetId";

    public static long getCurrentSetId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(CURRENT_SET,0);
    }

    public static void setCurrentSetId(long setId, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(CURRENT_SET,setId);
        editor.apply();
    }
}