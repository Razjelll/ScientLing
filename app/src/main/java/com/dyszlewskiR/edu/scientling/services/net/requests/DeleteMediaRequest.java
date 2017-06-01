package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.services.net.requests.Authentication;

import java.io.IOException;
import java.net.HttpURLConnection;

class DeleteMediaRequest {

    private static HttpURLConnection prepareConnection(String url, String username, String password) throws IOException {
        HttpURLConnection connection = URLConnector.getHttpConnection(url);
        //connection.setDoInput(true);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", Authentication.prepare(username, password));
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        return connection;
    }

    static HttpURLConnection start(long setId, String username, String password, String request) throws IOException {
        String requestUri = request.replace("?", String.valueOf(setId));
        HttpURLConnection connection = prepareConnection(requestUri, username, password);
        connection.connect();
        return connection;
    }
}
