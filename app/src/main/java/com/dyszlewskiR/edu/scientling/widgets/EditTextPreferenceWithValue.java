package com.dyszlewskiR.edu.scientling.widgets;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;

/**
 * Created by Razjelll on 28.12.2016.
 */

public class EditTextPreferenceWithValue extends EditTextPreference {

    private final int LAYOUT = R.layout.edit_text_preference_with_value;

    private TextView mTextView;


    public EditTextPreferenceWithValue(Context context) {
        super(context);
        setLayoutResource(LAYOUT);
    }

    public EditTextPreferenceWithValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(LAYOUT);
    }

    public EditTextPreferenceWithValue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(LAYOUT);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        mTextView = (TextView) view.findViewById(R.id.preference_value);
        if (mTextView != null) {
            mTextView.setText(getText());
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (mTextView != null) {
            mTextView.setText(getText());
        }
    }
}
