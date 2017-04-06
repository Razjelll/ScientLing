package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.LessonSelectionActivity;
import com.dyszlewskiR.edu.scientling.activity.SetEditActivity;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.data.DeletingLessonService;
import com.dyszlewskiR.edu.scientling.services.data.DeletingSetService;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetsManagerFragment extends Fragment {

    private final int LAYOUT_RESOURCE = R.layout.fragment_sets_manager;
    private final int ADAPTER_ITEM_RESOURCE = R.layout.item_set_manager;
    private final int ADD_REQUEST = 7233;
    private final int EDIT_REQUEST = 7334;

    private ListView mListView;
    private List<VocabularySet> mItems;
    private SetsManagerAdapter mAdapter;

    private int mLastEditedPosition;
    private int mDeletedPosition;

    public SetsManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadData();
        setHasOptionsMenu(true);
    }

    private void loadData(){
        DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
        mItems = dataManager.getSets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT_RESOURCE, container, false);
        setupControls(view);
        return view;
    }

    private void setupControls(View view){
        mListView = (ListView)view.findViewById(R.id.list);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        setSetsAdapter();
    }

    private void setSetsAdapter(){
        mAdapter = new SetsManagerAdapter(getContext(), ADAPTER_ITEM_RESOURCE, mItems);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_sets_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                Log.d("SentencesListActivity", "Add Click");
                startSetEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSetEditActivity(){
        Intent intent = new Intent(getActivity(), SetEditActivity.class);
        startActivityForResult(intent,ADD_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ADD_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                VocabularySet set = data.getParcelableExtra("result");
                mAdapter.add(set);
            }
        }
        if(requestCode == EDIT_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                VocabularySet set = data.getParcelableExtra("result");
                mAdapter.set(mLastEditedPosition, set);
            }
        }
    }

    private void deleteSet(VocabularySet set){
        if(mDeletedPosition >= 0){
            //TODO usunięcie katalogóów
            /*DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
            dataManager.deleteSet(set);*/
            startDeletingSetService(set);
            mAdapter.remove(set);

        }
    }

    private void startDeletingSetService(VocabularySet set){
        Intent intent = new Intent(getContext(), DeletingSetService.class);
        intent.putExtra("set", set);
        getActivity().startService(intent);
    }

    //region SetsManagerAdapter
    private class SetsManagerAdapter extends ArrayAdapter {

        private final int MENU_LESSONS = R.string.lessons;
        private final int MENU_EDIT = R.string.edit;
        private final int MENU_DELETE = R.string.delete;
        private final int MENU_CHOOSE = R.string.choose;

        private Context mContext;
        private int mResource;

        public SetsManagerAdapter(Context context, int resource, List<VocabularySet> data){
            super(context, resource, data);
            mContext = context;
            mResource = resource;
        }

        public void set(int position, VocabularySet set){
            mItems.set(position, set);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
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

            viewHolder.nameTextView.setText(mItems.get(position).getName());
            setupMenu(position, viewHolder);
            return rowView;
        }

        private void setupMenu(final int position, final ViewHolder viewHolder){
            viewHolder.actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.actionButton);
                    popupMenu.getMenu().add(MENU_LESSONS);
                    popupMenu.getMenu().add(MENU_EDIT);
                    popupMenu.getMenu().add(MENU_DELETE);
                    popupMenu.getMenu().add(MENU_CHOOSE);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals(mContext.getString(MENU_LESSONS))){
                                startLessonActivity(position);
                            }
                            if(item.getTitle().equals(mContext.getString(MENU_EDIT))){
                                mLastEditedPosition = position;
                                editItem(position);
                            }
                            if(item.getTitle().equals(mContext.getString(MENU_DELETE))){
                                deleteItem(position);
                            }
                            if(item.getTitle().equals(mContext.getString(MENU_CHOOSE))){
                                chooseItem(position);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void editItem(int itemPosition){
            Intent intent = new Intent(mContext, SetEditActivity.class);
            intent.putExtra("item", mItems.get(itemPosition));
            startActivityForResult(intent, EDIT_REQUEST);
        }

        private void deleteItem(int itemPosition){
            DeleteDialog dialog = new DeleteDialog(mContext, mItems.get(itemPosition));
            dialog.show();
        }

        private void chooseItem(int itemPosition){
            VocabularySet set = mItems.get(itemPosition);
            Intent intent = new Intent();
            intent.putExtra("result",set.getId());
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }

        private void startLessonActivity(int itemPosition){
            Intent intent = new Intent(getContext(), LessonSelectionActivity.class);
            intent.putExtra("set", mItems.get(itemPosition).getId());
            intent.putExtra("manager", true);
            startActivity(intent);
        }
    }

    public static class ViewHolder{
        public TextView nameTextView;
        public ImageView actionButton;

        public ViewHolder(View view){
            nameTextView = (TextView)view.findViewById(R.id.name_text_view);
            actionButton = (ImageView)view.findViewById(R.id.action_button);
        }
    }
    //endregion

    //region DeleteDialog
    private class DeleteDialog extends AlertDialog {


        protected DeleteDialog(Context context,final VocabularySet set) {
            super(context);
            this.setTitle(getString(R.string.delete));
            String message = getString(R.string.sure_delete_set) + "\n" + set.getName();
            this.setMessage(message);
            this.setButton(BUTTON_POSITIVE, getString(R.string.yes), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteSet(set);
                    dismiss();
                }
            });
            this.setButton(BUTTON_NEGATIVE, getString(R.string.no), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss(); //zamykamy dialog
                }
            });
        }
    }
    //endregion
}
