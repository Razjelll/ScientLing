package com.dyszlewskiR.edu.scientling.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.CategorySelectionActivity;
import com.dyszlewskiR.edu.scientling.activity.SentencesListActivity;
import com.dyszlewskiR.edu.scientling.activity.WordEditActivity;
import com.dyszlewskiR.edu.scientling.data.models.Category;
import com.dyszlewskiR.edu.scientling.data.models.Definition;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordAdvancedEditFragment extends Fragment {

    public static final int SENTENCES_REQUEST = 404;
    public static final int CATEGORY_REQUEST = 403;

    private Spinner mPartsOfSpeechSpinner;
    private Button mCategoryButton;
    private EditText mDefinitionEditText;
    private EditText mTranslationDefinitionEditText;
    private Button mSentencesButton;
    private RatingBar mDifficultRatingbar;

    private Word mWord;

    private Category mCategory;

    public WordAdvancedEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWord = new Word();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_advanced_edit, container, false);

        mPartsOfSpeechSpinner = (Spinner) view.findViewById(R.id.part_of_speech_spinner);
        mDefinitionEditText = (EditText) view.findViewById(R.id.definition_edit_text);
        mTranslationDefinitionEditText = (EditText) view.findViewById(R.id.definition_translation_edit_text);
        mTranslationDefinitionEditText.setVisibility(View.GONE);
        mDifficultRatingbar = (RatingBar)view.findViewById(R.id.difficult_ratingbar);
        mCategoryButton = (Button) view.findViewById(R.id.category_button);
        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCategorySelectionActivity();
            }
        });
        final TextView translationLabel = (TextView) view.findViewById(R.id.definition_translation_label);
        mDefinitionEditText.addTextChangedListener(new TextWatcher() { //pokazywajnie
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("WordAdvanced", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("WordAdvanced", String.valueOf(s));
                if (mDefinitionEditText.getText().length() == 0) {
                    translationLabel.setVisibility(View.GONE);
                    mTranslationDefinitionEditText.setVisibility(View.GONE);
                } else {
                    if (mTranslationDefinitionEditText.getVisibility() != View.VISIBLE) {
                        translationLabel.setVisibility(View.VISIBLE);
                        mTranslationDefinitionEditText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("WordAdvanced", s.toString());
            }
        });
        mSentencesButton = (Button) view.findViewById(R.id.sentencesButton);
        mSentencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SentencesListFragment fragment = (SentencesListFragment) fragmentManager.findFragmentById(R.id.sentence_list_fragment);
                if (fragment == null || !fragment.isVisible()) {
                    Intent intent = new Intent(getActivity(), SentencesListActivity.class);
                    intent.putParcelableArrayListExtra("sentencesList", mWord.getSentences());
                    getActivity().startActivityForResult(intent, SENTENCES_REQUEST);
                }
            }
        });
        return view;
    }

    private void startCategorySelectionActivity() {
        Intent intent = new Intent(getActivity(), CategorySelectionActivity.class);
        VocabularySet set = ((WordEditActivity) getActivity()).getSet();
        intent.putExtra("set", set);
        getActivity().startActivityForResult(intent, CATEGORY_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SENTENCES_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Sentence> sentences = data.getParcelableArrayListExtra("result"); //TODO można zmienić na List<>
                mWord.setSentences(sentences);
                mSentencesButton.requestFocus();
            }
        }
        if (requestCode == CATEGORY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Category category = data.getParcelableExtra("result");
                mWord.setCategory(category);
                mCategoryButton.setText(category.getName());
                mCategoryButton.requestFocus();

            }
        }
    }

    public Word getWord() {
        String selectedPartOfSpeech = String.valueOf(mPartsOfSpeechSpinner.getSelectedItem());
        //TODO opuszczono kategorię i zdanie ponieważ zostały wstawione wcześniej
        int difficult = mDifficultRatingbar.getNumStars();
        String definitionContent = String.valueOf(mDefinitionEditText.getText());
        String definitionTranslaiton = String.valueOf(mTranslationDefinitionEditText.getText());
        Definition definition = new Definition();
        definition.setContent(definitionContent);
        definition.setTranslation(definitionTranslaiton);

        mWord.setDifficult((byte)difficult);
        if(definition.equals(""))
        {
            mWord.setDefinition(definition);
        }

        return mWord;
    }

    public void clear()
    {
        mPartsOfSpeechSpinner.setSelection(0);
        mDefinitionEditText.setText("");
        mTranslationDefinitionEditText.setText("");
        mDifficultRatingbar.setProgress(0);
    }


    public boolean validate()
    {
        return true;
    }

}
