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

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;

/**
 * A placeholder fragment containing a simple view.
 */
public class SentenceDetailFragment extends Fragment {

    private EditText mSentenceContent;
    private EditText mSentenceTranslation;
    private Button mSaveButton;

    public SentenceDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sentence_detail, container, false);
        mSentenceContent = (EditText) view.findViewById(R.id.sentenceEditText);
        mSentenceTranslation = (EditText) view.findViewById(R.id.translationEditText);
        mSaveButton = (Button) view.findViewById(R.id.saveButton);

        Intent intent = getActivity().getIntent();
        final Sentence sentence = intent.getParcelableExtra("sentence");

        if (sentence != null) {
            mSentenceContent.setText(sentence.getContent());
            mSentenceTranslation.setText(sentence.getTranslation());
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sentence resultSentence = new Sentence();
                resultSentence.setContent(String.valueOf(mSentenceContent.getText()));
                resultSentence.setTranslation(String.valueOf(mSentenceTranslation.getText()));
                Intent result = new Intent();
                result.putExtra("result", resultSentence);
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
            }
        });
        return view;
    }


}
