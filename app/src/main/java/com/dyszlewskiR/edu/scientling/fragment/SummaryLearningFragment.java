package com.dyszlewskiR.edu.scientling.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.SummaryRepetitionAdapter;
import com.dyszlewskiR.edu.scientling.data.models.Word;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SummaryLearningFragment extends Fragment {

    private List<Word> mWords;
    private ListView mListView;
    private Button mSaveButton;

    public SummaryLearningFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        mWords = intent.getParcelableArrayListExtra("items");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_learning, container, false);
        mListView = (ListView) view.findViewById(R.id.list);
        mSaveButton = (Button)view.findViewById(R.id.save_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SummaryRepetitionAdapter adapter = new SummaryRepetitionAdapter(getActivity(), R.layout.item_summary_repetition, mWords);
        mListView.setAdapter(adapter);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO odpalenie usługi zapisujące zaznaczone słówka
                getActivity().finish();
            }
        });
    }
}
