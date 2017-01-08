package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.fragment.SetSelectionFragment;

public class SetSelectionActivity extends AppCompatActivity {

    public static final int SET_REQUEST = 401;

    private SetSelectionFragment mFragment;

    private ImageView mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);
        setupToolbar();
        setupControls();
        setListeners();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupControls(){
        mAddButton = (ImageView) findViewById(R.id.add_button);
    }

    private void setListeners(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetActivity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragment == null) {
            findFragment();
        }
        if (resultCode != RESULT_CANCELED) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void findFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = (SetSelectionFragment) fragmentManager.findFragmentById(R.id.set_selection_fragment);
    }

    private void startSetActivity() {
        Intent intent = new Intent(getBaseContext(), SetActivity.class);
        startActivityForResult(intent, SET_REQUEST);
    }

}
