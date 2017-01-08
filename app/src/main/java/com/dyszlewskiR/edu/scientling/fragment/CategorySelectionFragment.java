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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.CategoriesAdapter;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CategorySelectionFragment extends Fragment {

    private EditText mSearchEditText;
    private ListView mListView;

    private List<Category> mItems;
    private CategoriesAdapter mAdapter;
    private long mLanguageL1Id;

    public CategorySelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        VocabularySet set = intent.getParcelableExtra("set");
        mLanguageL1Id = set.getLanguageL1().getId();

        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        mItems = dataManager.getCategoriesByLanguage(mLanguageL1Id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_selection, container, false);
        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mSearchEditText.addTextChangedListener(new SearchWatcher());
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResultAndFinish(position);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAdapter = new CategoriesAdapter(getActivity(), R.layout.item_category, mItems);
        mListView.setAdapter(mAdapter);
    }

    /**
     * Meotoda ustawiająca wynik aktywności i zwracająca go do aktywności przez którą została wywołana.
     * Nastepnie obecna aktywność jest zamykana
     * @param position
     */
    private void setResultAndFinish(int position) {
        Intent intent = new Intent();
        intent.putExtra("result", mItems.get(position));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //wyowłujemy metode która filtruje elementy na liście na podstawie zazwartości
            //okienka EditText
            String string = s.toString();
            mAdapter.getFilter().filter(string.toLowerCase());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}