package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

public class SetDetailRequest {
    private static final String REQUEST_URI = Constants.SERVER_ADDRESS + "/sets/?/details";

    private long mSetId;

    public SetDetailRequest(long setId){
        mSetId = setId;
    }

    public HttpURLConnection start() throws IOException{
        String requestUri = REQUEST_URI.replace("?", String.valueOf(mSetId));
        HttpURLConnection connection = URLConnector.getHttpConnection(requestUri, Constants.SERVER_TIMEOUT);
        connection.connect();
        return connection;
    }

}
