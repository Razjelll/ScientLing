package com.dyszlewskiR.edu.scientling.dialogs;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;

/**
 * Created by Razjelll on 31.03.2017.
 */

public class LessonDialog extends DialogFragment {
    private final int LAYOUT_RESOURCE = R.layout.dialog_lesson;

    private EditText mNameEditText;
    private EditText mNumberEditText;
    private Button mOkButton;
    private Callback mCallback;
    private Lesson mLesson;
    private boolean mEdit;

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void setLesson(Lesson lesson){
        mLesson = lesson;
        mEdit = true;
    }

    public interface Callback{
        void onLessonOk(Lesson lesson);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT_RESOURCE, container, false);
        setupControls(view);
        setListeners();
        getDialog().setTitle(getString(R.string.lesson));
        return view;
    }

    private void setupControls(View view){
        mNameEditText = (EditText)view.findViewById(R.id.name_edit_text);
        mNumberEditText = (EditText)view.findViewById(R.id.number_edit_text);
        mOkButton = (Button)view.findViewById(R.id.ok_button);
    }

    private void setListeners(){
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    if(validate()){
                        Lesson lesson = new Lesson();
                        lesson.setName(mNameEditText.getText().toString());
                        lesson.setNumber(Long.parseLong(mNumberEditText.getText().toString()));
                        mCallback.onLessonOk(lesson);
                        dismiss();
                    }
                }
            }
        });
    }

    private boolean validate(){
        boolean correct = true;
        if(mNameEditText.getText().toString().equals("")){
            mNameEditText.setError(getString(R.string.not_empty_field));
            correct = false;
        }
        if(mNumberEditText.getText().toString().equals("")){
            mNumberEditText.setError(getString(R.string.not_empty_field));
            correct = false;
        } else {
            long number = Long.parseLong(mNumberEditText.getText().toString());
            if(number <= 0){
                mNumberEditText.setError(getString(R.string.not_zero_field));
                correct = false;
            }
        }
        return correct;

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface){
        mCallback = null;
        getFragmentManager().beginTransaction().remove(this).commit();
    }


}
