package com.dyszlewskiR.edu.scientling.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;

/**
 * A simple {@link Fragment} subclass.
 */
public class SideFlashcardFragment extends Fragment {

    private Word mWord;
    private int mPosition;

    public Word getWord() {return mWord;}
    public void setWord(Word word) {mWord= word;}

    public int getPosition() {return mPosition;}
    public void setPosition(int position) {mPosition = position;}


    public SideFlashcardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
