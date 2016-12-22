package com.dyszlewskiR.edu.scientling.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.Word;
import com.dyszlewskiR.edu.scientling.utils.TranslationListToString;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LearningFragment extends Fragment {

    private final int SENTENCES_FRAGMENT = 0;
    private final int IMAGE_FRAGMENT = 1;
    private final int DEFINITION_FRAGMENT = 2;
    private final int HINTS_FRAGMENT = 3;

    private List<Word> mWords;
    private LinearLayout mLayout;
    private TextView mContentTextView;
    private TextView mTranslationTextView;
    private TextView mPartOfSpeechTextView;
    private TextView mCategoryTextView;
    private Button mSpeechButton;
    private FrameLayout mFragmentsFrame;
    private ImageButton mSentencesButton;
    private ImageButton mImageButton;
    private ImageButton mDefinitionButton;
    private ImageButton mHintsButton;
    private Button mPreviousButton;
    private Button mNextButon;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private int mCurrentFragment;

    private int mCurrentPosition;

    public LearningFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        mWords = intent.getParcelableArrayListExtra("items");

        mFragmentManager = getFragmentManager();
        mCurrentFragment = -1; //TODO wstawiona wartośćdomyślna
        mCurrentPosition = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        mLayout = (LinearLayout) view.findViewById(R.id.layouts_container);
        mContentTextView = (TextView) view.findViewById(R.id.word_content_text_view);
        mTranslationTextView = (TextView) view.findViewById(R.id.word_translation_text_view);
        mPartOfSpeechTextView = (TextView) view.findViewById(R.id.word_part_of_speech_text_view);
        mCategoryTextView = (TextView) view.findViewById(R.id.word_category_text_view);
        mSpeechButton = (Button) view.findViewById(R.id.speech_button);
        mFragmentsFrame = (FrameLayout) view.findViewById(R.id.fragment_frame_layout);
        mSentencesButton = (ImageButton) view.findViewById(R.id.sentences_button);
        mImageButton = (ImageButton) view.findViewById(R.id.image_button);
        mDefinitionButton = (ImageButton) view.findViewById(R.id.definition_button);
        mHintsButton = (ImageButton) view.findViewById(R.id.hints_button);
        mPreviousButton = (Button) view.findViewById(R.id.previous_button);
        mNextButon = (Button) view.findViewById(R.id.next_button);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fillComponents(mCurrentPosition);
        setListeners();

    }

    /**
     * Metoda która zwraca numer fragmentu który zostanie załadowany do FrameLayout.
     * Metoda sprawdza, czy potrzebne wartości do stworzenia fragmentu są potrzebne,
     * i zwraca numer pierwszego fragmentu, który będzie miał potrzebnyą wartość.
     *
     * @param position pozycja elementu na liście słówek
     * @return numer fragmentu który zostanie umieszczony w miejscu Framelayout
     */
    private int getNumberExistingFragment(int position) {
        if (mWords.get(position).hasSentences()) {
            return SENTENCES_FRAGMENT;
        }
        if (mWords.get(position).hasDefinition()) {
            return DEFINITION_FRAGMENT;
        }
        if (mWords.get(position).hasImage()) {
            return IMAGE_FRAGMENT;
        }
        if (mWords.get(position).hasHints()) {
            return HINTS_FRAGMENT;
        }
        return -1;
    }

    private void setListeners() {
        mSentencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment != SENTENCES_FRAGMENT) {
                    replaceFragment(SENTENCES_FRAGMENT, mCurrentPosition);
                    mCurrentFragment = SENTENCES_FRAGMENT;
                }
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment != IMAGE_FRAGMENT) {
                    replaceFragment(IMAGE_FRAGMENT, mCurrentPosition);
                    mCurrentFragment = IMAGE_FRAGMENT;
                }
            }
        });
        mDefinitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment != DEFINITION_FRAGMENT) {
                    replaceFragment(DEFINITION_FRAGMENT, mCurrentPosition);
                    mCurrentFragment = DEFINITION_FRAGMENT;
                }
            }
        });
        mHintsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment != HINTS_FRAGMENT) {
                    replaceFragment(HINTS_FRAGMENT, mCurrentPosition);
                    mCurrentFragment = HINTS_FRAGMENT;
                }
            }
        });

        mNextButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWord();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWord();
            }
        });
    }

    private void nextWord() {
        if (mCurrentPosition != mWords.size() - 1) {
            mCurrentPosition++;
            fillComponents(mCurrentPosition);
        }
    }
    private void previousWord() {
        mCurrentPosition--;
        fillComponents(mCurrentPosition);
    }

    private void fillComponents(int position) {
        setTexts(position);

        int newFragmentNumber = getNumberExistingFragment(position);
       /* if(newFragmentNumber == -1)
        {
            mFragmentsFrame.setVisibility(View.GONE);
        } else
        {
            if(mFragmentsFrame.getVisibility() == View.GONE)
            {
                mFragmentsFrame.setVisibility(View.VISIBLE);
            }
        }*/
        if (newFragmentNumber == mCurrentFragment || newFragmentNumber == -1) {
            fillFrameLayout(position);
        } else {
            replaceFragment(newFragmentNumber, position);
        }
        arrangeVisibilityButtons(position);

    }

    private void setTexts(int position) {
        mContentTextView.setText(mWords.get(position).getContent());
        String translations = TranslationListToString.toString(mWords.get(position).getTranslations());
        mTranslationTextView.setText(translations);
        if (mWords.get(position).getPartsOfSpeech() != null) {
            mPartOfSpeechTextView.setText(mWords.get(position).getPartsOfSpeech().getName());
        }
        if (mWords.get(position).getCategory() != null) {
            mCategoryTextView.setText(mWords.get(position).getCategory().getName());
        }
    }

    //TODO tymczasowa nazwa
    private void arrangeVisibilityButtons(int position) {
        //jeżeli nie ma przykładowych zdań chowamy przycisk
        if (!mWords.get(position).hasSentences()) {
            mSentencesButton.setVisibility(View.GONE);
        } else {
            mSentencesButton.setVisibility(View.VISIBLE);
        }

        if (!mWords.get(position).hasDefinition()) {
            mDefinitionButton.setVisibility(View.GONE);
        } else {
            mDefinitionButton.setVisibility(View.VISIBLE);
        }

        if (!mWords.get(position).hasImage()) {
            mImageButton.setVisibility(View.GONE);
        } else {
            mImageButton.setVisibility(View.VISIBLE);
        }

        if (!mWords.get(position).hasHints()) {
            mHintsButton.setVisibility(View.GONE);
        } else {
            mHintsButton.setVisibility(View.VISIBLE);
        }

        //ustawianie przycisków nawigacji
        //TODO można zrobić, że jeśli jest ustawione dobrze nie ustawiamy ponownie
        if (mCurrentPosition == 0) {
            mPreviousButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousButton.setVisibility(View.VISIBLE);
        }

        if (mCurrentPosition == mWords.size() - 1) {
            mNextButon.setText(getResources().getString(R.string.finish));
        } else {
            mNextButon.setText(getResources().getString(R.string.next_item));
        }
    }

    /**
     * Metoda ustawiająca dane w zależności od tego, który fragment jest akutalnie załadowany.
     */
    private void fillFrameLayout(int position) {
        switch (mCurrentFragment) {
            case SENTENCES_FRAGMENT:
                fillSentenceFragment(position);
                break;
        }
    }

    private void fillSentenceFragment(int position) {
        if (mFragment != null) {
            ((SentencesPagerFragment) mFragment).setList(mWords.get(position).getSentences());
        }
    }

    private void replaceFragment(int fragmentNumber, int position) {
        if (mCurrentFragment != fragmentNumber) {
            Fragment fragment = getFragment(fragmentNumber, position);
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame_layout, fragment);
                //TODO zobaczyć, czy da się sprawdzić wynik transakcji
                fragmentTransaction.commit();
                mCurrentFragment = fragmentNumber;
                mFragment = fragment;
            }/* else {
                mCurrentFragment = -1;
            }*/
        }
    }

    private Fragment getFragment(int fragmentNumber, int position) {
        Fragment fragment = null;
        switch (fragmentNumber) {
            case SENTENCES_FRAGMENT:
                fragment = new SentencesPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("items", mWords.get(position).getSentences());
                fragment.setArguments(bundle);
                return fragment;
            //TODO zrobić reszte fragmentów

        }
        return null;
    }
}
