package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razjelll on 05.12.2016.
 */

public class CategoriesAdapter extends BaseAdapter implements Filterable {

    private List<Category> mItems;
    private List<Category> mFilteredItems;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;
    private ValueFilter mFilter;

    public CategoriesAdapter(Context context, int resource, List<Category> data) {
        //super(context, resource, data);
        mItems = new ArrayList(data);
        mFilteredItems = data;
        mContext = context;
        mResource = resource;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mFilteredItems.size();
    }

    public Object getItem(int position) {
        return mFilteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFilteredItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            //LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.categoryTextView.setText(mFilteredItems.get(position).getName());
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ValueFilter();
        }
        return mFilter;
    }

    static class ViewHolder {
        public TextView categoryTextView;

        public ViewHolder(View view) {
            categoryTextView = (TextView) view.findViewById(R.id.category_text_view);
        }
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Category> list = mItems;
            int count = list.size();
            final List<Category> nlist = new ArrayList<Category>(count);
            String itemString ;
            for(int i=0; i<count; i++)
            {
                itemString = list.get(i).getName();
                if(itemString.toLowerCase().contains(filterString))
                {
                    nlist.add(mItems.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredItems = (ArrayList<Category>) results.values;
            notifyDataSetChanged();
        }
    }
}
