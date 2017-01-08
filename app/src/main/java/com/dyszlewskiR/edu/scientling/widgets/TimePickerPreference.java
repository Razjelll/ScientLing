package com.dyszlewskiR.edu.scientling.widgets;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dyszlewskiR.edu.scientling.R;

/**
 * Created by Razjelll on 08.01.2017.
 */

public class TimePickerPreference extends Preference {
    private int mHour = 0;
    private int mMinute = 0;
    private TimePicker mPicker;
    private SharedPreferences mPreferences;
    public TimePickerPreference(Context context)
    {
        super(context);
    }
    public TimePickerPreference(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public TimePickerPreference(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public static int getHour(String time){
        String[] timeParts= time.split(":");
        try{
            return Integer.valueOf(timeParts[0]);
        } catch (NumberFormatException ex){
            return 0;
        }
    }

    public static int getMinute(String time){
        String[] timeParts= time.split(":");
        try{
            return Integer.valueOf(timeParts[1]);
        } catch (NumberFormatException ex){
            return 0;
        }
    }

   @Override
    public void onBindView(View view){
       super.onBindView(view);
   }

    @Override
    public void onClick(){
        super.onClick();
        setupTime(getContext());
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                savePreference(hourOfDay, minute);
            }
        },mHour,mMinute,true).show();
    }

    private void setupTime(Context context){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String time = mPreferences.getString(getKey(), "00:00");
        String[] timeParts = time.split(":");
        mHour = Integer.valueOf(timeParts[0]);
        mMinute = Integer.valueOf(timeParts[1]);
    }

    private void savePreference(int hour, int minute){
        SharedPreferences.Editor editor = mPreferences.edit();
        String hourString = String.valueOf(hour);
        if(hourString == "0"){
            hourString = "00";
        }
        String minuteString = String.valueOf(minute);
        if(minuteString == "0"){
            minuteString = "00";
        }
        String value = hourString + ":" + minuteString;
        editor.putString(getKey(), value);
        editor.apply();
    }

}
