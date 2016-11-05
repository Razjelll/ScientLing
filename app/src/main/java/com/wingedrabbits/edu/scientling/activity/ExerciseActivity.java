package com.wingedrabbits.edu.scientling.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

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
import com.wingedrabbits.edu.scientling.fragment.ChooseExerciseFragment;
import com.wingedrabbits.edu.scientling.fragment.KnowExerciseFragment;
import com.wingedrabbits.edu.scientling.fragment.ListenAndChooseExerciseFragment;
import com.wingedrabbits.edu.scientling.fragment.ListenAndWriteExerciseFragment;
import com.wingedrabbits.edu.scientling.fragment.WriteExerciseFragment;


public class ExerciseActivity extends AppCompatActivity implements WriteExerciseFragment.OnFragmentInteractionListener,
        KnowExerciseFragment.OnFragmentInteractionListener,
        ChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndWriteExerciseFragment.OnFragmentInteractionListener{


    private final int KNOW_EXERCISE = 0;
    private final int KNOW_EXERCISE_INV = 1;
    private final int CHOOSE_EXERCISE = 2;
    private final int CHOOSE_EXERCISE_INV = 3;
    private final int LISTEN_AND_CHOOSE_EXERCISE = 4;
    private final int LISTEN_AND_CHOOSE_EXERCISE_INV = 5;
    private final int LISTEN_AND_WRITE_EXERCISE = 6;
    private final int LISTEN_AND_WRITE_EXERCISE_INV = 7;
    private final int WRITE_EXERCISE = 8;

    /**Kontrolka wyboru rodzaju ćwiczenia znajdująca się na górnym pasku narzędzi*/
    private Spinner mExerciseSpinner;
    /**Kontrolka zaznaczajaca postęp danego ćwiczenia*/
    private ProgressBar mExerciseProgress;
    private Fragment mFragment;

    /**Zmienna, która określa jakie ćwiczenie jest w tej chwili aktywne*/
    private int mCurrentFragment;
    /**Manager, który służy do zarządzanai fragmentami*/
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle(""); //usunięcie tytułu aplikacji z górnego paska
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mExerciseProgress = (ProgressBar)findViewById(R.id.exerciseProgressBar);
        mExerciseProgress.setMax(100);
        mExerciseProgress.setProgress(50);

        prepareExerciseSpinner();

        fragmentManager = getFragmentManager();
        replaceFragment(new ChooseExerciseFragment());

    }

    /**
     * Metoda przygotowująca pasek wyboru znajdujący sie na pasku ActionBar
     */
    private void prepareExerciseSpinner()
    {
        mExerciseSpinner = (Spinner)findViewById(R.id.actionBarSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actionbar_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseSpinner.setAdapter(adapter);

        mExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != mCurrentFragment)
                {
                    switch(position)
                    {
                        case KNOW_EXERCISE:
                            replaceFragment(new KnowExerciseFragment());
                            //TODO ustawienie odpowiedznich zmiennych we fragmentach
                            break;
                        case KNOW_EXERCISE_INV:
                            replaceFragment(new KnowExerciseFragment());
                            break;
                        case CHOOSE_EXERCISE:
                            replaceFragment(new ChooseExerciseFragment());
                            break;
                        case CHOOSE_EXERCISE_INV:
                            replaceFragment(new ChooseExerciseFragment());
                            break;
                        case LISTEN_AND_CHOOSE_EXERCISE:
                            replaceFragment(new ListenAndChooseExerciseFragment());
                            break;
                        case LISTEN_AND_CHOOSE_EXERCISE_INV:
                            replaceFragment(new ListenAndChooseExerciseFragment());
                            break;
                        case LISTEN_AND_WRITE_EXERCISE:
                            replaceFragment(new ListenAndWriteExerciseFragment());
                            break;
                        case LISTEN_AND_WRITE_EXERCISE_INV:
                            replaceFragment(new ListenAndWriteExerciseFragment());
                            break;
                        case WRITE_EXERCISE:
                            replaceFragment(new WriteExerciseFragment());
                            break;
                    }
                    mCurrentFragment = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Metoda zamieniająca fragmenty z ćwiczeniami na aktywności
     * @param fragment fragment, na jaki chcemy zmienić obecny
     */
    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.exerciseFragment, fragment);
        fragmentTransaction.commit();
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
