package com.dyszlewskiR.edu.scientling.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Razjelll on 19.04.2017.
 */

public class LogPref {
    private static final String LOGIN = "prefLogin";
    private static final String PASSWORD = "prefPassword";
    private static final String IS_LOGGED = "prefLogged";

    public static String getLogin(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String login = prefs.getString(LOGIN, null);
        return login;
    }

    public static void setLogin(String login, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(LOGIN, login);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String password = prefs.getString(PASSWORD, null);
        return password;
    }

    public static void setPassword(String password, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public static boolean isLogged(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLogged = prefs.getBoolean(IS_LOGGED, false);
        return isLogged;
    }

    public static void setLogged(boolean logged, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(IS_LOGGED, logged);
        editor.apply();
    }

}
