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

    private Callback mCallback;
    private final LocalBinder mLocalBinder = new LocalBinder();

    private int mRunningTasks;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public class LocalBinder extends Binder {
        public DownloadSetsService getService(){
            return DownloadSetsService.this;
        }
    }

    public interface Callback{
        void onOperationProgress(long setId,int progress);
        void onOperationCompleted(long setId);
    }

    public void setCallback(Callback callback){

        mCallback = callback;
    }

    public boolean isRunning(){
        return mRunningTasks >0;
    }

    @Override
    public void onDestroy(){
        Log.d(getClass().getSimpleName(), "Ała. Umieram");
    }

    public void startDownloading(long setId, String name, boolean database, boolean images, boolean records){
        DownloadAsyncTask task = new DownloadAsyncTask(getBaseContext()/*, mCallback*/, LingApplication.getInstance().getDataManager());
        DownloadingParams params = new DownloadingParams(setId, name, database, images, records);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    private class DownloadAsyncTask extends AsyncTask<DownloadingParams, Integer, Void>{

        private int mNotificationId;
        private Context mContext;
        private DataManager mDataManager;
        private Notification.Builder mNotificationBuilder;
        private NotificationManager mNotificationManager;

        private long mSetId;
        private String mName;

        public DownloadAsyncTask(Context context, DataManager dataManager){
            mContext = context;
            mDataManager = dataManager;
        }

        @Override
        protected void onPreExecute(){
            //większamy liczbę działających zadań
            mRunningTasks++;
            Log.d(getClass().getSimpleName(), "Rozpoczeto zadanie. Liczba zadań: " + mRunningTasks);
        }

        @Override
        protected Void doInBackground(DownloadingParams... params) {
            mSetId = params[0].getSetId();
            mName = params[0].getName();
            boolean database = params[0].isDatabase();
            boolean images = params[0].isImages();
            boolean records = params[0].isRecords();
            if(database){
                downloadDatabase(mSetId, mName);
            }
            if(images){
                downloadImages(mSetId, mName);
            }
            if(records){
                downloadRecords(mSetId, mName);
            }
            return null;
        }

        private void downloadDatabase(long setId, String name){
            DownloadSetRequest request = new DownloadSetRequest(setId, LogPref.getLogin(mContext), LogPref.getPassword(mContext));
            try {
                createNotification(name, mContext.getString(R.string.database));
                DownloadSetResponse response = new DownloadSetResponse(request.start());
                VocabularySet set = saveData(response, mDataManager);
                saveSetInfo(setId, set, mDataManager);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private VocabularySet saveData(DownloadSetResponse response, DataManager dataManager) throws IOException, JSONException, ParseException {
            int wordsCount = response.getWordsCount();
            dataManager.startTransaction();
            VocabularySet set = saveSet(response,dataManager);
            saveLessons(response, set, dataManager);
            saveWords(response, wordsCount,dataManager);
            dataManager.finishTranslation();
            return set;
        }

        private void saveSetInfo(long setId, VocabularySet set, DataManager dataManager){
            //zapisujemy w bazie, że zestaw został pobrany
            //dataManager.updateSetUploaded(false, set.getId());
            //TODO zobaczyć czy to będzie grało
            dataManager.updateUploadingUser(null, set.getId());
            dataManager.insertSetGlobalId(setId,set.getId());
        }

        private VocabularySet saveSet(DownloadSetResponse response, DataManager dataManager) throws JSONException, IOException, ParseException {
            VocabularySet set = SetCreator.createFromJson(response.getSetJson());
            String setCatalog = FileNameCreator.getCatalogName(set.getName(), FileSystem.getPath("", mContext), mContext);
            set.setCatalog(setCatalog);
            long setId = dataManager.saveSet(set);
            set.setId(setId);
            return set;
        }

        private void saveLessons(DownloadSetResponse response, VocabularySet set, DataManager dataManager) throws IOException {
            JsonNode node;
            while((node = response.getLessonJson()) != null){
                Lesson lesson = LessonCreator.createFromJson(node);
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
            //TODO możnaby było sprawdzać czy dany numer został już wylosowany
            Random random = new Random();
            return random.nextInt(9999);
        }

        private void createNotification(String setName, String whatDownloading){
            mNotificationId = randomNotificationNumber();
            if(mNotificationManager == null){
                mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if(mNotificationBuilder == null){
                mNotificationBuilder= new Notification.Builder(mContext.getApplicationContext());
            }
            mNotificationBuilder.setOngoing(true)
                    .setContentTitle(setName)
                    .setContentText(mContext.getString(R.string.downloading) + " : " + whatDownloading)
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

        private void downloadImages(long setId, String name){
            downloadMedia(setId, name, MediaType.IMAGES);
        }

        private void downloadRecords(long setId, String name){
            downloadMedia(setId, name, MediaType.RECORDS);
        }

        private void downloadMedia(final long setId, String name, final MediaType mediaType){
            createMediaNotification(name, mediaType);
            DownloadMediaRequest request = new DownloadMediaRequest(setId, LogPref.getLogin(mContext),
                    LogPref.getPassword(mContext), mediaType);
            try {
                DownloadMediaResponse response = new DownloadMediaResponse(request.start(), mediaType.getValue(), mContext);
                saveMedia(response, setId, mediaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void createMediaNotification(String setName, MediaType mediaType){
            switch (mediaType){
                case IMAGES:
                    createNotification(setName, mContext.getString(R.string.images));
                case RECORDS:
                    createNotification(setName, mContext.getString(R.string.records));
            }
        }

        private void saveMedia(DownloadMediaResponse response,final long setId,final MediaType mediaType) throws IOException {
            final long contentLength = response.getContentLength();
            final long[] downloadedContent = new long[1];
            String catalog = getCatalog(setId);
            response.saveFile(catalog, new DownloadMediaResponse.SaveCallback() {
                @Override
                public void onSave(int length) {
                    downloadedContent[0] += length;
                    int progress = (int)((downloadedContent[0]*100)/contentLength);
                    publishProgress(progress);
                }

                @Override
                public void onSaveCompleted() {
                    //TODO zobaczyć czy zamiast tego bedzie trzeba zrobić coś innego

                    //saveMediaInfo(setId, mediaType, mDataManager);
                }
            });
        }

        private String getCatalog(long globalId){
            return mDataManager.getSetCatalogByGlobalId(globalId);
        }

        /*private void saveMediaInfo(long setId, MediaType mediaType, DataManager dataManager){
            switch (mediaType){
                case IMAGES:
                    dataManager.updateSetImagesDownloaded(true, setId); break;
                case RECORDS:
                    dataManager.updateRecordsUploaded(true, setId); break;
            }
        }*/

        @Override
        public void onProgressUpdate(Integer... progress){
            if(progress[0]!=100){
                setNotificationProgress(progress[0]);
            } else {
                //TODO przerobić notyfikację żeby była cacy
                mNotificationBuilder.setProgress(0,0,false)
                        .setContentText(mName + "\n" + mContext.getString(R.string.finished));
            }
            if(mCallback != null){
                mCallback.onOperationProgress(mSetId, progress[0]);
            }
            mNotificationManager.notify(mNotificationId, buildNotification());
        }

        @Override
        public void onPostExecute(Void result){
            mRunningTasks--;
            Log.d(getClass().getSimpleName(),"Zakończono zadanie. Liczba zadań: " + mRunningTasks);
            if(mCallback != null){
                mCallback.onOperationCompleted(mSetId);
            } else {
                //jeżeli callback nie jest ustawiony pznacza to że usługa nie jest połączona z żadną aktywnością
                //i nie może byc przez nią zamknięta. Z tego powodu musi się zamknąć sama
                if(mRunningTasks == 0){
                    Log.d(getClass().getSimpleName(), "Zatrzymuje się. Do widzenia");
                    stopSelf();
                }
            }
        }
    }
}

class DownloadingParams{
    private long mSetId;
    private String mName;
    private boolean mDatabase;
    private boolean mImages;
    private boolean mRecords;

    public DownloadingParams(long setId, String name, boolean database, boolean images, boolean records){
        mSetId = setId;
        mName = name;
        mDatabase = database;
        mImages = images;
        mRecords = records;
    }

    public long getSetId(){return mSetId;}
    public String getName(){return mName;}
    public boolean isDatabase(){return mDatabase;}
    public boolean isImages(){return mImages;}
    public boolean isRecords(){return mRecords;}

}


