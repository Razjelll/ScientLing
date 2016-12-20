package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LessonActivity;
import com.dyszlewskiR.edu.scientling.activity.SetSelectionActivity;
import com.dyszlewskiR.edu.scientling.adapters.LessonsAdapter;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.VocabularySet;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LessonSelectionFragment extends Fragment {

    private final int SET_REQUEST = 401;
    private final int LESSON_REQUEST = 402;

    private ListView mListView;
    private DataManager mDataManager;
    /**
     * List lekcji. List będize zawierać lekcje z wybranego zbioru pobrane z bazy dancyh
     */
    private List<Lesson> mLessons;
    /**
     * Przycisk służący do uruchomienia aktywności wyboru zbioru z którego mają być wyświetlane lekcje
     */
    private Button mSetButton;

    private LessonsAdapter mAdapter;
    private VocabularySet mSet;

    public LessonSelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        long currentSetId = ((LingApplication) getActivity().getApplication()).getCurrentSetId();
        mSet = mDataManager.getSetById(currentSetId); //TODO zobaczyć czy nie lepiej będzie operować na samych id
        loadData(mSet);
    }

    private void loadData(VocabularySet set) {
        if (mLessons != null) {
            mLessons.clear();
            for (Lesson l : mDataManager.getLessons(set)) {
                mLessons.add(l);
            }
        } else {
            mLessons = mDataManager.getLessons(set);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_selection, container, false);
        mSetButton = (Button) view.findViewById(R.id.set_button);
        mSetButton.setText(mSet.getName());
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetSelectionActivity();
            }
        });
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResultAndFinish(position);
            }
        });
        return view;
    }


    private void startSetSelectionActivity() {
        Intent intent = new Intent(getActivity(), SetSelectionActivity.class);
        getActivity().startActivityForResult(intent, SET_REQUEST);
    }

    private void setResultAndFinish(int itemPosition) {
        Lesson lesson = mLessons.get(itemPosition);
        lesson.setSet(mSet);
        Intent result = new Intent();
        result.putExtra("result", lesson);
        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new LessonsAdapter(getActivity(), R.layout.item_lesson, mLessons);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LESSON_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Lesson lesson = data.getParcelableExtra("result");
                mLessons.add(0, lesson);
                mListView.invalidateViews();
            }
        }
        if (requestCode == SET_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                VocabularySet set = data.getParcelableExtra("result");
                mSet = set;
                loadData(mSet);
                mSetButton.setText(mSet.getName());
                mAdapter.setData(mLessons);
                mAdapter.notifyDataSetChanged();
                //mListView.invalidateViews();

            }
        }
    }

    /**
     * Metoda uruchamiająca akrywność do edycji lub dodawania lekcji. Do aktywności zostaje przekazany
     * obiekt VocabularySet, aby zapisać go razem z lekcją w bazie danych
     */
    public void startLessonActivity() {
        Intent intent = new Intent(getActivity(), LessonActivity.class);
        intent.putExtra("set", mSet);
        getActivity().startActivityForResult(intent, LESSON_REQUEST);
    }

}
