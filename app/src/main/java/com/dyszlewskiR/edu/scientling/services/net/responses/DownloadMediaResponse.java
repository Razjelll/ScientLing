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

/**
 * Created by Razjelll on 08.05.2017.
 */

public class DownloadMediaResponse {
    public static final int OK = 1;
    public static final int UNAUTHORIZED = -1;
    public static final int ERROR = -2;

    private final int BUFFER_SIZE = 1024;

    private HttpURLConnection mConnection;
    private Context mContext;
    private String mMediaFolder;

    private ZipInputStream mZipInputStream;

    public DownloadMediaResponse(HttpURLConnection connection,String mediaFolder, Context context){
        mConnection =connection;
        mContext = context;
        mMediaFolder = mediaFolder;
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

    public long getContentLength(){
        if(mConnection!=null){
            return mConnection.getContentLength();
           // return Long.parseLong(mConnection.getHeaderField("X-Content-Length"));
        }
        return 0;
    }

    public interface SaveCallback{
        void onSave(int length);
        void onSaveCompleted();
    }

    public void saveFile(String setCatalog, SaveCallback callback) throws IOException {
        if(mConnection == null){
            return;
        }
        //File mediaCatalog = FileSystem.getCatalog(mMediaFolder, mContext);
        File mediaCatalog = FileSystem.getCatalog(setCatalog, mContext);
        if(!mediaCatalog.exists()){
            mediaCatalog.mkdir();
        }
        //String catalogPath = mMediaFolder + "/" + setCatalog;
        String catalogPath = FileSystem.getMediaPath(setCatalog, mMediaFolder, mContext);
        //File catalog = FileSystem.getCatalog(catalogPath, mContext);
        File catalog = new File(catalogPath);
        if(!catalog.exists()){
            boolean succes = catalog.mkdir();
            Log.d(getClass().getSimpleName(), "Create catalog " + succes);
        }

        InputStream inputStream = mConnection.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        byte[]buffer = new byte[BUFFER_SIZE];
        ZipEntry zipEntry;
        while((zipEntry = zipInputStream.getNextEntry()) != null){
            Log.d(getClass().getSimpleName(), "start Entry");
            File imageFile = FileSystem.getFile(zipEntry.getName(),FileSystem.getMediaCatalog(setCatalog, mMediaFolder) , mContext);
            if(!imageFile.exists()){
                imageFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int length;
            while((length = zipInputStream.read(buffer,0, buffer.length))>0){
                outputStream.write(buffer,0, length);
                if(callback != null){
                    callback.onSave(length);
                }
            }
            outputStream.close();
        }
        zipInputStream.close();
        if(callback != null){
            callback.onSaveCompleted();
        }
    }

    private File mImageFile;
    private ZipEntry mZipEntry;
    private String mCatalogPath;
    private FileOutputStream mOutputStream;
    private byte[] mBuffer;

    public void startSavingFiles(String setCatalog) throws IOException {
        mZipInputStream = new ZipInputStream(mConnection.getInputStream());
        File mediaCatalog = FileSystem.getCatalog(mMediaFolder, mContext);
        if(!mediaCatalog.exists()){
            mediaCatalog.mkdir();
        }
        mCatalogPath = mMediaFolder + "/" + setCatalog;
        File catalog = FileSystem.getCatalog(mCatalogPath, mContext);
        if(!catalog.exists()){
            catalog.mkdir();
        }

        mBuffer = new byte[BUFFER_SIZE];
    }

    public long saveFile() throws IOException {
        Log.d(getClass().getSimpleName(), "saveFile");
        int length;
        length = mZipInputStream.read(mBuffer,0, mBuffer.length);
        mOutputStream.write(mBuffer,0, length);
        return length;
    }

    public boolean next() throws IOException {
        if(mZipEntry == null){
            mZipEntry = mZipInputStream.getNextEntry();
            if(mZipEntry == null){
                return false;
            }
            mImageFile = FileSystem.getFile(mZipEntry.getName(), mCatalogPath, mContext);
            if(!mImageFile.exists()){
                mImageFile.createNewFile();
            }
            mOutputStream = new FileOutputStream(mImageFile);
        }
        int bytes = mZipInputStream.available();
        if(mZipInputStream.available()>0){
            return true;
        } else {
            if(mOutputStream != null){
                mOutputStream.close();
            }
            if((mZipEntry = mZipInputStream.getNextEntry()) != null){
                mImageFile = FileSystem.getFile(mZipEntry.getName(), mCatalogPath, mContext);
                if(!mImageFile.exists()){
                    mImageFile.createNewFile();
                }
                mOutputStream = new FileOutputStream(mImageFile);
                return true;
            }
        }
        return false;
    }

    private void createFile(){

    }

    public void release() throws IOException {
        mZipInputStream.close();
    }



    public void closeConnection(){
        mConnection.disconnect();
    }
}
