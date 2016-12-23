package com.dyszlewskiR.edu.scientling.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.FlashcardAdapter;
import com.dyszlewskiR.edu.scientling.asyncTasks.LoadLearningAsyncTask;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.services.DataManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class FlashcardFragment extends Fragment {

    private final String TAG = "FlashcardFragment";

    private List<Word> mWords;
    private ViewPager mViewPager;
    private DataManager mDataManager;
    public FlashcardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcard, container, false);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LoadLearningAsyncTask task = new LoadLearningAsyncTask(mDataManager);
        try {
            mWords = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } // TODO zrobić komunikaty
        final FlashcardAdapter adapter = new FlashcardAdapter(getActivity(), R.layout.item_flashcard, mWords);
        mViewPager.setAdapter(adapter);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            private boolean moved;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    moved = false;
                }*/
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    moved= true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    moved = false;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(!moved){
                        v.performClick();
                    }
                }
                return false;
            }
        });

        /*mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mViewPager onClick");
                adapter.changeSide(mViewPager.getCurrentItem());
                adapter.notifyDataSetChanged();
                mViewPager.invalidate();
            }
        });*/
    }
}
