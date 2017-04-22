package com.dyszlewskiR.edu.scientling.net.requests;

import com.dyszlewskiR.edu.scientling.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Razjelll on 21.04.2017.
 */

public class DownloadSetRequest {
    private static final String DOWNLOAD_SET_REQUEST = Constants.SERVER_ADDRESS + "/sets";

    private long mSetId;

    public DownloadSetRequest(long setId){
        mSetId = setId;
    }

    public HttpURLConnection start() throws IOException {
        String url = DOWNLOAD_SET_REQUEST + "/" + mSetId;
        HttpURLConnection connection = URLConnector.getHttpConnection(url, Constants.SERVER_TIMEOUT);
        connection.connect();
        return connection;
    }
}
