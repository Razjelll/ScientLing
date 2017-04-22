package com.dyszlewskiR.edu.scientling.net.requests;

import android.util.JsonReader;

import com.dyszlewskiR.edu.scientling.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razjelll on 19.04.2017.
 */

public class LoginRequest {

    private static final String LOGIN_REQUEST = Constants.SERVER_ADDRESS + "/login";
    private static int TIME_OUT = 5000;
    private Map<String, String> mParams;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";


    public LoginRequest(String username, String password) {
        mParams = new HashMap<>();
        mParams.put("username", username);

        mParams.put("password", password);
    }

    public HttpURLConnection start() throws IOException, JSONException {
        HttpURLConnection connection = URLConnector.getHttpConnection(LOGIN_REQUEST, TIME_OUT);
        String content = getJson(mParams.get(USERNAME), mParams.get(PASSWORD));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(content);
        writer.close();
        connection.connect();
        return connection;
    }

    private String getJson(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(USERNAME, username);
        json.put(PASSWORD, password);
        return json.toString();
    }
}