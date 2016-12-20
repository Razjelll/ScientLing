package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.SentenceDetailActivity;
import com.dyszlewskiR.edu.scientling.adapters.SentencesAdapter;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SentencesListFragment extends Fragment {

    public final int ADD_REQUEST = 456;
    private final int EDIT_REQUEST = 123;
    private List<Sentence> mItems;
    private ListView mListView;

    private Button mSaveButton;

    private int mEditedPosition;

    public SentencesListFragment() {
        mEditedPosition = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getActivity().getIntent();
        mItems = data.getParcelableArrayListExtra("sentencesList");
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sentences_list, container, false);
        view.setFocusable(false);
        mListView = (ListView) view.findViewById(R.id.sentenceList);
        mSaveButton = (Button) view.findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("result", (ArrayList<Sentence>) mItems);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        SentencesAdapter adapter = new SentencesAdapter(getActivity(), R.layout.item_sentences, mItems);
        mListView.setAdapter(adapter);


        //mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setClickable(true);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("mListView ", "onItemClick");
                Intent intent = new Intent(getActivity(), SentenceDetailActivity.class);
                intent.putExtra("sentence", mItems.get(position));//TODO do stałej to
                mEditedPosition = position;
                getActivity().startActivityForResult(intent, EDIT_REQUEST);
            }
        });
    }

    public void startActivity(int resource) {

    }

    public void addSentence(Sentence sentence) {
        mItems.add(sentence);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Sentence sentence = data.getParcelableExtra("result");
                if (sentence != null) {
                    addSentence(sentence);
                }

                mListView.invalidateViews();
            }
        }
        if (requestCode == EDIT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Sentence sentence = data.getParcelableExtra("result");
                if (sentence != null) {
                    if (mEditedPosition != -1) {
                        mItems.get(mEditedPosition).setContent(sentence.getContent());
                        mItems.get(mEditedPosition).setTranslation(sentence.getTranslation());
                        mListView.invalidateViews();
                        mEditedPosition = -1;
                    }

                }
            }
        }
    }
}
