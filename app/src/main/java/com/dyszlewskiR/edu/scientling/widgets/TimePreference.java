package com.dyszlewskiR.edu.scientling.widgets;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.AppCompatPreferenceActivity;

import java.sql.Time;

/**
 * Created by Razjelll on 28.12.2016.
 */

public class TimePreference  extends DialogPreference{

    private final int POSITIVE_BUTTON_TEXT = R.string.set;
    private final int NEGATIVE_BUTTON_TEXT = R.string.cancel;

    private int mHour;
    private int mMinute;
    private TimePicker mTimePicker;

    public TimePreference(Context context) {
        super(context);
        setupButtonsText();

    }

    public TimePreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupButtonsText();
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        setupButtonsText();
    }

    private void setupButtonsText() {
        setPositiveButtonText(getContext().getString(POSITIVE_BUTTON_TEXT));
        setNegativeButtonText(getContext().getString(NEGATIVE_BUTTON_TEXT));
    }


    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]);
    }

    public static int getMinutes(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[1]);
    }

    @Override
    public int getDialogLayoutResource()
    {
        return 0;
    }








}
