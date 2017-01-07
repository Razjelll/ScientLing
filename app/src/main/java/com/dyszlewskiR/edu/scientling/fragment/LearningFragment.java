package com.dyszlewskiR.edu.scientling.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.SummaryLearningActivity;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.utils.TranslationListConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LearningFragment extends Fragment {

    private final String TAG = "LearningFragment";

    private final int SENTENCES_FRAGMENT = 0;
    private final int IMAGE_FRAGMENT = 1;
    private final int DEFINITION_FRAGMENT = 2;
    private final int HINTS_FRAGMENT = 3;

    private List<Word> mWords;
    private LinearLayout mLayout;
    private RelativeLayout mFragmentContainer;
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
        mFragmentContainer = (RelativeLayout) view.findViewById(R.id.fragment_container);
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
            final Animation animationIn = AnimationUtils.makeInAnimation(getActivity(), false);
            Animation animationOut = AnimationUtils.makeOutAnimation(getActivity(), false);
            animationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCurrentPosition++;
                    fillComponents(mCurrentPosition);
                    mLayout.startAnimation(animationIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mLayout.startAnimation(animationOut);
        } else {
            //TODO może dodać jakiś komunikat;
            resetColorButtons();
            startSummaryActivity();
        }
    }

    private void startSummaryActivity()
    {
        Intent intent = new Intent(getActivity(), SummaryLearningActivity.class);

        ArrayList<Word> arrayList = new ArrayList<>(mWords);
        intent.putParcelableArrayListExtra("items", arrayList);
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().startActivity(intent);
        getActivity().finish();
        //ActivityCompat.finishAffinity(getActivity());
    }

    /**
     * Metoda resetujące kolor przycisków. Należy wykonać tę metodę przed przejściem do innej aktywności,
     * ponieważ w przypadku zmiany koloru przycisku na zielony ustawienie to moze być zapamiętane
     * i wszystkie przycisku korzystające z danego kształtu będą miały ten kolor.
     */
    private void resetColorButtons()
    {
        GradientDrawable buttonShape = (GradientDrawable)mSentencesButton.getBackground().getCurrent();
        buttonShape.setColor(getResources().getColor(R.color.colorMain));

    }

    private void previousWord() {
        final Animation animationIn = AnimationUtils.makeInAnimation(getActivity(), true);
        Animation animationOut = AnimationUtils.makeOutAnimation(getActivity(), true);
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCurrentPosition--;
                fillComponents(mCurrentPosition);
                mLayout.startAnimation(animationIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLayout.startAnimation(animationOut);
    }

    private void fillComponents(int position) {
        setTexts(position);

        int newFragmentNumber = getNumberExistingFragment(position);

        if (newFragmentNumber == -1) {
            mFragmentContainer.setVisibility(View.INVISIBLE);
        } else {
            if (mFragmentContainer.getVisibility() == View.INVISIBLE) {
                mFragmentContainer.setVisibility(View.VISIBLE);
            }
        }
        if (newFragmentNumber == mCurrentFragment || newFragmentNumber == -1) {
            fillFrameLayout(position);
        } else {
            replaceFragment(newFragmentNumber, position);
        }
        arrangeVisibilityButtons(position);

    }

    private void setTexts(int position) {
        mContentTextView.setText(mWords.get(position).getContent());
        String translations = TranslationListConverter.toString(mWords.get(position).getTranslations());
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
            case DEFINITION_FRAGMENT:
                fillDefinitionFragment(position);
                break;
            case HINTS_FRAGMENT:
                fillHintsFragment(position);
                break;
            case IMAGE_FRAGMENT:
                fillImageFragment(position);
                break;
        }
    }

    private void fillSentenceFragment(int position) {
        if (mFragment != null) {
            ((SentencesPagerFragment) mFragment).setList(mWords.get(position).getSentences());
        }
    }

    private void fillDefinitionFragment(int position)
    {
        if(mFragment!= null)
        {
            ((DefinitionPagerFragment)mFragment).setDefinition(mWords.get(position).getDefinition());
        }
    }

    private void fillHintsFragment(int position)
    {
        if(mFragment!=null)
        {
            ((HintsPagerFragment)mFragment).setList(mWords.get(position).getHints());
        }
    }

    private void fillImageFragment(int position)
    {
        if(mFragment != null)
        {
            ((ImagePagerFragment)mFragment).setImage(mWords.get(position).getImage().getBitmap());
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
                changeButtonsColor(fragmentNumber);
            }/* else {
                mCurrentFragment = -1;
            }*/
        }
    }

    private Fragment getFragment(int fragmentNumber, int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (fragmentNumber) {
            case SENTENCES_FRAGMENT:
                fragment = new SentencesPagerFragment();
                bundle.putParcelableArrayList("items", mWords.get(position).getSentences());
                fragment.setArguments(bundle);
                return fragment;
            case DEFINITION_FRAGMENT:
                fragment = new DefinitionPagerFragment();
                bundle.putParcelable("item", mWords.get(position).getDefinition());
                fragment.setArguments(bundle);
                return fragment;
            case HINTS_FRAGMENT:
                fragment = new HintsPagerFragment();
                bundle.putParcelableArrayList("items", mWords.get(position).getHints());
                fragment.setArguments(bundle);
                return fragment;
            case IMAGE_FRAGMENT:
                fragment = new ImagePagerFragment();
                bundle.putParcelable("item", mWords.get(position).getImage().getBitmap());
                fragment.setArguments(bundle);
                return fragment;
        }
        return null;
    }

    private void changeButtonsColor(int selectedButton)
    {
        changeButtonColor(mSentencesButton, selectedButton == SENTENCES_FRAGMENT);
        changeButtonColor(mDefinitionButton, selectedButton == DEFINITION_FRAGMENT);
        changeButtonColor(mImageButton, selectedButton == IMAGE_FRAGMENT);
        changeButtonColor(mHintsButton, selectedButton == HINTS_FRAGMENT);
    }

    private void changeButtonColor(ImageButton button, boolean selected)
    {
        GradientDrawable buttonShape = (GradientDrawable)button.getBackground().getCurrent();
        if(selected) {
            buttonShape.setColor(getResources().getColor(R.color.correctColor));
        } else {
            buttonShape.setColor(getResources().getColor(R.color.colorMain));
        }
    }
}
