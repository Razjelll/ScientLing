package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DescriptionRequest {
    private final static String REQUEST_URI = Constants.SERVER_ADDRESS + "/sets/?/description";

    public static HttpURLConnection start(long setId, String username, String password) throws IOException {
        String requestUri = REQUEST_URI.replace("?",String.valueOf(setId));
        HttpURLConnection connection = URLConnector.getHttpConnection(requestUri);
        connection.setRequestProperty("Authorization", Authentication.prepare(username, password));
        connection.connect();
        return connection;
    }
}
