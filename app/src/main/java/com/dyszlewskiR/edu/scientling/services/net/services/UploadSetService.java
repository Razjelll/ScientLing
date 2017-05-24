package com.dyszlewskiR.edu.scientling.services.net.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.file.FileSizeCalculator;
import com.dyszlewskiR.edu.scientling.data.file.MediaFileSystem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.MediaSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadImagesRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadRecordRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.UploadSetResponse;
import com.dyszlewskiR.edu.scientling.services.net.values.MediaType;
import com.dyszlewskiR.edu.scientling.services.net.writers.FileWriter;
import com.dyszlewskiR.edu.scientling.services.net.writers.SetWriter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UploadSetService extends Service {
    private final String LOG_TAG = "UploadSetService";

    private Callback mCallback;
    private int mRunningTasks;
    private final LocalBinder mLocalBinder = new LocalBinder();

    public interface Callback {
        void onOperationProgress(int progress);
        void onOperationCompleted(long setId, long globalId, OperationParts parts);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public class LocalBinder extends Binder {
        public UploadSetService getService() {
            return UploadSetService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public boolean isRunning() {
        return mRunningTasks > 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    public void upload(long setId, Long globalId, String description, boolean database, boolean images, boolean records) {
        UploadSetAsyncTask task = new UploadSetAsyncTask();
        long setGlobalId = globalId != null ? globalId : -1;
        UploadParams params = new UploadParams(setId, setGlobalId, description, database, images, records);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    private class UploadParams {
        private long mSetId;
        private Long mSetGlobalId;
        private String mDescription;
        private boolean mUploadDatabase;
        private boolean mUploadImages;
        private boolean mUploadRecords;

        public UploadParams(long setId, long globalId, String description, boolean database, boolean images, boolean records) {
            mSetId = setId;
            mSetGlobalId = globalId;
            mUploadDatabase = database;
            mUploadImages = images;
            mUploadRecords = records;
            mDescription = description;
        }

        public long getSetId() {
            return mSetId;
        }

        public long getSetGlobalId() {
            return mSetGlobalId;
        }

        public String getDescription() {
            return mDescription;
        }

        public boolean isUploadDatabase() {
            return mUploadDatabase;
        }

        public boolean isUploadImages() {
            return mUploadImages;
        }

        public boolean ismUploadRecords() {
            return mUploadRecords;
        }

        public void setSetId(long setId) {
            mSetId = setId;
        }
    }

    private class UploadSetAsyncTask extends AsyncTask<UploadParams, Integer, Long> {

        private boolean mUploadDatabase;
        private boolean mUploadImages;
        private boolean mUploadRecords;
        private long mSetId;

        @Override
        protected Long doInBackground(UploadParams... params) {
            mRunningTasks++;
            mSetId = params[0].getSetId();
            long setGlobalId = params[0].getSetGlobalId();
            DataManager dataManager = LingApplication.getInstance().getDataManager();
            mUploadDatabase = params[0].isUploadDatabase();
            mUploadImages = params[0].isUploadImages();
            mUploadRecords = params[0].ismUploadRecords();
            if (mUploadDatabase) {
                setGlobalId = uploadDatabase(mSetId, params[0].getDescription(), dataManager);
                if (setGlobalId > 0) {
                    Log.d(LOG_TAG, "Wysyłanie powiodło sie");
                    dataManager.insertSetGlobalId(setGlobalId, mSetId);
                    dataManager.updateUploadingUser(LogPref.getLogin(getBaseContext()), mSetId);
                } else {
                    Log.d(LOG_TAG, "Global id mniejsze od zera. Wysyłanie się nie udało");
                    return null;
                }
            }
            String catalog = null;
            if (mUploadImages) {
                if (catalog == null) {
                    VocabularySet set = dataManager.getSetById(mSetId);
                    catalog = set.getCatalog();
                }
                try {
                    uploadImages(setGlobalId, catalog);
                    dataManager.updateImageUploaded(true, setGlobalId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mUploadRecords) {
                if (catalog == null) {
                    VocabularySet set = dataManager.getSetById(mSetId);
                    catalog = set.getCatalog();
                }
                try {
                    uploadRecords(setGlobalId, catalog);
                    dataManager.updateRecordsUploaded(true, setGlobalId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return setGlobalId;
        }

        private long uploadDatabase(long setId, String description, DataManager dataManager) {
            HttpURLConnection connection = null;
            long setGlobalId = -1;
            try {
                final int[] wordsCount = new int[1];
                final int[] wordsUploaded = new int[1];
                connection = UploadSetRequest.start(LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()));
                SetWriter setWriter = new SetWriter(connection.getOutputStream(), UploadSetRequest.CHUNK_SIZE, dataManager);
                setWriter.setCallback(new SetWriter.Callback() {
                    @Override
                    public void addWord() {
                        wordsUploaded[0]++;
                        publishProgress(wordsUploaded[0] * 100 / wordsCount[0]);
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
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return setGlobalId;
        }

        private long uploadImages(long globalId, String catalog) throws IOException, JSONException {
            Log.d(LOG_TAG, "uploadImages");
            UploadImagesRequest request = new UploadImagesRequest(globalId, LogPref.getLogin(getBaseContext()),
                    LogPref.getPassword(getBaseContext()), catalog, getBaseContext());
            HttpURLConnection connection = null;
            try {
                connection = request.start();
                String imagesCatalogPath = MediaFileSystem.getMediaPath(catalog, MediaType.IMAGES, getBaseContext());
                uploadFiles(imagesCatalogPath, connection.getOutputStream());
                InputStream stream = connection.getInputStream();
                stream.close();
                //TODO zrobić pobieranie odpowiedzi
            } finally {
                if (connection != null) {
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
                    publishProgress((int) (totalUploadedBytes[0] * 100 / imagesSize[0]));
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
            try {
                connection = request.start();
                String recordsFolderPath = MediaFileSystem.getMediaPath(catalog, MediaType.RECORDS, getBaseContext());
                uploadFiles(recordsFolderPath, connection.getOutputStream());
                InputStream stream = connection.getInputStream();
                stream.close();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return -1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d(LOG_TAG, "Postęp serwis " + progress[0]);
            if (mCallback != null) {
                mCallback.onOperationProgress(progress[0]);
            }
        }

        @Override
        protected void onPostExecute(Long globalId) {
            mRunningTasks--;
            if (mCallback != null) {
                OperationParts parts = new OperationParts();
                parts.setDatabase(mUploadDatabase);
                parts.setImages(mUploadImages);
                parts.setRecords(mUploadRecords);
                mCallback.onOperationCompleted(mSetId, globalId, parts);
            } else { //jeżeli callback == null
                //w przypadku gdy aktywność która wywołała metode nie jest na pierwszym planie zamykamy
                //usługę po wykonaniu pracy, ponieważ jeżeli tego nie zrobimy usługa nie zostanie
                // zamknięta
                if (!isRunning()) {
                    stopSelf();
                }
            }
        }
    }
}
