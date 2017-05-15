package com.dyszlewskiR.edu.scientling.services.net.requests;

import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DeleteRecordsRequest extends DeleteMediaRequest{
    private static final String DELETE_REQUEST = Constants.SERVER_ADDRESS + "/sets/?/records";

    public static HttpURLConnection start(long setId, String username, String password) throws IOException {
        return start(setId, username, password, DELETE_REQUEST);
    }
}
