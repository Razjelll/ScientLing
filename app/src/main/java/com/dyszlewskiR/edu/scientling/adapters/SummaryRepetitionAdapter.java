package com.dyszlewskiR.edu.scientling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.utils.TranslationListConverter;

import java.util.List;

/**
 * Created by Razjelll on 22.12.2016.
 */

public class SummaryRepetitionAdapter extends ArrayAdapter {

    private List<Word> mItems;
    private Context mContext;
    private int mResource;

    public SummaryRepetitionAdapter(Context context, int resource, List<Word> data) {
        super(context, resource, data);
        mItems = data;
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(mResource, null);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)rowView.getTag();
        }
        //TODO zobaczyć jak działa zaznaczenie
        viewHolder.contentTextView.setText(mItems.get(position).getContent());
        String translations = TranslationListConverter.toString(mItems.get(position).getTranslations());
        viewHolder.translationTextView.setText(translations);
        int progress = mItems.get(position).getMasterLevel();
        if(progress < 0) { //jeżeli jest to pierwsze uczenia słówka, które nie jest zaznaczone w powtórkach ukrywamy postęp
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.progressTextView.setVisibility(View.GONE);
        } else //w przeciwnym razie ustawiamy wartości
        {
            viewHolder.progressBar.setProgress(progress);
            viewHolder.progressTextView.setText(progress + "%");
        }
        viewHolder.selectionButton.setSelected(mItems.get(position).isSelected());

        return rowView;
    }

    static class ViewHolder {
        public CheckBox checkBox;
        public TextView contentTextView;
        public TextView translationTextView;
        public ProgressBar progressBar;
        public TextView progressTextView;
        public ToggleButton selectionButton;

        public ViewHolder(View view)
        {
            checkBox = (CheckBox) view.findViewById(R.id.word_check_box);
            contentTextView = (TextView) view.findViewById(R.id.word_content_text_view);
            translationTextView = (TextView) view.findViewById(R.id.word_translation_text_view);
            progressBar = (ProgressBar)view.findViewById(R.id.word_progress_bar);
            progressTextView = (TextView)view.findViewById(R.id.word_progress_text_view);
            selectionButton = (ToggleButton)view.findViewById(R.id.word_selected_toggle_button);
        }
    }
}
