package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.fragment.WordAdvancedEditFragment;
import com.dyszlewskiR.edu.scientling.fragment.WordEditFragment;
import com.dyszlewskiR.edu.scientling.utils.Constants;
import com.dyszlewskiR.edu.scientling.utils.resources.Colors;

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
        setDefaultSet();
        setContentView(R.layout.activity_word_edit);
        setupToolbar();

        setupFragments();
        setupControls();
        addListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFragments() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mBasicFragment = (WordEditFragment) fragmentManager.findFragmentById(BASIC_FRAGMENT_ID);
        mAdvancedFragment = (WordAdvancedEditFragment) fragmentManager.findFragmentById(ADVANCED_FRAGMENT_ID);
    }

    private void setupControls() {
        mSaveButton = (Button) findViewById(R.id.save_button);
        mModeSwitch = (ToggleButton) findViewById(R.id.more_toggle_button);
    }

    private void addListeners() {
        mSaveButton.setOnClickListener(new SaveOnClickListener());
        mModeSwitch.setOnCheckedChangeListener(new MoreOnCheckedChangeListener(getSupportFragmentManager()));
    }

    private void setDefaultSet() {
        DataManager dataManager = ((LingApplication) getApplication()).getDataManager();
        long defaultSetId = Constants.DEFAULT_SET_ID;
        mSet = dataManager.getSetById(defaultSetId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metoda tworząca model słówka z obu fragmentów. Jeżeli został wypełniony tylko podstawowy
     * fragment, wtedy model będzie pobierany z podstawowego fragmentu. Jeżeli został wypełniony również
     * dodatkowy fragment, zostaną pobrane modele z obu fragmentów i do modelu z dodatkowego
     * fragmentu zostaną dodane wartości z modelu z podstawowego fragmentu, ponieważ wymaga to
     * znaczenie mniej działań niż działanie w drugą stronę.
     *
     * @return
     */
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

    /**
     * Metoda zapisująca słówko do bazy danych
     */
    private long saveWord(Word word) {
        DataManager dataManager = ((LingApplication) getApplication()).getDataManager();
        long id = dataManager.saveWord(word);
        showSaveResult(id > 0);
        return id;
    }

    /**
     * Metoda wyświetlająca komunikaty o powodzenu i niepowodzeniu zapisu
     */
    private void showSaveResult(boolean success) {
        if (success) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.save_word_success), Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            TextView snackbarText = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            snackbarText.setTextColor(Colors.getColor(R.color.correctColor, getBaseContext()));
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.save_word_failed), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView snackbarText = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            snackbarText.setTextColor(Colors.getColor(R.color.incorrectColor, getBaseContext()));
            snackbar.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBasicFragment.onActivityResult(requestCode, resultCode, data);
        if (mAdvancedFragment != null && mAdvancedFragment.isVisible())// kiedy drugi fragment jest widoczny
        {
            mAdvancedFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class SaveOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mBasicFragment.validate()) {
                Word word = getWordsFromFragments();
                long id = saveWord(word);
                if (id > 0) {
                    mBasicFragment.clear();
                    if (mAdvancedFragment != null) {
                        mAdvancedFragment.clear();
                    }
                }
            }
        }
    }

    private class MoreOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private FragmentManager mFragmentManager;

        public MoreOnCheckedChangeListener(FragmentManager fragmentManager) {
            super();
            mFragmentManager = fragmentManager;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (mAdvancedFragment == null) {
                    mAdvancedFragment = new WordAdvancedEditFragment();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.word_advanced_edit_fragment, mAdvancedFragment);
                    fragmentTransaction.commit();
                } else {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.show(mAdvancedFragment);
                    fragmentTransaction.commit();
                }
            } else {
                if (mAdvancedFragment != null) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.hide(mAdvancedFragment);
                    fragmentTransaction.commit();
                }
            }
        }
    }


}
