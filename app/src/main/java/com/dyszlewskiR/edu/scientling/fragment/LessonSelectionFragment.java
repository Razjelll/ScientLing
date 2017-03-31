package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LessonActivity;
import com.dyszlewskiR.edu.scientling.activity.SetSelectionActivity;
import com.dyszlewskiR.edu.scientling.adapters.LessonsAdapter;
import com.dyszlewskiR.edu.scientling.dialogs.LessonDialog;
import com.dyszlewskiR.edu.scientling.preferences.Settings;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.utils.Constants;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LessonSelectionFragment extends Fragment implements LessonDialog.Callback {

    private final int LAYOUT_RESOURCE = R.layout.fragment_lesson_selection;
    private final int ADAPTER_ITEM_RESOURCE = R.layout.item_lesson;
    private final String DEFUALT_LESSON_NUMBER = "0";

    private ListView mListView;
    private List<Lesson> mItems;
    private long mSetId;

    private Context mContext;
    private int mResource;
    private LessonAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getData();
        setHasOptionsMenu(true);
    }

    private void getData(){
        Intent intent = getActivity().getIntent();
        mSetId = intent.getLongExtra("set", Constants.DEFAULT_SET_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT_RESOURCE, container, false);
        setupControls(view);
        setListeners();
        loadData();
        return view;
    }

    private void setupControls(View view){
        mListView = (ListView)view.findViewById(R.id.list);
    }

    private void setListeners(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("result", mItems.get(position));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    private void loadData(){
        DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
        //można stworzyć nowy zbiór, ponieważ przy zapytaniu brane pod uwagę jest tylko id
        mItems = dataManager.getLessons(new VocabularySet(mSetId));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        setAdapter();
    }

    private void setAdapter(){
        mAdapter = new LessonAdapter(getActivity(), ADAPTER_ITEM_RESOURCE);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.lesson_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //TODO
                return true;
            case R.id.add_button:
                openLessonDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openLessonDialog(){
        LessonDialog dialog = new LessonDialog();
        dialog.setCallback(this);
        //TODO może będzie potrzebne przekazanie lekcji
        dialog.show(getFragmentManager(),"LessonDialog");
    }

    @Override
    public void onLessonOk(Lesson lesson) {
        mItems.add(lesson);
        mAdapter.notifyDataSetChanged();
        saveLessonInDb(lesson);
    }

    private void saveLessonInDb(Lesson lesson){
        DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
        //TODO chyba można tak zrobić, przetestować czy będzie poprawnie zapisywać
        lesson.setSet(new VocabularySet(mSetId));
        dataManager.saveLesson(lesson);
    }

    private class LessonAdapter extends BaseAdapter {

        public LessonAdapter(Context context, int resource){
            mContext = context;
            mResource = resource;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            ViewHolder viewHolder;
            if(rowView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(mResource, null);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)rowView.getTag();
            }

            if(mItems.get(position).getName().equals("")){
                viewHolder.lessonNameTextView.setText(getString(R.string.lack));
                viewHolder.lessonNumberTextView.setText(DEFUALT_LESSON_NUMBER);
            } else {
                viewHolder.lessonNameTextView.setText(mItems.get(position).getName());
                viewHolder.lessonNumberTextView.setText(String.valueOf(mItems.get(position).getNumber()));
            }
            return rowView;
        }
    }

    public static class ViewHolder{
        public TextView lessonNameTextView;
        public TextView lessonNumberTextView;
        //public ImageView actionHolder;

        public ViewHolder(View view){
            lessonNameTextView = (TextView)view.findViewById(R.id.lesson_text_view);
            lessonNumberTextView = (TextView)view.findViewById(R.id.lesson_number_text_view);
            //actionButton = (Button)view.findViewById(R.id.action_button);
        }
    }
}
