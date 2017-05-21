package com.dyszlewskiR.edu.scientling.services.net.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.file.FileSizeCalculator;
import com.dyszlewskiR.edu.scientling.data.file.FileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.MediaSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadImagesRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadRecordRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.UploadSetResponse;
import com.dyszlewskiR.edu.scientling.services.net.writers.FileWriter;
import com.dyszlewskiR.edu.scientling.services.net.writers.SetWriter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UploadSetService extends Service {
    private final String LOG_TAG = "UploadSetService";

    public static final String CUSTOM_INTENT = "UploadSetIntent";

    public static final String BROADCAST_ACTION = "UploadSetService";

    private long mSetId;
    private long mSetGlobalId;
    private String mSetName;
    private String mDescription;

    private Callback mCallback;
    private final LocalBinder mLocalBinder = new LocalBinder();
    private boolean mIsRunning;

    public interface Callback{
        void onOperationProgress(int progress);
        void onOperationCompleted();
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public class LocalBinder extends Binder {
        public UploadSetService getService(){
            return UploadSetService.this;
        }
    }

    public boolean isRunning(){
        return mIsRunning;
    }

    @Override
    public IBinder onBind(Intent intent){
        return mLocalBinder;
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

    private class UploadSetAsyncTask extends AsyncTask<UploadParams, Integer, Void>{

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
                if(setGlobalId>0){
                    Log.d(LOG_TAG, "Wysyłanie powiodło sie");
                    dataManager.insertSetGlobalId(setGlobalId, setId);
                    dataManager.updateSetUploaded(true, setId);
                    //TODO tutaj można wrzucić to w pętle i wykonywać aż się globalId zapisze
                    broadcastIntent.putExtra("globalId", setGlobalId);
                } else {
                    Log.d(LOG_TAG, "Global id mniejsze od zera. Wysyłanie się nie udało");
                    return null;
                }

            }
            if(params[0].isUploadImages()){
                VocabularySet set = dataManager.getSetById(setId);
                String catalog = set.getCatalog();
                try {
                    long imagesSize = FileSizeCalculator.calculate(FileSystem.getPath(catalog, getBaseContext()));
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
            //UploadSetRequest request = new UploadSetRequest(setId,description, LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()),getBaseContext(),dataManager);
            HttpURLConnection connection = null;
            long setGlobalId = -1;
            try {
                final int[] wordsCount = new int[1];
                final int[] wordsUploaded = new int[1];
                connection = UploadSetRequest.start(LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()));
                SetWriter setWriter = new SetWriter(connection.getOutputStream(),UploadSetRequest.CHUNK_SIZE, dataManager);
                setWriter.setCallback(new SetWriter.Callback() {
                    @Override
                    public void addWord() {
                        wordsUploaded[0]++;
                        publishProgress(wordsUploaded[0]*100/wordsCount[0]);
                    }
                    @Override
                    public void getWordsCount(int count) {
                        wordsCount[0] = count;
                    }
                });
                setWriter.startWriting(setId, description);
                setWriter.close();

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
                String imagesCatalogPath = FileSystem.getImagePath(catalog, getBaseContext());
                uploadFiles(imagesCatalogPath, connection.getOutputStream());


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

        private void uploadFiles(String folderPath, OutputStream outputStream) throws IOException {
            final long[] imagesSize = {FileSizeCalculator.calculate(folderPath)};
            final long[] totalUploadedBytes = new long[1];
            FileWriter fileWriter = new FileWriter(outputStream, MediaSetRequest.CHUNK_SIZE);
            fileWriter.setCallback(new FileWriter.Callback() {
                @Override
                public void onProgressUpdate(long uploadedBytes) {
                    totalUploadedBytes[0] += uploadedBytes;
                    publishProgress((int)(totalUploadedBytes[0]*100/imagesSize[0]));
                }
            });
            fileWriter.startZipWriting(folderPath);
            fileWriter.close();
        }

        private long uploadRecords(long globalId, String catalog) throws IOException, JSONException {
            Log.d(LOG_TAG, "uploadRecords");
            UploadRecordRequest request = new UploadRecordRequest(globalId, LogPref.getLogin(getBaseContext()),
                    LogPref.getPassword(getBaseContext()), catalog, getBaseContext());
            HttpURLConnection connection = null;
            try{
                connection = request.start();
                String recordsFolderPath = FileSystem.getRecordsPath(catalog, getBaseContext());
                uploadFiles(recordsFolderPath, connection.getOutputStream());
                InputStream stream = connection.getInputStream();
                stream.close();

            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return -1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            Log.d(LOG_TAG, "Postęp serwis "+ progress[0]);
            if(mCallback != null){
                mCallback.onOperationProgress(progress[0]);
            }
        }

        @Override
        protected void onPostExecute(Void result){
            if(mCallback != null){
                mCallback.onOperationCompleted();
            }
        }
    }
}
