package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.dialogs.SentenceDialog;
import com.dyszlewskiR.edu.scientling.fragment.SentenceDetailFragment;
import com.dyszlewskiR.edu.scientling.fragment.SentencesListFragment;

public class SentencesListActivity extends AppCompatActivity {

    private ImageView mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences_list);
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

        //mAddButton = (ImageView) findViewById(R.id.add_button);
    }

    private void setListeners(){
       /* mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetail();
            }
        });*/
    }

    private void startDetail(){
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        SentenceDetailFragment fragment = (SentenceDetailFragment) fragmentManager.findFragmentById(R.id.sentenceDetailFragment);
        if (fragment == null || !fragment.isVisible()) {
            Intent intent = new Intent(getApplicationContext(), SentenceDetailActivity.class);
            startActivityForResult(intent, 456);

        } else {
            //TODO
        }*/

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sentences_list_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            getSentenceListFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    private SentencesListFragment getSentenceListFragment(){
        return (SentencesListFragment)getSupportFragmentManager().findFragmentById(R.id.sentence_list_fragment);
    }

    @Override
    public void onBackPressed(){
        getSentenceListFragment().onBackPressed();
    }


}
