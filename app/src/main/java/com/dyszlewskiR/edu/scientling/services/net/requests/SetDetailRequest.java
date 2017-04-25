package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Created by Razjelll on 25.04.2017.
 */

public class SetDetailRequest {
    private static final String GET_SET_DETAIL_REQUEST = Constants.SERVER_ADDRESS + "/sets/details";

    private long mSetId;

    public SetDetailRequest(long setId){
        mSetId = setId;
    }

    public HttpURLConnection start() throws IOException{
        String url = GET_SET_DETAIL_REQUEST + "/" + mSetId;
        HttpURLConnection connection = URLConnector.getHttpConnection(url, Constants.SERVER_TIMEOUT);
        connection.connect();
        return connection;
    }

}
