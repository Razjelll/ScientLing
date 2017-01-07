package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.utils.ResourceTranslator;

import java.util.List;


/**
 * Created by Razjelll on 29.11.2016.
 */

public class SetsSelectionAdapter extends ArrayAdapter<VocabularySet> {

    private List<VocabularySet> mItems;
    private Context mContext;
    private int mResource;
    private ViewHolder mViewHolder;

    public SetsSelectionAdapter(Context context, int resource, List<VocabularySet> data) {
        super(context, resource);

        mItems = data;
        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public VocabularySet getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.nameTextView.setText(mItems.get(position).getName());
        String language = ResourceTranslator.translate(mItems.get(position).getLanguageL2().getName(), mContext);
        viewHolder.languageTextView.setText(language);

        return rowView;
    }

    private class ViewHolder
    {
        public TextView nameTextView;
        public TextView languageTextView;

        public ViewHolder(View view)
        {
            nameTextView = (TextView) view.findViewById(R.id.set_list_name);
            languageTextView = (TextView) view.findViewById(R.id.set_list_language);
        }
    }
}
