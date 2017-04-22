package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;

import java.util.List;

/**
 * Created by Razjelll on 06.01.2017.
 */

public class ExercisesTypesAdapter extends ArrayAdapter {
    private List<String> mItems;
    private int mSelectedItem;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    public ExercisesTypesAdapter(Context context, int resource, List<String> data, int selected) {
        super(context, resource, data);
        mContext = context;
        mResource = resource;
        mItems = data;
        mSelectedItem = selected;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        if (mSelectedItem == position + 1) {
            viewHolder.radioButton.setSelected(true);
        }
        viewHolder.exerciseNameTextView.setText(mItems.get(position));

        return rowView;
    }

    static class ViewHolder {
        public RadioButton radioButton;
        public TextView exerciseNameTextView;

        public ViewHolder(View view) {
            //radioButton = (RadioButton) view.findViewById(R.id.radio_button);
            exerciseNameTextView = (TextView) view.findViewById(R.id.exercise_name_text_view);
        }
    }
}
