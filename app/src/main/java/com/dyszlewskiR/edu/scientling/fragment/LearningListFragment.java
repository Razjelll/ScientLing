package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LearningActivity;
import com.dyszlewskiR.edu.scientling.adapters.LearningWordsAdapter;
import com.dyszlewskiR.edu.scientling.asyncTasks.LoadLearningAsyncTask;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.data.models.params.WordsParams;
import com.dyszlewskiR.edu.scientling.services.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LearningListFragment extends Fragment {

    private final int LEARNING_REQUEST = 400;

    private List<Word> mWords;
    private ListView mWordsListView;
    private DataManager mDataManager;

    private Button mStartButton;

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
        mStartButton = (Button) view.findViewById(R.id.start_button);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLearningActivity();
            }
        });

        LoadLearningAsyncTask task = new LoadLearningAsyncTask(mDataManager);
        LearningParams params = new LearningParams();
        long setId = ((LingApplication)getActivity().getApplication()).getCurrentSetId();
        params.setSetId(setId);
        params.setLimit(5);
        try {
            mWords = task.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        LearningWordsAdapter adapter = new LearningWordsAdapter(getActivity(), R.layout.item_learning_word, mWords);
        mWordsListView.setAdapter(adapter);
    }

    private void startLearningActivity()
    {
        Intent intent = new Intent(getActivity(), LearningActivity.class);
        intent.putParcelableArrayListExtra("items", new ArrayList<Word>(mWords));
        getActivity().startActivityForResult(intent, LEARNING_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LEARNING_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

}
