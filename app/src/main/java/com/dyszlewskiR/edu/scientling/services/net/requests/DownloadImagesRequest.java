package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DownloadImagesRequest {
    private static final String DOWNLOAD_REQUEST = Constants.SERVER_ADDRESS + "/sets/images/";

    private long mSetId;
    private String mUsername;
    private String mPassword;

    public DownloadImagesRequest(long setId, String username, String password){
        mSetId = setId;
        mUsername = username;
        mPassword = password;
    }

    public HttpURLConnection start() throws IOException {
        HttpURLConnection connection = URLConnector.getHttpConnection(DOWNLOAD_REQUEST+mSetId);
        connection.setRequestProperty("Authorization", Authentication.prepare(mUsername, mPassword));
        connection.setDoInput(true);
        connection.connect();
        return connection;
    }
}
