package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;

import java.util.List;

/**
 * Created by Razjelll on 19.12.2016.
 */

public class LessonsProgressAdapter extends ArrayAdapter {

    private List<Lesson> mItems;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    public LessonsProgressAdapter(Context context, int resource, List<Lesson> data) {
        super(context, resource);

        mItems = data;
        mContext = context;
        mResource = resource;

        mInflater = LayoutInflater.from(mContext);
    }



    @Override
    public int getCount()
    {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {return mItems.get(position);}

    @Override
    public long getItemId(int position){return mItems.get(position).getId();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null)
        {
            rowView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.lessonNumberTextView.setText(String.valueOf(mItems.get(position).getNumber()));
        viewHolder.lessonProgressBar.setProgress(mItems.get(position).getProgress());
        viewHolder.lessonNameTextView.setText(mItems.get(position).getName());
        viewHolder.lessonProgressTextView.setText(String.valueOf(mItems.get(position).getProgress() + "%"));

        return rowView;
    }

    static class ViewHolder {
        public TextView lessonNumberTextView;
        public ProgressBar lessonProgressBar;
        public TextView lessonNameTextView;
        public TextView lessonProgressTextView;

        public ViewHolder(View view)
        {
            lessonNumberTextView = (TextView) view.findViewById(R.id.lesson_number_text_view);
            lessonProgressBar = (ProgressBar)view.findViewById(R.id.lesson_progress_bar);
            lessonNameTextView = (TextView) view.findViewById(R.id.lesson_name_text_view);
            lessonProgressTextView = (TextView)view.findViewById(R.id.lesson_progress_text_view);
        }
    }
}
