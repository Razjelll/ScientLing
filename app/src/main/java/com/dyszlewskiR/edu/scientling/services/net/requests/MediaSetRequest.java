package com.dyszlewskiR.edu.scientling.services.net.requests;

import android.content.Context;

import com.dyszlewskiR.edu.scientling.data.file.MediaFileSystem;
import com.dyszlewskiR.edu.scientling.services.net.URLConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class MediaSetRequest {

    private static final int READ_TIMEOUT = 3000;
    public static final int CHUNK_SIZE = 1024;
    protected static final String TWO_HYPNES = "--";
    protected static final String MULTIPART_BOUNDARY = "*****";
    protected static final String LINE_END = "\r\n";

    protected final String META_NAME = "json";
    protected final String FILE_NAME = "data";
    protected final String FILENAME = "set.zip";

    private long mSetId;
    private String mUsername;
    private String mPassword;
    private String mSetCatalog;
    private Context mContext;

    protected long getSetId(){return mSetId;}
    protected String getUsername(){return mUsername;}
    protected String getPassword(){return mPassword;}

    protected void setSetId(long setId){mSetId = setId;}
    protected void setUsername(String username){mUsername = username;}
    protected void setPassword(String password){mPassword = password;}
    protected void setCatalog(String catalog){mSetCatalog = catalog;}
    protected void setContext(Context context){mContext = context;}


    public MediaSetRequest(long setId, String username, String password, String catalog, Context context){
        mSetId = setId;
        mUsername = username;
        mPassword = password;
        mSetCatalog = catalog;
        mContext = context;
    }

    protected HttpURLConnection createConnection(String url) throws IOException {
        HttpURLConnection connection = URLConnector.getHttpConnection(url);
        connection.setRequestMethod("POST");
        //connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Authorization", Authentication.prepare(mUsername, mPassword));
        connection.setChunkedStreamingMode(CHUNK_SIZE);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
        return connection;
    }

    protected HttpURLConnection start(String request, String filesFolder) throws IOException, JSONException {
        HttpURLConnection connection = createConnection(request);
        OutputStream outputStream = connection.getOutputStream();
        connection.connect();
        //uploadFiles(outputStream, getSetId(), filesFolder);
        return connection;
    }

    private void uploadFiles(OutputStream outputStream, long setId, String filesFolder) throws JSONException, IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        //setMeta(dataOutputStream, META_NAME, prepareMetaJson(setId));
        setFile(dataOutputStream, FILE_NAME, FILENAME, filesFolder);
        dataOutputStream.close();
    }

    protected void setMeta(DataOutputStream dataOutputStream, String name, String content) throws IOException {
        if(dataOutputStream!= null){
            dataOutputStream.writeBytes(TWO_HYPNES+MULTIPART_BOUNDARY);
            dataOutputStream.writeBytes(LINE_END);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name="+name);
            dataOutputStream.writeBytes(LINE_END);
            dataOutputStream.writeBytes("Content-Type: application/json");
            dataOutputStream.writeBytes(LINE_END);
            dataOutputStream.writeBytes(LINE_END);
            dataOutputStream.writeBytes(content);
            dataOutputStream.writeBytes(LINE_END);

            //dataOutputStream.writeBytes(TWO_HYPNES+MULTIPART_BOUNDARY+TWO_HYPNES);
            dataOutputStream.writeBytes(TWO_HYPNES+MULTIPART_BOUNDARY);
            dataOutputStream.writeBytes(LINE_END);
            dataOutputStream.flush();
        }
    }

    protected  void setFile(DataOutputStream dataOutputStream, String name, String fileName, String mediaType) throws IOException {
        /*dataOutputStream.writeBytes(TWO_HYPNES+MULTIPART_BOUNDARY);
        dataOutputStream.writeBytes(LINE_END);

        dataOutputStream.writeBytes("Content-Disposition: form-data; name="+name+"; filename="+fileName);
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.writeBytes("Content-Type: application/octet-stream");
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.writeBytes(LINE_END);*/
        uploadFile(dataOutputStream,mediaType);
        /*dataOutputStream.writeBytes(LINE_END);

        dataOutputStream.writeBytes(TWO_HYPNES+MULTIPART_BOUNDARY+TWO_HYPNES);
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.flush();*/
    }

    protected void uploadFile(DataOutputStream outputStream, String folderName) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        final File folder = MediaFileSystem.getCatalog(mSetCatalog, mContext);
        if(!folder.exists()){
            return;
        }
        int entriesCount = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() && fileEntry.getName().equals(folderName)) {
                for (final File imageEntry : fileEntry.listFiles()) {
                    ZipEntry entry = new ZipEntry(imageEntry.getName());
                    zipOutputStream.putNextEntry(entry);
                    entriesCount++;
                    FileInputStream inputStream = new FileInputStream(imageEntry);
                    int data;
                    byte[] buffer = new byte[CHUNK_SIZE];
                    while (inputStream.read(buffer) != -1) {
                        //zipOutputStream.write(inputStream.read());
                        zipOutputStream.write(buffer);
                        zipOutputStream.flush();
                    }

                    zipOutputStream.closeEntry();
                    inputStream.close();
                }
            }
        }
    }

    private final String SET_ID = "id";
    protected String prepareMetaJson(long setId) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(SET_ID, setId);
        return object.toString();
    }
}
