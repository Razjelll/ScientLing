package com.dyszlewskiR.edu.scientling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.fragment.LessonSelectionFragment;

public class LessonSelectionActivity extends AppCompatActivity {

    public static final int LESSON_REQUEST = 402;

    private LessonSelectionFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLessonActivity();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = (LessonSelectionFragment) fragmentManager.findFragmentById(R.id.lesson_selection_fragment);
    }

    private void startLessonActivity() {
       /* Intent intent = new Intent(getBaseContext(), LessonActivity.class);
        startActivityForResult(intent, LESSON_REQUEST);*/
        mFragment.startLessonActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {

            if (mFragment != null) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
