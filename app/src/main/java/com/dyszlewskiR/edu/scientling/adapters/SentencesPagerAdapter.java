package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;

import java.util.List;

/**
 * Created by Razjelll on 21.12.2016.
 */

public class SentencesPagerAdapter extends PagerAdapter {

    private List<Sentence> mItems;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    public SentencesPagerAdapter(Context context, int resource, List<Sentence> data)
    {
        mContext = context;
        mResource = resource;
        mItems = data;

        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        //TODO zobaczyć czy uda się to zrobić inaczej
        View view = mInflater.inflate(R.layout.item_sentence_viewpager, null);
        TextView contentTextView = (TextView)view.findViewById(R.id.sentence_content_text_view);
        TextView translationTextView = (TextView)view.findViewById(R.id.sentence_translation_text_view);

        contentTextView.setText(mItems.get(position).getContent());
        translationTextView.setText(mItems.get(position).getTranslation());

        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager)container).removeView((View)object);
    }

}
