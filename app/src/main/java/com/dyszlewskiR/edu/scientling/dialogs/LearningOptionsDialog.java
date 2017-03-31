package com.dyszlewskiR.edu.scientling.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LearningActivity;
import com.dyszlewskiR.edu.scientling.activity.LearningListActivity;
import com.dyszlewskiR.edu.scientling.activity.PreferenceActivity;
import com.dyszlewskiR.edu.scientling.adapters.CategoriesAdapter;
import com.dyszlewskiR.edu.scientling.adapters.LessonsAdapter;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.preferences.Preferences;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.utils.Constants;
import com.dyszlewskiR.edu.scientling.widgets.NumberPreference;

import java.util.List;

/**
 * Created by Razjelll on 15.01.2017.
 */

public class LearningOptionsDialog extends Dialog {

    private long mSetId;
    private List<Lesson> mLessons;
    private List<Category> mCategories;

    private Spinner mLessonSpinner;
    private Spinner mCategorySpinner;
    private Spinner mDifficultSpinner;
    private NumberPicker mWordsNumberPicker;
    private Button mStartButton;

    private LessonsAdapter mLessonsAdapter;
    private CategoriesAdapter mCategoriesAdapter;

    public LearningOptionsDialog(Context context,long setId, List<Lesson> lessons, List<Category> categories) {
        super(context);
        mSetId = setId;
        mLessons = lessons;
        mCategories = categories;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_learning_option);
        setupControls();
        setListeners();
        setAdapters();
        setWidth();
    }

    private void setWidth(){
        ViewGroup.LayoutParams params= getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

    }

    private void setupControls(){
        mLessonSpinner = (Spinner)findViewById(R.id.lesson_spinner);
        mCategorySpinner = (Spinner)findViewById(R.id.category_spinner);
        mDifficultSpinner = (Spinner) findViewById(R.id.difficult_spinner);
        mWordsNumberPicker = (NumberPicker)findViewById(R.id.words_number_picker);
        mStartButton = (Button)findViewById(R.id.start_button);

        mWordsNumberPicker.setMinValue(Constants.MIN_WORD_LEARNING);
        mWordsNumberPicker.setMaxValue(Constants.MAX_WORD_LEARNING);
    }

    private void setListeners(){
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LearningListActivity.class);
                intent.putExtra("set", mSetId);
                long lessonId = mCategoriesAdapter.getItemId(mLessonSpinner.getSelectedItemPosition());
                if(lessonId > 0){
                    intent.putExtra("lesson",lessonId);
                }
                long categoryId = mCategoriesAdapter.getItemId(mCategorySpinner.getSelectedItemPosition());
                if(categoryId > 0){
                    intent.putExtra("category", categoryId);
                }
                if(mDifficultSpinner.getSelectedItemPosition() != 0){
                    intent.putExtra("difficult", mDifficultSpinner.getSelectedItemPosition());
                }
                intent.putExtra("order", Preferences.getOrderLearning(getContext()));
                intent.putExtra("limit", mWordsNumberPicker.getValue());
                getContext().startActivity(intent);
            }
        });
    }

    private void setAdapters(){
        mLessonsAdapter = new LessonsAdapter(getContext(), R.layout.item_lesson_spinner, mLessons, true);
        mLessonSpinner.setAdapter(mLessonsAdapter);

        mCategoriesAdapter = new CategoriesAdapter(getContext(),R.layout.item_category_spinner, mCategories, true);
        mCategorySpinner.setAdapter(mCategoriesAdapter);

        String[] difficultLevels = {getContext().getString(R.string.lack), "1", "2","3","4","5"};
        ArrayAdapter<String> difficultAdapter = new ArrayAdapter<>(getContext(),R.layout.item_difficult_spinner,difficultLevels);
        mDifficultSpinner.setAdapter(difficultAdapter);
    }
}
