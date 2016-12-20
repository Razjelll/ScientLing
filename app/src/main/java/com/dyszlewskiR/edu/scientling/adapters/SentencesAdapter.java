package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.Sentence;

import java.util.List;

/**
 * Created by Razjelll on 27.11.2016.
 */

public class SentencesAdapter extends ArrayAdapter {


    private List<Sentence> mItems;
    private Context mContext;
    private int mResource;

    public SentencesAdapter(Context context, int resource, List<Sentence> data) {
        super(context, resource);

        mContext = context;
        mResource = resource;
        mItems = data;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
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

        viewHolder.contentTextView.setText(mItems.get(position).getContent());
        viewHolder.translationTextView.setText(mItems.get(position).getTranslation());
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = position;
                mItems.remove(pos);
                notifyDataSetChanged();
            }
        });
        return rowView;
    }

    static class ViewHolder {
        public TextView contentTextView;
        public TextView translationTextView;
        public Button deleteButton;

        public ViewHolder(View view)
        {
            contentTextView = (TextView) view.findViewById(R.id.sentencesContentText);
            translationTextView = (TextView) view.findViewById(R.id.sentencesTranslationText);
            deleteButton = (Button) view.findViewById(R.id.sentencesDeleteButton);
        }
    }

    /*private class OnDeleteClickListener implements View.OnClickListener {

        private ListView mView;
        private int mPosition;

        public OnDeleteClickListener(ListView view, int position)
        {
            mView= view;
            mPosition = position;
        }
        @Override
        public void onClick(View v) {
            mItems.remove(mPosition);
            mView.invalidateViews();
        }
    }*/
}
