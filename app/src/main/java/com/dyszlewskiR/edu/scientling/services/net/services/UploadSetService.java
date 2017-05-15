package com.dyszlewskiR.edu.scientling.services.net.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaDataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadImagesRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadRecordRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.UploadSetResponse;
import com.dyszlewskiR.edu.scientling.utils.StringSimilarityCalculator;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class UploadSetService extends Service {
    private final String LOG_TAG = "UploadSetService";

    public static final String BROADCAST_ACTION = "UploadSetService";

    private long mSetId;
    private long mSetGlobalId;
    private String mSetName;
    private String mDescription;



    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(getClass().getSimpleName(), "startService");
        mSetId = intent.getLongExtra("id", -1);
        mSetGlobalId = intent.getLongExtra("globalId", -1);
        mSetName = intent.getStringExtra("name");
        mDescription = intent.getStringExtra("desc");

        boolean uploadDatabase = intent.getBooleanExtra("database", false);
        boolean uploadImages = intent.getBooleanExtra("images",false);
        boolean uploadRecords = intent.getBooleanExtra("records", false);

        UploadSetAsyncTask task = new UploadSetAsyncTask();
        UploadParams params = new UploadParams(mSetId, mSetGlobalId,mDescription, uploadDatabase, uploadImages, uploadRecords);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
        return Service.START_NOT_STICKY;
    }

    private class UploadParams{
        private long mSetId;
        private long mSetGlobalId;
        private String mDescription;
        private boolean mUploadDatabase;
        private boolean mUploadImages;
        private boolean mUploadRecords;

        public UploadParams(long setId, long globalId,String description, boolean database, boolean images, boolean records){
            mSetId = setId;
            mSetGlobalId = globalId;
            mUploadDatabase = database;
            mUploadImages = images;
            mUploadRecords = records;
            mDescription = description;
        }

        public long getSetId(){return mSetId;}
        public long getSetGlobalId(){return mSetGlobalId;}
        public String getDescription(){return mDescription;}
        public boolean isUploadDatabase(){return mUploadDatabase;}
        public boolean isUploadImages(){return mUploadImages;}
        public boolean ismUploadRecords(){return mUploadRecords;}

        public void setSetId(long setId){mSetId = setId;}
        public void setSetGlobalId(long setId){mSetGlobalId = setId;}
        public void setDescription(String description){mDescription = description;}
        public void setUploadDtabase(boolean isUpload){mUploadDatabase = isUpload;}
        public void setUploadImagees(boolean isUpload){mUploadImages = isUpload;}
        public void setUploadRecords(boolean isUpload){mUploadRecords = isUpload;}
    }

    private class UploadSetAsyncTask extends AsyncTask<UploadParams, Void, Void>{

        @Override
        protected Void doInBackground(UploadParams... params) {
            long setId = params[0].getSetId();
            long setGlobalId = params[0].getSetGlobalId();
            DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(BROADCAST_ACTION);
            broadcastIntent.putExtra("set", mSetId);
            if(params[0].isUploadDatabase()){
                setGlobalId = uploadDatabase(setId,params[0].getDescription(), dataManager);
                dataManager.insertSetGlobalId(setGlobalId, setId);
                dataManager.updateSetUploaded(true, setId);
                //TODO tutaj można wrzucić to w pętle i wykonywać aż się globalId zapisze
                broadcastIntent.putExtra("globalId", setGlobalId);
            }
            if(params[0].isUploadImages()){
                VocabularySet set = dataManager.getSetById(setId);
                String catalog = set.getCatalog();
                try {
                    uploadImages(setGlobalId, catalog);
                    dataManager.updateImageUploaded(true, mSetId);
                    broadcastIntent.putExtra("images", true);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(params[0].ismUploadRecords()){
                VocabularySet set = dataManager.getSetById(setId);
                String catalog = set.getCatalog();
                try {
                    uploadRecords(setGlobalId, catalog);
                    dataManager.updateRecordsUploaded(true, mSetId);
                    broadcastIntent.putExtra("records", true);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sendBroadcast(broadcastIntent);
            return null;
        }

        private long uploadDatabase(long setId,String description, DataManager dataManager){
            Log.d(LOG_TAG, "uploadDatabase");
            UploadSetRequest request = new UploadSetRequest(setId,description, LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()),getBaseContext(),dataManager);
            HttpURLConnection connection = null;
            long setGlobalId = -1;
            try {
                connection = request.start();
                UploadSetResponse response = new UploadSetResponse(connection);
                setGlobalId = response.getId();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return setGlobalId;
        }

        private long uploadImages(long globalId, String catalog) throws IOException, JSONException {
            Log.d(LOG_TAG, "uploadImages");
            UploadImagesRequest request = new UploadImagesRequest(globalId, LogPref.getLogin(getBaseContext()),
                    LogPref.getPassword(getBaseContext()),catalog,  getBaseContext());
            HttpURLConnection connection = null;
            try {
                connection = request.start();
                InputStream stream = connection.getInputStream();
                stream.close();
                //TODO zrobić pobieranie odpowiedzi
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return -1;
        }

        private long uploadRecords(long globalId, String catalog) throws IOException, JSONException {
            Log.d(LOG_TAG, "uploadRecords");
            UploadRecordRequest request = new UploadRecordRequest(globalId, LogPref.getLogin(getBaseContext()),
                    LogPref.getPassword(getBaseContext()), catalog, getBaseContext());
            HttpURLConnection connection = null;
            try{
                connection = request.start();
                InputStream stream = connection.getInputStream();
                stream.close();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return -1;
        }
    }
}
