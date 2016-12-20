package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.fragment.WordAdvancedEditFragment;
import com.dyszlewskiR.edu.scientling.fragment.WordEditFragment;

public class WordEditActivity extends AppCompatActivity {

    private final int BASIC_FRAGMENT_ID = R.id.word_edit_fragment;
    private final int ADVANCED_FRAGMENT_ID = R.id.word_advanced_edit_fragment;

    private WordEditFragment mBasicFragment;
    private WordAdvancedEditFragment mAdvancedFragment;

    private Button mSaveButton;
    private ToggleButton mModeSwitch;

    private VocabularySet mSet;

    public VocabularySet getSet() {
        return mSet;
    }

    public void setSet(VocabularySet set) {
        mSet = set;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mBasicFragment = (WordEditFragment) fragmentManager.findFragmentById(BASIC_FRAGMENT_ID);
        mAdvancedFragment = (WordAdvancedEditFragment) fragmentManager.findFragmentById(ADVANCED_FRAGMENT_ID);

        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBasicFragment.validate())
                {
                    Word word = getWordsFromFragments();
                    long id = saveWord(word);
                    if (id > 0) {
                        mBasicFragment.clear();
                        if(mAdvancedFragment != null)
                        {
                            mAdvancedFragment.clear();
                        }
                    }
                }
            }
        });
        mModeSwitch = (ToggleButton) findViewById(R.id.mode_switch);
        mModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mAdvancedFragment == null) {
                        mAdvancedFragment = new WordAdvancedEditFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.word_advanced_edit_fragment, mAdvancedFragment);
                        fragmentTransaction.commit();
                    } else {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.show(mAdvancedFragment);
                        fragmentTransaction.commit();
                    }
                } else {
                    if (mAdvancedFragment != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(mAdvancedFragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }

    private Word getWordsFromFragments() {
        if (!mModeSwitch.isChecked()) {
            return mBasicFragment.getWord();
        }
        Word word = mBasicFragment.getWord();
        Word word2 = mAdvancedFragment.getWord();
        word2.setContent(word.getContent());
        word2.setTranslations(word.getTranslations());
        word2.setLessonId(word.getLessonId());
        return word2;
    }

    private long saveWord(Word word) {
        DataManager dataManager = ((LingApplication) getApplication()).getDataManager();
        long id = dataManager.saveWord(word);
        if (id <= 0) {
            //TODO wyświetlić komunikat o niepowodzeniu
        } else {
            //TODO wyświelić komunikat o powodzeniu
        }
        return id;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.word_edit_fragment);
        fragment.onActivityResult(requestCode, resultCode, data);*/
        mBasicFragment.onActivityResult(requestCode, resultCode, data);
        if (mAdvancedFragment != null && mAdvancedFragment.isVisible())// kiedy drugi fragment jest widoczny
        {
            mAdvancedFragment.onActivityResult(requestCode, resultCode, data);
        }

    }


}
