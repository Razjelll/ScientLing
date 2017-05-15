package com.dyszlewskiR.edu.scientling.services.net.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.file.FileNameCreator;
import com.dyszlewskiR.edu.scientling.data.file.FileSystem;
import com.dyszlewskiR.edu.scientling.data.models.creators.LessonCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.WordCreator;
import com.dyszlewskiR.edu.scientling.data.models.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.DownloadImagesRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.DownloadMediaRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.DownloadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.DownloadMediaResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.DownloadSetResponse;
import com.dyszlewskiR.edu.scientling.services.net.values.MediaType;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Random;

public class DownloadSetsService extends Service {

    public static final String CUSTOM_INTENT = "DownloadSetsIntent";

    private Notification.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private int mNotificationId;
    private String mSetName;
    private long mSetId;


    private boolean mDatabaseDownload;
    private boolean mImagesDownload;
    private boolean mRecordsDownload;
   // private LocalBinder mLocalBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mSetId = intent.getLongExtra("id", -1);
        mSetName = intent.getStringExtra("name");
        mDatabaseDownload = intent.getBooleanExtra("database", false);
        mImagesDownload = intent.getBooleanExtra("images", false);
        mRecordsDownload = intent.getBooleanExtra("records", false);
        if(mDatabaseDownload){
            Log.d(getClass().getSimpleName(), "download Database");
            DownloadSetAsyncTask task = new DownloadSetAsyncTask();
            task.execute(new TaskParams(mSetId, mSetName));
            //jeżeli powiniśmy ściągnąć baze wraz z obrazkami i nagraniami nie robimy tego w tym miejscu
            //ponieważ najpierw musi zostać pobrana baza. Uruchomienie ściągania obrazków i nagrań
            //nastąpi po ściągnięciu bazy
        } else {
            startMediaAsyncTask();
        }

        return Service.START_NOT_STICKY;
    }

    private void startMediaAsyncTask(){
        if(mImagesDownload){
            DownloadMediaAsyncTask imagesTask = new DownloadMediaAsyncTask(MediaType.IMAGES);
            imagesTask.execute(mSetId);
        }
        if(mRecordsDownload){
            DownloadMediaAsyncTask recordsTask = new DownloadMediaAsyncTask(MediaType.RECORDS);
            recordsTask.execute(mSetId);
        }
    }

    public void onDatabaseDownload(long setId){
        startMediaAsyncTask();
    }

    private class TaskParams{
        private long mId;
        private String mName;

        public TaskParams(long id, String name){
            mId = id;
            mName = name;
        }

        public long getId(){return mId;}
        public String getName(){return mName;}
    }

    public class LocalBinder extends Binder {
        public DownloadSetsService getService(){
            return DownloadSetsService.this;
        }
    }

    private class DownloadSetAsyncTask extends AsyncTask<TaskParams, Integer, Void> {

        @Override
        protected Void doInBackground(TaskParams... params) {
            int savedWords = 0;
            DownloadSetRequest request = new DownloadSetRequest(params[0].getId(), LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()));
            try {
                mNotificationId = randomNotificationNumber();
                createNotification(params[0].getName());
                DownloadSetResponse response = new DownloadSetResponse(request.start());
                DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
                int wordsCount = response.getWordsCount();
                dataManager.startTransaction();
                VocabularySet set = saveSet(response,dataManager);
                saveLessons(response, set, dataManager);
                saveWords(response, wordsCount,dataManager);
                dataManager.finishTranslation();
                //zapisujemy w bazie, że zestaw został pobrany
                dataManager.updateSetUploaded(false, set.getId());
                dataManager.insertSetGlobalId(params[0].getId(),set.getId());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private VocabularySet saveSet(DownloadSetResponse response, DataManager dataManager) throws JSONException, IOException, ParseException {
            VocabularySet set = SetCreator.createFromJson(response.getSetJson());
            String setCatalog = FileNameCreator.getCatalogName(set.getName(),getBaseContext().getFilesDir().getAbsolutePath());
            set.setCatalog(setCatalog);
            long setId = dataManager.saveSet(set);
            set.setId(setId);
            return set;
        }

        private void saveLessons(DownloadSetResponse response, VocabularySet set, DataManager dataManager) throws IOException {
            JsonNode node;
            while((node = response.getLessonJson()) != null){
                Lesson lesson = LessonCreator.createFromJson(node);
                //lesson.setSet(set);
                lesson.setSetId(set.getId());
                dataManager.saveLesson(lesson);
            }
        }

        private void saveWords(DownloadSetResponse response,int numWords, DataManager dataManager) throws IOException {
            JsonNode node;
            int savedWords = 0;
            Word word;
            while((node = response.getWordJson()) != null){
                word = WordCreator.createFromJson(node);
                long lessonId = dataManager.getLessonId(word.getLessonId());
                word.setLessonId(lessonId);
                dataManager.saveWord(word);
                savedWords++;
                publishProgress(savedWords*100/numWords);
            }
        }

        private int randomNotificationNumber(){
            Random random = new Random();
            return random.nextInt(9999);
        }

        private void createNotification(String setName){
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationBuilder= new Notification.Builder(getApplicationContext());
            mNotificationBuilder.setOngoing(true)
                    .setContentTitle(getString(R.string.downloading))
                    .setContentText(setName)
                    .setSmallIcon(R.drawable.ic_hint)
                    .setProgress(100,0,false);
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        private void setNotificationProgress(int progress){
            mNotificationBuilder.setProgress(100, progress, false);
        }

        private Notification buildNotification(){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                return mNotificationBuilder.getNotification();
            } else {
                return mNotificationBuilder.build();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            Log.d("AsyncTask", String.valueOf(progress[0]));
            if(progress[0]!=100){
                setNotificationProgress(progress[0]);
            } else {
                mNotificationBuilder.setProgress(0,0,false)
                        .setContentText(mSetName + "\n" + getString(R.string.finished));
            }
            Intent intent = new Intent();
            intent.setAction(CUSTOM_INTENT);
            intent.putExtra("set", mSetId);
            intent.putExtra("progress", progress[0]);
            sendBroadcast(intent);
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        @Override
        protected void onPostExecute(Void result){
            onDatabaseDownload(mSetId);
        }
    }

    private class DownloadMediaParam{
        private long mId;
        private String mCatalog;

        public DownloadMediaParam(long id, String catalog){
            mId = id;
            mCatalog = catalog;
        }

        public long getId(){return mId;}
        public void setId(long id){mId = id;}

        public String getCatalog(){return mCatalog;}
        public void setCatalog(String catalog){mCatalog = catalog;}
    }

    private class DownloadMediaAsyncTask extends AsyncTask<Long, Integer, Void> implements DownloadMediaResponse.Callback{

        private MediaType mMediaType;
        private long mSetGlobalId;
        private long mContentLength;
        private long mProgress;

        public DownloadMediaAsyncTask(MediaType mediaType){
            mMediaType = mediaType;
        }

        @Override
        protected Void doInBackground(Long... params) {
            mSetGlobalId = params[0];
            createNotification("Obrazki");
            DownloadMediaRequest request = new DownloadMediaRequest(mSetGlobalId, LogPref.getLogin(getBaseContext()),
                    LogPref.getPassword(getBaseContext()), mMediaType);
            try {
                DownloadMediaResponse response = new DownloadMediaResponse(request.start(), mMediaType.getValue(), getBaseContext());
                mContentLength = response.getContentLenght();
                String catalog = getCatalog(mSetGlobalId);
                response.saveFile(catalog);
                /*response.startSavingFiles("Katalog");
                while(response.next()){
                    response.saveFile();
                }
                response.release();*/
                response.closeConnection();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getCatalog(long globalId){
            DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
            return dataManager.getSetCatalog(globalId);
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            Log.d("AsyncTask", String.valueOf(progress[0]));
            if(progress[0]!=100){
                setNotificationProgress(progress[0]);
            } else {
                mNotificationBuilder.setProgress(0,0,false)
                        .setContentText(mSetName + "\n" + getString(R.string.finished));
            }
            Intent intent = new Intent();
            intent.setAction(CUSTOM_INTENT);
            intent.putExtra("set", mSetId);
            intent.putExtra("progress", progress[0]);
            sendBroadcast(intent);
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        private void createNotification(String setName){
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationBuilder= new Notification.Builder(getApplicationContext());
            mNotificationBuilder.setOngoing(true)
                    .setContentTitle(getString(R.string.downloading))
                    .setContentText(setName)
                    .setSmallIcon(R.drawable.ic_hint)
                    .setProgress(100,0,false);
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        private void setNotificationProgress(int progress){
            mNotificationBuilder.setProgress(100, progress, false);
        }

        private Notification buildNotification(){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                return mNotificationBuilder.getNotification();
            } else {
                return mNotificationBuilder.build();
            }
        }

        @Override
        protected void onPostExecute(Void result){
            DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
            switch (mMediaType){
                case IMAGES:
                    dataManager.updateSetImagesDownloaded(true,mSetGlobalId); break;
                case RECORDS:
                    dataManager.updateSetRecordsDownloaded(true, mSetGlobalId); break;
            }
            mNotificationBuilder.setProgress(0,0,false)
                    .setContentText(mSetName + "\n" + getString(R.string.finished));

        Intent intent = new Intent();
            intent.setAction(CUSTOM_INTENT);
            intent.putExtra("set", mSetId);
            intent.putExtra("progress",100);
        sendBroadcast(intent);
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        private int mLastProgress;

        @Override
        public void getProgress(long progress) {
            mProgress += progress;
            int progressValue = (int)((mProgress*100/mContentLength));
            if(mLastProgress != progressValue){
                publishProgress(progressValue);
                mLastProgress = progressValue;
            }
        }
    }


}
