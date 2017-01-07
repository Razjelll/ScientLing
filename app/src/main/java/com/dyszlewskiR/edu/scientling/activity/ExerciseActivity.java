package com.dyszlewskiR.edu.scientling.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.database.DatabaseHelper;
import com.dyszlewskiR.edu.scientling.data.models.others.RepetitionDate;
import com.dyszlewskiR.edu.scientling.fragment.ChooseExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.KnowExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.ListenAndChooseExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.ListenAndWriteExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.SummaryExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.WriteExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.dialogs.ExercisesListDialogFragment;
import com.dyszlewskiR.edu.scientling.services.exercises.ChooseExercise;
import com.dyszlewskiR.edu.scientling.services.exercises.CreateExerciseTask;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseParams;
import com.dyszlewskiR.edu.scientling.services.exercises.IExercise;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseLanguage;
import com.dyszlewskiR.edu.scientling.services.exercises.L1toL2;
import com.dyszlewskiR.edu.scientling.services.exercises.L2toL1;
import com.dyszlewskiR.edu.scientling.utils.Constants;
import com.dyszlewskiR.edu.scientling.utils.DateHelper;

import java.util.Date;
import java.util.concurrent.ExecutionException;


public class ExerciseActivity extends AppCompatActivity implements WriteExerciseFragment.OnFragmentInteractionListener,
        KnowExerciseFragment.OnFragmentInteractionListener,
        ChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndChooseExerciseFragment.OnFragmentInteractionListener,
        ListenAndWriteExerciseFragment.OnFragmentInteractionListener {

    private final int CHOOSE_EXERCISE = 1;
    private final int WRITE_EXERCISE = 2;
    private final int KNOW_EXERCISE = 3;
    private final int SUMMARY_EXERCISE = 0;

    /**
     * Kontrolka zaznaczajaca postęp danego ćwiczenia
     */
    private ProgressBar mExerciseProgress;
    private ProgressBar mCircleProgressBar;
    private TextView mCircleProgressBarText;
    /**
     * Zmienna, która określa jakie ćwiczenie jest w tej chwili aktywne
     */
    private int mCurrentFragmentNumber;
    /**
     * Manager, który służy do zarządzanai fragmentami
     */
    private FragmentManager fragmentManager;
    private ExerciseManager mExerciseManager;
    private int mNumQuestions;

    private IExerciseLanguage mExerciseLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ExerciseParams params = getExerciseParams();
        setupToolbar();
        setupControls();
        setInitialValues(params);
        setSpinnerAdapter();
        setListeners();
        //mExerciseManager = new ExerciseManager(new ExerciseParameters(1,-1,-1,-1,5),this.getApplicationContext());

        fragmentManager = getFragmentManager();


        CreateExerciseTask createExerciseTask = new CreateExerciseTask(this, params.getFirstExercise());

        createExerciseTask.execute(params);

        /*if(mExerciseManager.getNumQuestions()==0){
            Toast.makeText(getBaseContext(), "Koniec",Toast.LENGTH_LONG);
            finish();
        }*/

        //mExerciseProgress.setMax(mExerciseManager.getNumQuestions()); //zrobimy ro w asynctask

        Log.d("ExerciseActivity", "onCreate"); //DEBUG //TODO podorabiać pozostałe
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        toolbar.setTitle(""); //usunięcie tytułu aplikacji z górnego paska
        setSupportActionBar(toolbar);
    }

    private ExerciseParams getExerciseParams() {
        ExerciseParams params = new ExerciseParams();
        Intent intent = getIntent();
        long setId = intent.getLongExtra("set", Constants.DEFAULT_SET_ID);
        int numberQuestions = intent.getIntExtra("questions", 0);
        int numberAnswers = intent.getIntExtra("answers", 4);
        int repetitionMonth = intent.getIntExtra("repetitionMonth",0);
        int repetitionDay = intent.getIntExtra("repetitionDay",0);
        boolean fromLesson = intent.getBooleanExtra("fromLesson",false);
        boolean fromCategory = intent.getBooleanExtra("fromCategory",false);
        int firstExercise = intent.getIntExtra("exercise",1);

        params.setSetId(setId);

        params.setNumberQuestion(numberQuestions);
        params.setNumberAnswers(numberAnswers);
        if(repetitionMonth >0 && repetitionDay > 0) {
            params.setRepetitionMonth(repetitionMonth);
            params.setRepetitionDay(repetitionDay);
        }
        params.setAnswerFromLesson(fromLesson);
        params.setAnswerFromCategory(fromCategory);
        params.setFirstExercise(firstExercise);

        return params;
    }

    private void setupControls(){
        mExerciseProgress = (ProgressBar) findViewById(R.id.exerciseProgressBar);
        mCircleProgressBar = (ProgressBar) findViewById(R.id.circleWaitingBar);
        mCircleProgressBarText = (TextView) findViewById(R.id.circleWaitingBarText);
    }

    private void setSpinnerAdapter(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actionbar_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setListeners(){

    }

    private void setInitialValues(ExerciseParams params){
        mExerciseProgress.setMax(params.getNumberQuestion());
        mExerciseProgress.setProgress(0);
        mExerciseLanguage = new L1toL2();
    }

    public void finishActivity(){
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_change_exercise:
                hideKeyboard();
                Bundle arguments = new Bundle();
                arguments.putInt("selected", mCurrentFragmentNumber-1);
                ExercisesListDialogFragment dialog = new ExercisesListDialogFragment();
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(),"TAG");
                return true;
            case R.id.item_reverse_exercise:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void prepareExerciseProgress(int value) //nazwa do wymiany
    {
        mExerciseProgress.setMax(value);
        mExerciseProgress.setProgress(0);
    }

    public void changeFragment(int fragmentNumber)
    {
        if(fragmentNumber != mCurrentFragmentNumber){
            switch (fragmentNumber) {
                case CHOOSE_EXERCISE:
                    replaceFragment(ChooseExerciseFragment.newInstance(mExerciseManager, mExerciseLanguage));
                    break;
                case WRITE_EXERCISE:
                    replaceFragment(WriteExerciseFragment.newInstance(mExerciseManager, mExerciseLanguage));
                    break;
                case KNOW_EXERCISE:
                    replaceFragment(KnowExerciseFragment.newInstance(mExerciseManager, mExerciseLanguage));
                    break;
                case SUMMARY_EXERCISE:
                    replaceFragment(SummaryExerciseFragment.newInstance(mExerciseManager));
                    break;
            }
            mCurrentFragmentNumber = fragmentNumber;
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Metoda zamieniająca fragmenty z ćwiczeniami na aktywności
     *
     * @param fragment fragment, na jaki chcemy zmienić obecny
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.exerciseFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void hideCircleProgressBar() {
        mCircleProgressBar.setVisibility(View.GONE);
        mCircleProgressBarText.setVisibility(View.GONE);
    }

    public void showCurrentFragment() {
        changeFragment(mCurrentFragmentNumber);
    }

    public void setExerciseManager(ExerciseManager exerciseManager) {
        mExerciseManager = exerciseManager;
    }

    public void setNumQuestions(int numQuestions) {
        mNumQuestions = numQuestions;
    }

    public void updateQuestion() {
        int correctAnswers = mExerciseManager.getNumCorrectAnswers();
        if (correctAnswers == mNumQuestions) {
            changeFragment(SUMMARY_EXERCISE);
        }
        mExerciseProgress.setProgress(correctAnswers);
    }

    public void restart(int fragment) {
        mExerciseProgress.setProgress(0);
        changeFragment(fragment); //TODO ustawione domyślenie
    }

    public enum ExerciseLanguage {
        L1, L2
    }


}
