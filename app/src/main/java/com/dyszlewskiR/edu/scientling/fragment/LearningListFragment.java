package com.dyszlewskiR.edu.scientling.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.LearningWordsAdapter;
import com.dyszlewskiR.edu.scientling.asyncTasks.LoadLearningAsyncTask;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.services.DataManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LearningListFragment extends Fragment {

    private List<Word> mWords;
    private ListView mWordsListView;
    private DataManager mDataManager;

    public LearningListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_list, container, false);
        mWordsListView = (ListView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        LoadLearningAsyncTask task = new LoadLearningAsyncTask(mDataManager);
        try {
            mWords = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        LearningWordsAdapter adapter = new LearningWordsAdapter(getActivity(), R.layout.item_learning_word, mWords);
        mWordsListView.setAdapter(adapter);
    }
}
