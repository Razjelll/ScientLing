package com.dyszlewskiR.edu.scientling.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.Exercise;
import com.dyszlewskiR.edu.scientling.fragment.ChooseExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.KnowExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.ListenAndChooseExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.ListenAndWriteExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.WriteExerciseFragment;
import com.dyszlewskiR.edu.scientling.services.exercises.CreateExerciseTask;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseParameters;
import com.dyszlewskiR.edu.scientling.services.exercises.L1toL2;
import com.dyszlewskiR.edu.scientling.services.exercises.L2toL1;


public class ExerciseActivity extends AppCompatActivity implements WriteExerciseFragment.OnFragmentInteractionListener,
        KnowExerciseFragment.OnFragmentInteractionListener,
        ChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndWriteExerciseFragment.OnFragmentInteractionListener{

    public enum ExerciseLanguage{
        L1, L2
    }

    //TODO refaktoryzacja

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
    private ProgressBar mCircleProgressBar;
    private TextView mCircleProgressBarText;

    /**Zmienna, która określa jakie ćwiczenie jest w tej chwili aktywne*/
    private int mCurrentFragmentNumber;
    /**Manager, który służy do zarządzanai fragmentami*/
    private FragmentManager fragmentManager;

    private ExerciseManager mExerciseManager;


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


        mCircleProgressBar = (ProgressBar)findViewById(R.id.circleWaitingBar);
        mCircleProgressBarText = (TextView)findViewById(R.id.circleWaitingBarText);

        prepareExerciseSpinner();


        //mExerciseManager = new ExerciseManager(new ExerciseParameters(1,-1,-1,-1,5),this.getApplicationContext());

        fragmentManager = getFragmentManager();

        mCurrentFragmentNumber = 2; //TODO pobierać startowy numer z preferencji
        CreateExerciseTask createExerciseTask = new CreateExerciseTask(this);
        createExerciseTask.execute(new ExerciseParameters(1,-1,-1,-1,5));

        //mExerciseProgress.setMax(mExerciseManager.getNumQuestions()); //zrobimy ro w asynctask

        Log.d("ExerciseActivity", "onCreate"); //DEBUG //TODO podorabiać pozostałe
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
                if(position != mCurrentFragmentNumber)
                {
                    changeFragment(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void prepareExerciseProgress(int value) //nazwa do wymiany
    {

        mExerciseProgress.setMax(value);
        mExerciseProgress.setProgress(0);
    }

    public void changeFragment(int fragmentNumber) //TODO mozna to zmienić na enuma
    {
        switch(fragmentNumber) {
            case KNOW_EXERCISE:
                replaceFragment(KnowExerciseFragment.newInstance(mExerciseManager,new L2toL1()));
                //TODO ustawienie odpowiedznich zmiennych we fragmentach
                break;
            case KNOW_EXERCISE_INV:
                replaceFragment(KnowExerciseFragment.newInstance(mExerciseManager, new L1toL2()));
                break;
            case CHOOSE_EXERCISE:
                replaceFragment(ChooseExerciseFragment.newInstance(mExerciseManager, new L2toL1()));
                break;
            case CHOOSE_EXERCISE_INV:
                replaceFragment(ChooseExerciseFragment.newInstance(mExerciseManager, new L1toL2()));
                break;
            case LISTEN_AND_CHOOSE_EXERCISE:
                replaceFragment(ListenAndChooseExerciseFragment.newInstance(mExerciseManager,new L2toL1()));
                break;
            case LISTEN_AND_CHOOSE_EXERCISE_INV:
                replaceFragment(ListenAndChooseExerciseFragment.newInstance(mExerciseManager, new L1toL2()));
                break;
            case LISTEN_AND_WRITE_EXERCISE:
                replaceFragment(ListenAndWriteExerciseFragment.newInstance(mExerciseManager, new L2toL1()));
                break;
            case LISTEN_AND_WRITE_EXERCISE_INV:
                replaceFragment(ListenAndWriteExerciseFragment.newInstance(mExerciseManager, new L1toL2()));
                break;
            case WRITE_EXERCISE:
                replaceFragment(WriteExerciseFragment.newInstance(mExerciseManager,new L1toL2()));
                break;
        }
        mCurrentFragmentNumber = fragmentNumber;

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

    public void hideCircleProgressBar()
    {
        mCircleProgressBar.setVisibility(View.GONE);
        mCircleProgressBarText.setVisibility(View.GONE);
    }

    public void showCurrentFragment()
    {
        changeFragment(mCurrentFragmentNumber);
        mExerciseSpinner.setSelection(mCurrentFragmentNumber);
    }

    public void setExerciseManager(ExerciseManager exerciseManager)
    {
        mExerciseManager = exerciseManager;
    }

    public void refresh()
    {
        int currentQuestion = mExerciseManager.getCurrentQuestionNum();
        mExerciseProgress.setProgress(currentQuestion);
    }


}
