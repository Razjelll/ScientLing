package com.dyszlewskiR.edu.scientling.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.dialogs.ExercisesListDialogFragment;
import com.dyszlewskiR.edu.scientling.dialogs.OKFinishAlertDialog;
import com.dyszlewskiR.edu.scientling.fragment.ChooseExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.KnowExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.SummaryExerciseFragment;
import com.dyszlewskiR.edu.scientling.fragment.WriteExerciseFragment;
import com.dyszlewskiR.edu.scientling.services.exercises.CreateExerciseTask;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseManager;
import com.dyszlewskiR.edu.scientling.services.exercises.ExerciseParams;
import com.dyszlewskiR.edu.scientling.services.exercises.IExerciseDirection;
import com.dyszlewskiR.edu.scientling.services.exercises.L1toL2;
import com.dyszlewskiR.edu.scientling.services.exercises.L2toL1;
import com.dyszlewskiR.edu.scientling.utils.Constants;


public class ExerciseActivity extends AppCompatActivity {

    private final int CHOOSE_EXERCISE = 1;
    private final int WRITE_EXERCISE = 2;
    private final int KNOW_EXERCISE = 3;
    private final int SUMMARY_EXERCISE = 0;

    private final int L1_TO_L2 = 0;
    private final int L2_TO_L1 = 1;

    /**
     * Kontrolka zaznaczajaca postęp danego ćwiczenia
     */
    private ProgressBar mExerciseProgress;
    private ProgressBar mCircleProgressBar;
    private TextView mCircleProgressBarText;
    private ImageView mCloseButton;
    /**
     * Zmienna, która określa jakie ćwiczenie jest w tej chwili aktywne
     */
    private int mCurrentFragmentNumber;
    private int mLastFragmentNumber;
    /**
     * Manager, który służy do zarządzanai fragmentami
     */
    private FragmentManager fragmentManager;
    private ExerciseManager mExerciseManager;

    private int mExerciseDirection;

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
        fragmentManager = getSupportFragmentManager();
        CreateExerciseTask createExerciseTask = new CreateExerciseTask(this);
        createExerciseTask.execute(params);
        Log.d("ExerciseActivity", "onCreate");
    }

    public void onEndCreatingExercise(ExerciseManager exerciseManager, ExerciseParams params) {
        mExerciseManager = exerciseManager;
        /*if(mExerciseManager.getNumQuestions()==0){
            finishWithDialog(params);
        }*/ //zostało to obsłużone w głównej aktywności
        prepareExerciseProgress(mExerciseManager.getNumQuestions());
        hideCircleProgressBar();
        changeFragment();
    }

    public void finishWithDialog(ExerciseParams params) {
        if (params.isRepetitionDate()) {
            new OKFinishAlertDialog(ExerciseActivity.this, "Ni ma", "Ja Krzacze").show();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setTitle(""); //usunięcie tytułu aplikacji z górnego paska
        setSupportActionBar(toolbar);
    }

    private ExerciseParams getExerciseParams() {
        ExerciseParams params = new ExerciseParams();
        Intent intent = getIntent();
        long setId = intent.getLongExtra("set", Constants.DEFAULT_SET_ID);
        int numberQuestions = intent.getIntExtra("questions", 0);
        int numberAnswers = intent.getIntExtra("answers", 4);
        int repetitionDate = intent.getIntExtra("repetitionDate", 0);
        boolean fromLesson = intent.getBooleanExtra("fromLesson", false);
        boolean fromCategory = intent.getBooleanExtra("fromCategory", false);
        int firstExercise = intent.getIntExtra("exercise", 1);
        int direction = intent.getIntExtra("direction", 0);

        params.setSetId(setId);
        params.setNumberQuestion(numberQuestions);
        params.setNumberAnswers(numberAnswers);
        if (repetitionDate != 0) {
            params.setRepetitionDate(repetitionDate);
        }
        params.setAnswerFromLesson(fromLesson);
        params.setAnswerFromCategory(fromCategory);
        params.setFirstExercise(firstExercise);
        params.setExerciseDirection(direction);

        return params;
    }

    private void setupControls() {
        mExerciseProgress = (ProgressBar) findViewById(R.id.exerciseProgressBar);
        mCircleProgressBar = (ProgressBar) findViewById(R.id.circleWaitingBar);
        mCircleProgressBarText = (TextView) findViewById(R.id.circleWaitingBarText);
        mCloseButton = (ImageView) findViewById(R.id.close_button);
    }

    private void setSpinnerAdapter() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actionbar_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setListeners() {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitAlertDialog();
            }
        });
    }

    private void showExitAlertDialog() {
        new ExitAlertDialog(ExerciseActivity.this).show();
    }

    private void setInitialValues(ExerciseParams params) {
        mExerciseProgress.setMax(params.getNumberQuestion());
        mExerciseProgress.setProgress(0);
        mExerciseDirection = params.getExerciseDirection();
        mCurrentFragmentNumber = params.getFirstExercise();
    }

    @Override
    public void onBackPressed() {
        showExitAlertDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_change_exercise:
                hideKeyboard();
                Bundle arguments = new Bundle();
                arguments.putInt("selected", mCurrentFragmentNumber - 1);
                ExercisesListDialogFragment dialog = new ExercisesListDialogFragment();
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "TAG");
                return true;
            case R.id.item_reverse_exercise:
                new ChangeDirectionAlertDialog(ExerciseActivity.this).show();
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

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setExerciseDirection(int direction) {
        Log.d(getClass().getSimpleName(), "direction " + direction);
        if (mExerciseDirection != direction) {
            Log.d(getClass().getSimpleName(), "Direction zamieniono");
            mExerciseDirection = direction;
            changeFragment();
            restart();
        }
    }

    public void setExerciseFragment(int fragment) {
        if (mCurrentFragmentNumber != fragment) {
            mLastFragmentNumber = mCurrentFragmentNumber;
            mCurrentFragmentNumber = fragment;
            changeFragment();
        }
    }

    private void changeFragment() {
        IExerciseDirection direction = getExerciseDirection(mExerciseDirection);
        Fragment fragment = getExerciseFragment(mCurrentFragmentNumber, direction);
        replaceFragment(fragment);
    }

    private IExerciseDirection getExerciseDirection(int direction) {
        switch (direction) {
            case L1_TO_L2:
                return new L1toL2();
            case L2_TO_L1:
                return new L2toL1();
        }
        assert false;
        return null;
    }

    private Fragment getExerciseFragment(int fragment, IExerciseDirection direction) {
        switch (fragment) {
            case CHOOSE_EXERCISE:
                return ChooseExerciseFragment.newInstance(mExerciseManager, direction);
            case WRITE_EXERCISE:
                return WriteExerciseFragment.newInstance(mExerciseManager, direction);
            case KNOW_EXERCISE:
                return KnowExerciseFragment.newInstance(mExerciseManager, direction);
            case SUMMARY_EXERCISE:
                SummaryExerciseFragment fragment1 = SummaryExerciseFragment.newInstance(mExerciseManager);
                fragment1.setLastFragment(mLastFragmentNumber - 1);
                return fragment1; //TODO to moze wymagać poprawki
        }
        assert false;
        return null;
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

    public void hideCircleProgressBar() {
        mCircleProgressBar.setVisibility(View.GONE);
        mCircleProgressBarText.setVisibility(View.GONE);
    }

    public void updateQuestion() {
        int correctAnswers = mExerciseManager.getNumCorrectAnswers();
        if (correctAnswers == mExerciseManager.getNumQuestions()) {
            setExerciseFragment(SUMMARY_EXERCISE);
        }
        mExerciseProgress.setProgress(correctAnswers);
    }

    public void restart() {
        mExerciseProgress.setProgress(0);
        mExerciseManager.restart();
    }


    private class ExitAlertDialog extends AlertDialog {
        protected ExitAlertDialog(Context context) {
            super(context);
            this.setTitle(getString(R.string.close_exercise));
            String message = getString(R.string.your_sure_leave) + " " + getString(R.string.you_lost_progress_exercise);
            this.setMessage(message);
            this.setCancelable(true);
            this.setButton(BUTTON_POSITIVE, getString(R.string.yes), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            this.setButton(BUTTON_NEUTRAL, getString(R.string.back), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }

    private class ChangeDirectionAlertDialog extends AlertDialog {

        protected ChangeDirectionAlertDialog(Context context) {
            super(context);
            this.setTitle(getString(R.string.you_are_sure));
            this.setMessage(getString(R.string.change_direction_message));
            this.setCancelable(true);
            this.setButton(BUTTON_POSITIVE, getString(R.string.yes), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setExerciseDirection((mExerciseDirection + 1) % 2);
                }
            });
            this.setButton(BUTTON_NEGATIVE, getString(R.string.back), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }
}
