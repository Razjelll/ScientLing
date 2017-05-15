package com.dyszlewskiR.edu.scientling.services.net.requests;

import android.content.Context;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.file.FileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.json.JsonSetParts;
import com.dyszlewskiR.edu.scientling.services.net.URLConnector;
import com.dyszlewskiR.edu.scientling.services.net.utils.ChunkWriter;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadSetRequest {
    private static final String UPLOAD_SET = Constants.SERVER_ADDRESS + "/sets";
    private static final int READ_TIMEOUT = 3000;
    private static final int CHUNK_BUFFER_SIZE = 1024;

    private long mSetId;
    private String mDescription;
    private DataManager mDataManager;
    private Context mContext;
    private String mUsername;
    private String mPassword;

    public UploadSetRequest(long setId,String description, String username, String password,Context context, DataManager dataManager){
        mSetId = setId;
        mDescription = description;
        mUsername = username;
        mPassword = password;
        mDataManager = dataManager;
        mContext = context;
    }

    public HttpURLConnection start() throws IOException, JSONException, InterruptedException {
        HttpURLConnection connection = URLConnector.getHttpConnection(UPLOAD_SET, Constants.SERVER_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        //connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Authorization", Authentication.prepare(mUsername, mPassword));
        connection.setChunkedStreamingMode(CHUNK_BUFFER_SIZE);
        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        prepareAndSendPostContent(outputStream,"Opis");
        return connection;
    }

    private void prepareAndSendPostContent(OutputStream outputStream, String description) throws JSONException, IOException {
        ChunkWriter writer = new ChunkWriter(CHUNK_BUFFER_SIZE, outputStream);
        JsonSetParts jsonCreator = new JsonSetParts(mSetId, mDataManager);
        jsonCreator.setSetDescription(mDescription);
        String json;
        StringBuilder stringBuilder = new StringBuilder();
        while((json = jsonCreator.getNext())!=null){
            writer.write(json);
            stringBuilder.append(json);
        }
        Log.d(getClass().getSimpleName(), stringBuilder.toString());
        writer.writeBuffer();
        writer.close();
    }
}
