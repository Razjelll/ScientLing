package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.fragment.LearningFragment;
import com.dyszlewskiR.edu.scientling.fragment.LearningListFragment;

public class LearningListActivity extends AppCompatActivity {

    private final String TAG = "LearningListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_learning_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
        if (resultCode != RESULT_CANCELED) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
