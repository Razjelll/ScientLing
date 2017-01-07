package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

/**
 * A placeholder fragment containing a simple view.
 */
public class LessonFragment extends Fragment {

    private EditText mNumberTextView;
    private EditText mNameTextView;
    private Button mSaveButton;

    private VocabularySet mSet;

    private DataManager mDataManager;

    public LessonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mSet = intent.getParcelableExtra("set");

        mDataManager = ((LingApplication) getActivity().getApplication()).getDataManager(); //TODO sprawdzić czy nie zrobić tego w innym miejscu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        mNumberTextView = (EditText) view.findViewById(R.id.lesson_number_edit_text);
        mNameTextView = (EditText) view.findViewById(R.id.lesson_name_edit_text);
        mNumberTextView.addTextChangedListener(new CheckNumber());
        mSaveButton = (Button) view.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lesson lesson = saveAndReturnLesson();
                setResultAndFinish(lesson);
            }
        });
        return view;
    }

    private Lesson saveAndReturnLesson() {
        Lesson lesson = new Lesson();
        lesson.setName(String.valueOf(mNameTextView.getText()));
        lesson.setNumber(Long.parseLong(String.valueOf(mNumberTextView.getText())));
        lesson.setSet(mSet);
        long id = mDataManager.saveLesson(lesson);
        if (id < 0) {
            //TODO wyświetlenie komunikatu
        } else {
            lesson.setId(id);
        }
        return lesson;
    }

    private void setResultAndFinish(Lesson lesson) {
        Intent intent = new Intent();

        intent.putExtra("result", lesson);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    class CheckNumber implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (Integer.parseInt(s.toString()) < 0) {
                    s.replace(0, s.length(), "0");
                }
            } catch (NumberFormatException ex) {
            }
        }
    }
}
