package com.dyszlewskiR.edu.scientling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.LessonsProgressAdapter;
import com.dyszlewskiR.edu.scientling.data.models.others.RepetitionDate;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.preferences.Preferences;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.utils.DateHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mLessonListView;
    private Button mRepetitionButton;
    private Button mLearningButton;
    private Button mMoreRepetitionsButton;
    private ProgressBar mSetProgressBar;

    private List<Lesson> mLessons;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prepareComponents();
        mDataManager = ((LingApplication)getApplication()).getDataManager();
        //TODO to zrobić w osobnym wątku
        VocabularySet set = mDataManager.getSetById(1);
        mLessons = mDataManager.getLessonsWithProgress(set);

        LessonsProgressAdapter adapter = new LessonsProgressAdapter(getBaseContext(), R.layout.item_lesson_progress, mLessons);
        mLessonListView.setAdapter(adapter);

        //ustawianie postępu dla całego zestawu
        int setProgress = calculateSetProgress();
        mSetProgressBar.setProgress(setProgress);

    }

    private void prepareComponents()
    {
        mLessonListView = (ListView) findViewById(R.id.list);
        mRepetitionButton = (Button) findViewById(R.id.repetition_button);
        mLearningButton = (Button) findViewById(R.id.learning_button);
        mMoreRepetitionsButton = (Button)findViewById(R.id.more_repetition_button);
        mSetProgressBar = (ProgressBar)findViewById(R.id.set_progress_bar);

        mRepetitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExerciseActivity();
            }
        });
        mLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLearningActivity();
            }
        });
        mMoreRepetitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRepetitionActivity();
            }
        });

    }

    private int calculateSetProgress()
    {
        int sum = 0;
        for(int i=0; i<mLessons.size(); i++)
        {
            sum+= mLessons.get(i).getProgress();
        }
        return sum/mLessons.size();
    }

    private void startExerciseActivity()
    {
        Intent intent = new Intent(getBaseContext(), ExerciseActivity.class);
        intent.putExtra("set", Settings.getCurrentSetId(getBaseContext()));
        intent.putExtra("repetitionMonth", /*DateHelper.getCurrentMonth()*/4);
        intent.putExtra("repetitionDay",/*DateHelper.getCurrentDay()*/4);
        intent.putExtra("questions", Preferences.getNumberWordsInRepetitions(getBaseContext()));
        intent.putExtra("answers",Preferences.getNumberAnswer(getBaseContext()));
        Preferences.AnswerConnection connection = Preferences.getAnswerConnection(getBaseContext());
        if(connection == Preferences.AnswerConnection.LESSON) {
            intent.putExtra("fromLesson", true);
        } else {
            intent.putExtra("fromLesson", false);
        }
        if(connection == Preferences.AnswerConnection.CATEGORY){
            intent.putExtra("fromCategory", true);
        } else {
            intent.putExtra("fromCategory", false);
        }
        intent.putExtra("exercise", Preferences.getDefaultExercise(getBaseContext()));
        startActivity(intent);
    }

    private void startLearningActivity()
    {
        Intent intent = new Intent(getBaseContext(), LearningListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startRepetitionActivity(){
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_add_word)
        {
            startAddWordActivity();
        }
        if(id == R.id.nav_flashcard)
        {
            startFlashcardActivity();
        }
        if(id ==R.id.nav_settings)
        {
            startSettingsActivity();
        }

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startAddWordActivity()
    {
        Intent intent = new Intent(getBaseContext(), WordEditActivity.class);
        startActivity(intent);
    }

    private void startFlashcardActivity()
    {
        Intent intent = new Intent(getBaseContext(), FlashcardActivity.class);
        startActivity(intent);
    }

    private void startSettingsActivity()
    {
        Intent intent = new Intent(getBaseContext(), PreferenceActivity.class);
        startActivity(intent);
    }
}
