package com.dyszlewskiR.edu.scientling.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Razjelll on 19.04.2017.
 */

public class URLConnector {

    public static URLConnection getConnection(String url, int timeout) throws IOException {
        URLConnection connection = getConnection(url);
        connection.setConnectTimeout(timeout);
        return connection;
    }

    public static URLConnection getConnection(String url) throws IOException {
        URL serverUrl = new URL(url);
        URLConnection connection = serverUrl.openConnection();
        return connection;
    }

    public static HttpURLConnection getHttpConnection(String url, int timeout) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setConnectTimeout(timeout);
        return connection;
    }

    public static HttpURLConnection getHttpConnection(String url) throws IOException {
        URL serverUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        return connection;
    }

    public static HttpsURLConnection getHttpsConnection(String url, int timeout) throws IOException {
        HttpsURLConnection connection = getHttpsConnection(url);
        connection.setConnectTimeout(timeout);
        return connection;
    }

    public static HttpsURLConnection getHttpsConnection(String url) throws IOException {
        URL serverUrl = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
        return connection;
    }
}
