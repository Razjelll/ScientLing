package com.wingedrabbits.edu.scientling.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.wingedrabbits.edu.scientling.R;
import com.wingedrabbits.edu.scientling.fragment.WriteExerciseFragment;


public class ExerciseActivity extends AppCompatActivity implements WriteExerciseFragment.OnFragmentInteractionListener {

    private final int OPTION1 = 0; //TODO powpisywac oddpowiedznie nazwy

    private Spinner exerciseSpinner;
    private ProgressBar exerciseProgress;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle(""); //usunięcie tytułu aplikacji z górnego paska
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        exerciseProgress = (ProgressBar)findViewById(R.id.exerciseProgressBar);
        exerciseProgress.setMax(100);
        exerciseProgress.setProgress(50);

        prepareExerciseSpinner();
    }

    /**
     * Metoda przygotowująca pasek wyboru znajdujący sie na pasku ActionBar
     */
    private void prepareExerciseSpinner()
    {
        exerciseSpinner = (Spinner)findViewById(R.id.actionBarSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actionbar_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case OPTION1:
                        exerciseProgress.setProgress(50);
                    default:
                        exerciseProgress.setProgress(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //TODO uzupełnić
        return true;
    }

}
