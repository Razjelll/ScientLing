package com.dyszlewskiR.edu.scientling.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.WordsAdapter;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordManagerFragment extends Fragment {

    private DataManager mDataManager;
    private List<Word> mWords;

    private ListView mListView;
    private EditText mSearchEditText;
    private WordsAdapter mAdapter;

    public WordManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceStete)
    {
        super.onCreate(savedInstanceStete);
        mDataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_manager, container, false);
        mListView = (ListView)view.findViewById(R.id.list);
        mSearchEditText = (EditText)view.findViewById(R.id.word_search_edit_text);
        mSearchEditText.addTextChangedListener(new TextFilter());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mWords = mDataManager.getAllWords();
        mAdapter = new WordsAdapter(getActivity(), R.layout.item_word, mWords);
        mListView.setAdapter(mAdapter);
    }

    private class TextFilter implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapter.getFilter().filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
