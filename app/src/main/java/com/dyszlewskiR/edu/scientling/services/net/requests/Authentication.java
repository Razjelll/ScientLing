package com.dyszlewskiR.edu.scientling.services.net.requests;

import android.content.Context;
import android.util.Base64;

import com.dyszlewskiR.edu.scientling.preferences.LogPref;

/**
 * Created by Razjelll on 27.04.2017.
 */

public class Authentication {

    public static String prepare(Context context){
        String username = LogPref.getLogin(context) ;
        String password = LogPref.getPassword(context);
        return prepare(username, password);
    }

    public static String prepare(String username, String password){
        String user = username != null ? username : "";
        String pass = password != null ? password : "";
        String usernameAndPassword = username + ":" + password;
        String result = "Basic " + Base64.encodeToString(usernameAndPassword.getBytes(), Base64.DEFAULT);
        return  result;

    }
}
