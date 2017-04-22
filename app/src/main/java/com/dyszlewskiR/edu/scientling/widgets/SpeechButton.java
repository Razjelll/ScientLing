package com.dyszlewskiR.edu.scientling.widgets;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dyszlewskiR.edu.scientling.R;

/**
 * Created by Razjelll on 07.04.2017.
 */

public class SpeechButton extends RelativeLayout {
    private final int LAYOUT_RESOURCE = R.layout.widget_speech_button;
    private final int SPEECH_IMAGE_RESOURCE = R.drawable.ic_image;
    private final int PAUSE_IMAGE_RESOURCE = R.drawable.ic_hint;

    private ImageView mSpeechButton;
    private ProgressBar mProgressBar;
    private Context mContext;
    private boolean mLoading;


    public SpeechButton(Context context) {
        super(context);
        init(context);
    }

    public SpeechButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpeechButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        //LinearLayout view = (LinearLayout)inflater.inflate(LAYOUT_RESOURCE, null, true);
        mContext = context;
        initControls();

    }

    private void initControls() {
        /*mSpeechButton = (ImageView) view.findViewById(R.id.speech_image_button);
        mProgressBar = (ProgressBar)view.findViewById(R.id.loading_progress_bar);*/
        mSpeechButton = getSpeechButton(getContext());
        addView(mSpeechButton);
        mProgressBar = getProgressBar(getContext());
        addView(mProgressBar);
    }

    private ImageView getSpeechButton(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(getResources().getDrawable(R.drawable.round_button));
        } else {
            imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
        }
        imageView.setImageResource(SPEECH_IMAGE_RESOURCE);
        return imageView;
    }

    private ProgressBar getProgressBar(Context context) {
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar.setVisibility(GONE);
        return progressBar;
    }

    public boolean isLoadingState() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            mProgressBar.setVisibility(VISIBLE);
            mSpeechButton.setImageURI(null); //usuwamy obrazek
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSpeechButton.setImageResource(PAUSE_IMAGE_RESOURCE);
        }
        mLoading = loading;
    }

    public void setPauseImage() {
        mSpeechButton.setImageResource(PAUSE_IMAGE_RESOURCE);
    }

    public void setPlayImage() {
        mSpeechButton.setImageResource(SPEECH_IMAGE_RESOURCE);
    }


}
