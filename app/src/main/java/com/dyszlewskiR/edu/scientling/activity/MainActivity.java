package com.dyszlewskiR.edu.scientling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.LessonsProgressAdapter;
import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Preferences;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final int SET_CHANGING_REQUEST = 875;

    private ListView mLessonListView;
    private Button mRepetitionButton;
    private Button mLearningButton;
    private ImageButton mMoreRepetitionsButton;
    private ImageButton mMoreLearningButton;
    private ProgressBar mSetProgressBar;

    private List<Lesson> mLessons;
    private DataManager mDataManager;

    private int mRepetitionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(),"onCreate");
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupControls();
        setListeners();
    }

    @Override
    protected void onResume(){
        Log.d(getClass().getSimpleName(),"onResume");
        setInitialValues();
        setRepetitionNumber();//przenieśc do onResume
        super.onResume();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupControls() {
        mLessonListView = (ListView) findViewById(R.id.list);
        mRepetitionButton = (Button) findViewById(R.id.repetition_button);
        mLearningButton = (Button) findViewById(R.id.learning_button);
        mMoreRepetitionsButton = (ImageButton) findViewById(R.id.more_repetition_button);
        mMoreLearningButton = (ImageButton) findViewById(R.id.more_learning_button);
        mSetProgressBar = (ProgressBar) findViewById(R.id.set_progress_bar);
    }

    private void setListeners() {
        mRepetitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRepetitionCount != 0){
                    startDefaultRepetition();
                } else {
                    showNoRepetitionMessage();
                }
            }
        });
        mLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDefaultLearning();
            }
        });
        mMoreRepetitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRepetitionActivity();
            }
        });
        mLessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startLessonLearning(mLessons.get(position).getId());
            }
        });
    }

    private void showNoRepetitionMessage(){
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        Snackbar snackbar = Snackbar.make(view,getString(R.string.no_repetitions_today),Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void setInitialValues() {
        mDataManager = ((LingApplication) getApplication()).getDataManager();
        //TODO to zrobić w osobnym wątku
        VocabularySet set = mDataManager.getSetById(Settings.getCurrentSetId(getBaseContext()));
        mLessons = mDataManager.getLessonsWithProgress(set);
        getSupportActionBar().setTitle(set.getName());

        setLessonListValues();

        //ustawianie postępu dla całego zestawu
        int setProgress = calculateSetProgress();
        mSetProgressBar.setProgress(setProgress);
    }

    private void setRepetitionNumber() {
        long setId = Settings.getCurrentSetId(getBaseContext());
        int month = 4;
        int day = 4;
        int repetitionCount = mDataManager.getRepetitionsCount(setId, month, day);
        String repetitionButtonText = getString(R.string.repetitions);
        if (repetitionCount != 0) {
            repetitionButtonText += "(" + repetitionCount + ")";
        }
        mRepetitionButton.setText(repetitionButtonText);
        mRepetitionCount = repetitionCount;
    }

    private void setLessonListValues() {
        LessonsProgressAdapter adapter = new LessonsProgressAdapter(getBaseContext(), R.layout.item_lesson_progress, mLessons);
        mLessonListView.setAdapter(adapter);
    }

    private int calculateSetProgress() {
        int sum = 0;
        if (mLessons != null && !mLessons.isEmpty()) {
            for (int i = 0; i < mLessons.size(); i++) {
                sum += mLessons.get(i).getProgress();
            }
            return sum / mLessons.size();
        }
        return sum;
    }

    private void startDefaultRepetition() {
        Intent intent = new Intent(getBaseContext(), ExerciseActivity.class);
        intent.putExtra("set", Settings.getCurrentSetId(getBaseContext()));
        intent.putExtra("repetitionMonth", /*DateHelper.getCurrentMonth()*/4);
        intent.putExtra("repetitionDay",/*DateHelper.getCurrentDay()*/4);
        intent.putExtra("questions", Preferences.getNumberWordsInRepetitions(getBaseContext()));
        intent.putExtra("answers", Preferences.getNumberAnswers(getBaseContext()));
        Preferences.AnswerConnection connection = Preferences.getAnswerConnection(getBaseContext());
        if (connection == Preferences.AnswerConnection.LESSON) {
            intent.putExtra("fromLesson", true);
        } else {
            intent.putExtra("fromLesson", false);
        }
        if (connection == Preferences.AnswerConnection.CATEGORY) {
            intent.putExtra("fromCategory", true);
        } else {
            intent.putExtra("fromCategory", false);
        }
        intent.putExtra("exercise", Preferences.getDefaultExercise(getBaseContext()));
        intent.putExtra("direction", Preferences.getExerciseDirection(getBaseContext()));
        startActivity(intent);
    }

    private void startDefaultLearning() {
        Intent intent = getDefaultLearningIntent();
        startActivity(intent);
    }

    private void startLessonLearning(long lessonId) {
        Intent intent = getDefaultLearningIntent();
        intent.putExtra("lesson", lessonId);
        startActivity(intent);
    }

    private Intent getDefaultLearningIntent() {
        Intent intent = new Intent(getBaseContext(), LearningListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("set", Settings.getCurrentSetId(getBaseContext()));
        intent.putExtra("limit", Preferences.getNumberWordsInLearning(getBaseContext()));
        return intent;
    }

    private void startRepetitionActivity() {
        Intent intent = new Intent(getBaseContext(), RepetitionsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add_word) {
            startAddWordActivity();
        }
        if (id == R.id.nav_flashcard) {
            startFlashcardActivity();
        }
        if (id == R.id.nav_settings) {
            startSettingsActivity();
        }
        if (id == R.id.nav_change_set) {
            startSetChangingActivity();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startAddWordActivity() {
        Intent intent = new Intent(getBaseContext(), WordEditActivity.class);
        startActivity(intent);
    }

    private void startFlashcardActivity() {
        Intent intent = new Intent(getBaseContext(), FlashcardActivity.class);
        intent.putExtra("set", Settings.getCurrentSetId(getBaseContext()));
        intent.putExtra("type", FlashcardParams.ChoiceType.LAST_LEARNED);
        intent.putExtra("limit", Preferences.getNumberFlashcards(getBaseContext()));
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(getBaseContext(), PreferenceActivity.class);
        startActivity(intent);
    }

    private void startSetChangingActivity(){
        Intent intent = new Intent(getBaseContext(), CurrentSetSelectionActivity.class);
        startActivityForResult(intent,SET_CHANGING_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == SET_CHANGING_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Log.d(getClass().getSimpleName(), "onActivityResult");
                long newSetId = data.getLongExtra("result", Constants.DEFAULT_SET_ID);
                changeSet(newSetId);
            }
        }
    }

    private void changeSet(long newSetId){
        Settings.setCurrentSetId(newSetId, getBaseContext());
    }
}
