package com.dyszlewskiR.edu.scientling.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.fragment.LearningFragment;

public class LearningActivity extends AppCompatActivity {

    private final String TAG = "LearningActivity";

    private ImageView mCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_learning);
        setupToolbar();
        setupControls();
        setListeners();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);
    }

    private void setupControls(){
        mCloseButton = (ImageView) findViewById(R.id.close_button);
    }

    private void setListeners(){
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitAlertDialog();
            }
        });
    }

    private void showExitAlertDialog()
    {
        new ExitAlertDialog(LearningActivity.this).show();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private class ExitAlertDialog extends AlertDialog {

        protected ExitAlertDialog(Context context) {
            super(context);

            this.setTitle(getString(R.string.close_session_learning));
            String message = getString(R.string.your_sure_leave) + " " + getString(R.string.you_lost_progress_learning);
            this.setMessage(message);
            this.setCancelable(true);
            this.setButton(BUTTON_POSITIVE, getString(R.string.yes), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            this.setButton(BUTTON_NEUTRAL, getString(R.string.back), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }


}
