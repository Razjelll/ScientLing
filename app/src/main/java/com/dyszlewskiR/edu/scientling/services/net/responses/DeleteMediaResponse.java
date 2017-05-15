package com.dyszlewskiR.edu.scientling.services.net.responses;


import java.io.IOException;
import java.net.HttpURLConnection;

public class DeleteMediaResponse {

    private HttpURLConnection mConnection;

    public DeleteMediaResponse(HttpURLConnection connection){
        mConnection = connection;
    }

    public boolean getResponse() throws IOException {
        //TODO tymczasowa metoda, powinniśmy pobrać jakąś odpowiedź
        mConnection.getInputStream();
        return true;
    }

    public void closeConnection(){
        if(mConnection != null){
            mConnection.disconnect();
        }
    }
}
