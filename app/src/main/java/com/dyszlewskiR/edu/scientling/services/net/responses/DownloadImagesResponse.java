package com.dyszlewskiR.edu.scientling.services.net.responses;

import android.content.Context;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.file.FileSystem;
import com.dyszlewskiR.edu.scientling.services.net.values.ResponseStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadImagesResponse {
    public static final int OK = 1;
    public static final int UNAUTHORIZED = -1;
    public static final int ERROR = -2;

    private final int BUFFER_SIZE = 1024;

    private HttpURLConnection mConnection;
    private Context mContext;

    public DownloadImagesResponse(HttpURLConnection connection, Context context){
        mConnection =connection;
        mContext = context;
    }

    public int getResultCode() throws IOException {
        if(mConnection != null){
            int resultCode = mConnection.getResponseCode();
            switch (resultCode){
                case ResponseStatus.OK:
                    return OK;
                //TODO zrobić kody
            }
        }
        return ERROR;
    }

    public void saveImages(String setCatalog) throws IOException {
        if(mConnection == null){
            return;
        }
        File catalog = FileSystem.getCatalog(setCatalog, mContext);
        if(!catalog.exists()){
            catalog.mkdir();
        }
        InputStream inputStream = mConnection.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        byte[]buffer = new byte[BUFFER_SIZE];
        ZipEntry zipEntry;
        while((zipEntry = zipInputStream.getNextEntry()) != null){
            Log.d(getClass().getSimpleName(), "start Entry");
            File imageFile = FileSystem.getFile(zipEntry.getName(), setCatalog, mContext);
            if(!imageFile.exists()){
                imageFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            /*while(zipInputStream.available()>0){
                outputStream.write(zipInputStream.read());
            }*/
            /*while(zipInputStream.read(buffer)!=-1){
                outputStream.write(buffer);
            }*/
            /*int data;
            while((data = zipInputStream.read()) != -1){
                outputStream.write(data);
            }*/
            int length;
            while((length = zipInputStream.read(buffer,0, buffer.length))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.close();
        }
        zipInputStream.close();
    }

    public void closeConnection(){
        mConnection.disconnect();
    }
}
