package com.dyszlewskiR.edu.scientling.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.ProgressDetailsActivity;
import com.dyszlewskiR.edu.scientling.adapters.ProgressAdapter;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.SetProgress;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;

import org.w3c.dom.Text;

import java.util.List;


public class ProgressFragment extends Fragment {

    private ListView mListView;
    private ProgressAdapter mAdapter;

    public ProgressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        mListView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        DataManager dataManager = LingApplication.getInstance().getDataManager();
        List<SetProgress> sets = dataManager.getSetsProgress();
        mAdapter = new ProgressAdapter(getContext(), R.layout.item_progress, sets);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ProgressDetailsActivity.class);
                intent.putExtra("id", mAdapter.getItemId(position));
                startActivity(intent);
            }
        });
    }
}
