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
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.models.SetItem;
import com.dyszlewskiR.edu.scientling.services.net.requests.SetDetailRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.SetDetailResponse;
import com.dyszlewskiR.edu.scientling.utils.ResourceUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SetDetailsActivity extends AppCompatActivity {

    private TextView mNameTextView;
    private Button mDownloadButton;
    private TextView mL1TextView;
    private TextView mL2TextView;
    private TextView mAuthorTextView;
    private TextView mDescriptionTextView;
    private TextView mNumWordsTextView;
    private TextView mSizeTextView;
    private TextView mImagesSizeTextView;
    private TextView mRecordsSizeTextView;
    private TextView mRatingTextView;
    private TextView mDownloadCountTextView;
    private TextView mAddedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_details);
        setupToolbar();
        setupControls();
        long setId = getSetId();
        GetSetAsyncTask task = new GetSetAsyncTask();
        task.execute(setId);
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
        mDescriptionTextView = (TextView)findViewById(R.id.description_text_view);
        mNumWordsTextView = (TextView)findViewById(R.id.number_words_text_view);
        mSizeTextView = (TextView)findViewById(R.id.size_text_view);
        mImagesSizeTextView = (TextView)findViewById(R.id.images_size_text_view);
        mRecordsSizeTextView = (TextView)findViewById(R.id.records_size_text_view);
        mRatingTextView = (TextView)findViewById(R.id.rating_text_view);
        mDownloadCountTextView = (TextView)findViewById(R.id.download_count_text_view);
        mAddedDateTextView = (TextView)findViewById(R.id.added_date_text_view);
    }

    private void fillControls(SetItem item){
        mNameTextView.setText(item.getName());
        mL1TextView.setText(ResourceUtils.getString(item.getLanguageL1(), getBaseContext()));
        mL2TextView.setText(ResourceUtils.getString(item.getLanguageL2(), getBaseContext()));
        mAuthorTextView.setText(item.getAuthor());
        mDescriptionTextView.setText(item.getDescription());
        mNumWordsTextView.setText(String.valueOf(item.getWordsCount()));
        mSizeTextView.setText(String.valueOf(item.getBasicSize()));
        mImagesSizeTextView.setText(String.valueOf(item.getImagesSize()));
        mRecordsSizeTextView.setText(String.valueOf(item.getRecordsSize()));
        mRatingTextView.setText(String.valueOf(item.getRating()));
        mDownloadCountTextView.setText(String.valueOf(item.getDownloads()));
        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        mAddedDateTextView.setText(format.format(item.getAddedDate()));
    }

    private class GetSetAsyncTask extends AsyncTask<Long, Void, SetItem>{

        @Override
        protected SetItem doInBackground(Long... params) {
            SetDetailRequest request = new SetDetailRequest(params[0]);
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

}
