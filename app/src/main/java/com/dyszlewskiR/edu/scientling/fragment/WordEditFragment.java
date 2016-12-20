package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LessonSelectionActivity;
import com.dyszlewskiR.edu.scientling.activity.WordEditActivity;
import com.dyszlewskiR.edu.scientling.activity.WordManagerActivity;
import com.dyszlewskiR.edu.scientling.data.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.Translation;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordEditFragment extends Fragment {

    private final int SET_REQUEST = 222;
    private final int LESSON_REQUEST = 333;

    private Button mSetButton;
    private Button mLessonButton;
    private TextView mSetText;
    private TextView mLessonText;
    private EditText mWordEditText;
    private EditText mTranslationText;

    private Button slowka;

    private Word mWord;
    private Lesson mLesson;

    public WordEditFragment() {
        mWord = new Word();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLesson = new Lesson(); //TODO ustalone na sztywno, zmienić
        mLesson.setId(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_edit, container, false);

        mWordEditText = (EditText) view.findViewById(R.id.word_edit_text);
        mTranslationText = (EditText) view.findViewById(R.id.translation_edit_text);

        mSetText = (TextView) view.findViewById(R.id.set_text_view);
        mLessonText = (TextView) view.findViewById(R.id.lesson_text_view);

        mLessonButton = (Button) view.findViewById(R.id.lesson_change_button);

        mLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLessonSelectionActivity();
            }
        });


        slowka = (Button) view.findViewById(R.id.slowka);
        slowka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WordManagerActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    private void startLessonSelectionActivity() {
        Intent intent = new Intent(getActivity(), LessonSelectionActivity.class);
        getActivity().startActivityForResult(intent, LESSON_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LESSON_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Lesson lesson = data.getParcelableExtra("result");
                mLessonText.setText(lesson.getName());
                mSetText.setText(lesson.getSet().getName());
                mLesson = lesson;
                ((WordEditActivity) getActivity()).setSet(mLesson.getSet());
            }
        }
    }

    public Word getWord() {
        Word word = new Word();
        word.setContent(String.valueOf(mWordEditText.getText()));
        String[] translations = mTranslationText.getText().toString().split(";");
        ArrayList<Translation> translationArrayList = new ArrayList<>();
        for (String s : translations) {
            Translation translation = new Translation();
            translation.setContent(s);
            translationArrayList.add(translation);
        }
        word.setTranslations(translationArrayList);
        word.setLessonId(mLesson.getId());

        return word;
    }

    /**
     * Metoda czyszcząca szystkie pola we fragmenci, wykonywana po dodaniu słówka do bazy, aby
     * można było od razu wprowadzić kolejne słówko
     */
    public void clear()
    {
        mWordEditText.setText("");
        mTranslationText.setText("");
    }

    /**
     * Metoda sprawdzająca czy wszystkie dane zostały wprowadzone poprawnie. Jeśli któreś pole zostałó
     * uzupełnione niepoprawnie.
     * Do sprawdzenia:
     * @return poprawność wypełnionych pól
     */
    public boolean validate()
    {
        boolean correct = true;
        if(mWordEditText.getText().toString().equals(""))
        {
            mWordEditText.setError("To pole nie może być puste");
            correct = false;
        }
        if(mTranslationText.getText().toString().equals(""))
        {
            mTranslationText.setError("To pole nie może być puste");
            correct = false;
        }
        return correct;

    }


}
