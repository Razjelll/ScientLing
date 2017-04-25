package com.dyszlewskiR.edu.scientling.services.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.file.FileNameCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.LessonCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.WordCreator;
import com.dyszlewskiR.edu.scientling.data.models.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.models.Word;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.DownloadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.DownloadSetResponse;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Razjelll on 24.04.2017.
 */

public class DownloadSetsService extends Service {

    private Notification.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private int mNotificationId;
    private String mSetName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        long setId = intent.getLongExtra("id", -1);
        mSetName = intent.getStringExtra("name");
        DownloadSetAsyncTask task = new DownloadSetAsyncTask();
        task.execute(new TaskParams(setId, mSetName));
        return Service.START_NOT_STICKY;
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

    private class DownloadSetAsyncTask extends AsyncTask<TaskParams, Integer, Void> {

        @Override
        protected Void doInBackground(TaskParams... params) {
            int savedWords = 0;
            DownloadSetRequest request = new DownloadSetRequest(params[0].getId());
            try {
                mNotificationId = randomNotificationNumber();
                createNotification(params[0].getName());
                DownloadSetResponse response = new DownloadSetResponse(request.start());
                DataManager dataManager = ((LingApplication)getApplication()).getDataManager();
                int wordsCount = response.getWordsCount();
                /*VocabularySet set = SetCreator.createFromJson(response.getSetJson());
                String setCatalog = FileNameCreator.getCatalogName(set.getName(),getBaseContext().getFilesDir().getAbsolutePath());
                set.setCatalog(setCatalog);*/
                dataManager.startTransaction();
                VocabularySet set = saveSet(response,dataManager);
                saveLessons(response, set, dataManager);
                saveWords(response, wordsCount,dataManager);

                dataManager.finishTranslation();
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
                lesson.setSet(set);
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
            mNotificationManager.notify(mNotificationId, buildNotification());
        }
    }
}
