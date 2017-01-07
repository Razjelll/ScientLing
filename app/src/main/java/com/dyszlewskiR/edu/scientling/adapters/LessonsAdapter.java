package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.util.List;

/**
 * Created by Razjelll on 04.12.2016.
 */

public class LessonsAdapter extends ArrayAdapter {

    private List<Lesson> mItems;
    private Context mContext;
    private int mResource;

    public LessonsAdapter(Context context, int resource, List<Lesson> data) {
        super(context, resource, data);
        mItems = data;
        mContext = context;
        mResource = resource;
    }

    public void setData(List<Lesson> data) {
        mItems = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if(!mItems.get(position).getName().equals(Constants.DEFAULT_LESSON_NAME)){
            viewHolder.lessonNameTextView.setText(mItems.get(position).getName());
        } else {
            viewHolder.lessonNameTextView.setText(mContext.getString(R.string.lack));
        }

        if (mItems.get(position).getSet() != null) {
            viewHolder.setNameTextView.setText(mItems.get(position).getSet().getName());
        }
        if(mItems.get(position).getNumber() != Constants.DEFAULT_LESSON_NUMBER){
            viewHolder.lessonNumberTextView.setText(String.valueOf(mItems.get(position).getNumber()));
        } else {
            viewHolder.lessonNumberTextView.setText(mContext.getString(R.string.lack));
        }
        return rowView;


    }

    static class ViewHolder {
        public TextView lessonNameTextView;
        public TextView setNameTextView;
        public TextView lessonNumberTextView;

        public ViewHolder (View view)
        {
            lessonNameTextView = (TextView) view.findViewById(R.id.lesson_text_view);
            setNameTextView = (TextView) view.findViewById(R.id.set_text_view);
            lessonNumberTextView = (TextView) view.findViewById(R.id.lesson_number_text_view);
        }

    }
}
