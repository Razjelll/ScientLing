package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.file.FileSizeCalculator;
import com.dyszlewskiR.edu.scientling.data.file.SizeConverter;
import com.dyszlewskiR.edu.scientling.data.models.models.SetItem;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.net.requests.RatingRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.SetDetailRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.RatingResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.SetDetailResponse;
import com.dyszlewskiR.edu.scientling.utils.ResourceUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SetDetailsActivity extends AppCompatActivity {

    private final int ONE_MB = 1048576;

    private TextView mNameTextView;
    private Button mDownloadButton;
    private TextView mL1TextView;
    private TextView mL2TextView;
    private TextView mAuthorTextView;
    private TextView mRatingTextView;
    private TextView mDownloadCountTextView;
    private TextView mAddedDateTextView;
    private TextView mMoreTextView;

    private View mDescriptionContainer;
    private TextView mDescriptionTextView;
    private TextView mNumWordsTextView;
    private TextView mSizeTextView;
    private TextView mImagesSizeTextView;
    private TextView mRecordsSizeTextView;

    private RatingBar mRatingBar;
    private TextView mUploadRatingButton;

    private long mSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_details);
        setupToolbar();
        setupControls();
        mSetId = getSetId();
        GetSetAsyncTask task = new GetSetAsyncTask();
        task.execute(mSetId);
        setListeners();
    }

    private long getSetId(){
        Intent intent = getIntent();
        return intent.getLongExtra("id", -1);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupControls(){
        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mDownloadButton = (Button)findViewById(R.id.download_button);
        mL1TextView = (TextView)findViewById(R.id.l1_text_view);
        mL2TextView = (TextView)findViewById(R.id.l2_text_view);
        mAuthorTextView = (TextView)findViewById(R.id.author_text_view);
        mRatingTextView = (TextView)findViewById(R.id.rating_text_view);
        mDownloadCountTextView = (TextView)findViewById(R.id.download_count_text_view);
        mAddedDateTextView = (TextView)findViewById(R.id.added_date_text_view);
        mMoreTextView = (TextView)findViewById(R.id.show_more_text_view);
    }

    private void fillControls(SetItem item){
        mNameTextView.setText(item.getName());
        mL1TextView.setText(ResourceUtils.getString(item.getLanguageL1(), getBaseContext()));
        mL2TextView.setText(ResourceUtils.getString(item.getLanguageL2(), getBaseContext()));
        mAuthorTextView.setText(item.getAuthor());
        mRatingTextView.setText(String.valueOf(item.getRating()));
        mDownloadCountTextView.setText(String.valueOf(item.getDownloads()));
        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        if(item.getAddedDate() != null){
            mAddedDateTextView.setText(format.format(item.getAddedDate()));
        }

        //jeśli użytkownik pobrał dany zestaw wyświetlamy część pozwalającą ocenić zestaw
        if(item.getWasDownloaded() != null && item.getWasDownloaded()){
            ViewStub stub = (ViewStub)findViewById(R.id.rating_container);
            ViewGroup ratingContainer = (ViewGroup)stub.inflate();
            mRatingBar = (RatingBar)ratingContainer.findViewById(R.id.rating_bar);
            mUploadRatingButton = (TextView)ratingContainer.findViewById(R.id.upload_rating_button);

            if(item.getUserRating() > 0){
                mRatingBar.setRating(item.getUserRating());
            }
            mUploadRatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rating = (int)mRatingBar.getRating();
                    RatingParam param = new RatingParam(mSetId, rating, LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()));
                    RatingAsyncTask task = new RatingAsyncTask();
                    task.execute(param);
                }
            });
        }

        if(mDescriptionContainer != null){
            mDescriptionTextView.setText(item.getDescription());
            mNumWordsTextView.setText(String.valueOf(item.getWordsCount()));
            if(item.getBasicSize() < ONE_MB){
                String size = String.format("%.2f", SizeConverter.bytesToKb(item.getBasicSize()));
                mSizeTextView.setText(size + " kb");
            } else {
                String size = String.format("%.2f", SizeConverter.bytesToMB(item.getBasicSize()));
                mSizeTextView.setText(size +" MB");
            }
            String imagesSize = String.format("%.2f", SizeConverter.bytesToMB(item.getImagesSize()));
            mImagesSizeTextView.setText(imagesSize + " MB");
            String recordsSize = String.format("%.2f", SizeConverter.bytesToMB(item.getRecordsSize()));
            mRecordsSizeTextView.setText(recordsSize + " MB");
        }
    }

    private void setListeners(){
        mMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDescriptionContainer == null){
                    ViewStub stub = (ViewStub)findViewById(R.id.more_container);
                    mDescriptionContainer = stub.inflate();
                    mDescriptionTextView = (TextView)mDescriptionContainer.findViewById(R.id.description_text_view);
                    mNumWordsTextView = (TextView)mDescriptionContainer.findViewById(R.id.number_words_text_view);
                    mSizeTextView = (TextView)mDescriptionContainer.findViewById(R.id.size_text_view);
                    mImagesSizeTextView = (TextView)mDescriptionContainer.findViewById(R.id.images_size_text_view);
                    mRecordsSizeTextView = (TextView)mDescriptionContainer.findViewById(R.id.records_size_text_view);

                    mMoreTextView.setText(getString(R.string.less_information));
                    //TODO uruchiomić zadanie które pobierze informacje

                    GetSetAsyncTask task = new GetSetAsyncTask();
                    task.execute(mSetId);
                } else if(mDescriptionContainer.getVisibility()==View.VISIBLE){
                    mDescriptionContainer.setVisibility(View.GONE);
                    mMoreTextView.setText(getString(R.string.more_information));
                } else {
                    mDescriptionContainer.setVisibility(View.VISIBLE);
                    mMoreTextView.setText(getString(R.string.less_information));
                }
            }
        });
    }

    private class GetSetAsyncTask extends AsyncTask<Long, Void, SetItem>{

        @Override
        protected SetItem doInBackground(Long... params) {
            SetDetailRequest request = new SetDetailRequest(params[0], LogPref.getLogin(getBaseContext()), LogPref.getPassword(getBaseContext()));
            try {
                HttpURLConnection connection = request.start();
                SetDetailResponse response = new SetDetailResponse(connection);
                if(response.getResultCode()==SetDetailResponse.OK){
                    return response.getSet();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(SetItem result){
            if(result != null){
                fillControls(result);
            }
        }
    }

    private class RatingParam{
        private long mSetId;
        private int mRating;
        private String mUsername;
        private String mPassword;

        public RatingParam(long setId, int rating, String username, String password){
            mSetId = setId;
            mRating = rating;
            mUsername = username;
            mPassword = password;
        }

        public long getSetId(){return mSetId;}
        public int getRating(){return mRating;}
        public String getUsername(){return mUsername;}
        public String getPassword(){return mPassword;}
    }

    private class RatingAsyncTask extends AsyncTask<RatingParam, Void, Boolean>{

        @Override
        protected Boolean doInBackground(RatingParam... params) {
            HttpURLConnection connection = null;
            try {
                connection = RatingRequest.start(params[0].getSetId(), params[0].getRating(),
                        params[0].getUsername(), params[0].getPassword());
                RatingResponse response = new RatingResponse(connection);
                if(response.getResultCode()>0){
                    response.getResponse();
                    response.closeConnection();
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                Toast.makeText(getBaseContext(), "Ocena została wystawiona", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Wystąpił błąd przy wysyłaniu oceny", Toast.LENGTH_SHORT).show();
            }
            //TODO dodać napisy do stringu

        }
    }

}
