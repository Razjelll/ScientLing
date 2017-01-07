package com.dyszlewskiR.edu.scientling.activity;


import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import com.dyszlewskiR.edu.scientling.R;

public class PreferenceActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setupActionBar();

    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(),R.color.colorMain)));
            } else {
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorMain)));
            }
        }
    }

}
