package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DeleteSetRequest {
    private static final String DELETE_STATEMENT = Constants.SERVER_ADDRESS + "/sets/";

    public static HttpURLConnection start(long setId, String username, String password) throws IOException {
        HttpURLConnection connection = URLConnector.getHttpConnection(DELETE_STATEMENT+setId);
        connection.setDoInput(true);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", Authentication.prepare(username, password));
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        return connection;
    }
}
