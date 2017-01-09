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
import com.dyszlewskiR.edu.scientling.data.models.params.FlashcardParams;
import com.dyszlewskiR.edu.scientling.data.models.params.LearningParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.dialogs.OKFinishAlertDialog;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.utils.Constants;

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
        LearningParams params = getLearningParams();
        try {
            mWords = task.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(mWords.size()==0){
            closeWithDialog(params);
        }
        LearningWordsAdapter adapter = new LearningWordsAdapter(getActivity(), R.layout.item_learning_word, mWords);
        mWordsListView.setAdapter(adapter);
    }

    private void closeWithDialog(LearningParams params){
        //params będą służyły do udzielania inych komunikatow w zależności od parametrów
        new OKFinishAlertDialog(getActivity(), getString(R.string.no_words), getString(R.string.not_found_words)).show();
    }

    private LearningParams getLearningParams(){
        Intent intent = getActivity().getIntent();
        long setId = intent.getLongExtra("set", Constants.DEFAULT_SET_ID);
        long lessonId = intent.getLongExtra("lesson", 0);
        long categoryId = intent.getLongExtra("category",0);
        int difficult = intent.getIntExtra("difficult",-1);
        FlashcardParams.ChoiceType type = (FlashcardParams.ChoiceType)intent.getSerializableExtra("type");
        int order = intent.getIntExtra("order",2);
        int limit = intent.getIntExtra("limit", 0);

        LearningParams params = new LearningParams();
        params.setSetId(setId);
        if(lessonId>0){
            params.setLessonId(lessonId);
        }
        if(categoryId>0){
            params.setCategoryId(categoryId);
        }
        if(difficult>0){
            params.setDifficult(difficult);
        }
        params.setOrder(order);
        params.setLimit(limit);
        return params;
    }

    private void startLearningActivity()
    {
        Intent intent = new Intent(getActivity(), LearningActivity.class);
        intent.putParcelableArrayListExtra("items", new ArrayList<>(mWords));
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
