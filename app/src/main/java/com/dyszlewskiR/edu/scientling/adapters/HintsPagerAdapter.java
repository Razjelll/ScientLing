package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.Hint;

import java.util.List;
import java.util.Objects;

/**
 * Created by Razjelll on 22.12.2016.
 */

public class HintsPagerAdapter extends PagerAdapter{
    private List<Hint> mItems;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    public HintsPagerAdapter(Context context, int resource, List<Hint> data)
    {
        mContext = context;
        mResource = resource;
        mItems = data;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = mInflater.inflate(mResource, null);
        TextView contentTextView = (TextView) view.findViewById(R.id.hint_content_text_view);
        contentTextView.setText(mItems.get(position).getContent());

        ((ViewPager)container).addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager)container).removeView((View) object);
    }




    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
