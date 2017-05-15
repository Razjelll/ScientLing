package com.dyszlewskiR.edu.scientling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.fragment.SetsManagerFragment;
import com.dyszlewskiR.edu.scientling.fragment.SetsServerFragment;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.widgets.NonSwipeableViewPager;

public class SetsManagerActivity extends AppCompatActivity {

    private final int TAB_ONE = R.string.sets_tab;
    private final int TAB_TWO = R.string.server_tab;



    private TabLayout mTabLayout;
    //private NonSwipeableViewPager mViewPager;
    private ViewGroup mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_manager);
        setupToolbar();
        setupControls();
        setPagerAdapter();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
    }

    private void setupControls(){
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(TAB_ONE)));
        //jeżeli użytkownik jest zalogowany zostanie wyświetlohna zakładka Wysłane
        //jeżeli użytkonik nie jest zalogowany ukrywany jest cały TabLayout, zeby nie wyświetlać niepotrzebnie jednej zakładki
        if(LogPref.getLogin(getBaseContext())!= null){
            mTabLayout.addTab(mTabLayout.newTab().setText(getString(TAB_TWO)));
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            mTabLayout.setVisibility(View.INVISIBLE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SetsManagerFragment()).commit();
        /*mViewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(1);*/
    }

    private void setPagerAdapter(){
        /*final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));*/
        //TODO metoda przeterminowana
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //mViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SetsManagerFragment()).commit(); break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SetsServerFragment()).commit(); break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final int SETS_TAB = 0;
        private final int SERVER_TAB = 1;
        private int mNumOfTabs;

        public PagerAdapter(FragmentManager fragmentManager, int numOfTabs){
            super(fragmentManager);
            mNumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case SETS_TAB:
                    SetsManagerFragment fragment1 = new SetsManagerFragment();
                    return fragment1;
                case SERVER_TAB:
                    SetsServerFragment fragment2 = new SetsServerFragment();
                    return fragment2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
