package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.models.UsersSet;
import com.dyszlewskiR.edu.scientling.dialogs.UploadSetDialog;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.net.requests.DeleteImagesRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.DeleteRecordsRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.DeleteSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.DescriptionRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UpdateDescriptionRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UploadSetRequest;
import com.dyszlewskiR.edu.scientling.services.net.requests.UsersSetsRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.DeleteMediaResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.DeleteSetResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.DescriptionResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.UpdateDescriptionResponse;
import com.dyszlewskiR.edu.scientling.services.net.responses.UsersSetsResponse;
import com.dyszlewskiR.edu.scientling.services.net.values.MediaType;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class SetsServerFragment extends Fragment {

    private final int LAYOUT_RESOURCE = R.layout.fragment_users_sets;
    private final int ADAPTER_ITEM_RESOURCE = R.layout.item_users_set;

    private ListView mListView;
    private ViewGroup mWaitingContainer;
    private TextView mErrorTextView;
    private UsersSetsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT_RESOURCE, container, false);
        setupControls(view);
        return view;
    }

    private void setupControls(View view){
        mListView = (ListView)view.findViewById(R.id.list);
        mWaitingContainer = (ViewGroup)view.findViewById(R.id.waiting_container);
        mErrorTextView = (TextView)view.findViewById(R.id.error_text_view);
    }

    public void setAdapter(List<UsersSet> data){
        if(data != null){
            DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
            mAdapter = new UsersSetsAdapter(getContext(), ADAPTER_ITEM_RESOURCE, data, dataManager);
            mListView.setAdapter(mAdapter);
            registerForContextMenu(mListView);
        }
    }

    private final int DELETE_ALL = R.string.delete;
    private final int DELETE_IMAGES = R.string.delete_images;
    private final int DELETE_RECORDS = R.string.delete_records;
    private final int CHANGE_DESCRIPTION = R.string.change_description;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view , ContextMenu.ContextMenuInfo menuInfo){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;
        UsersSet set = mAdapter.getItem(position);
        menu.setHeaderTitle(set.getName());
        menu.add(0, DELETE_ALL, 0, getString(DELETE_ALL));
        if(set.hasImages()){
            menu.add(0, DELETE_IMAGES, 0, getString(DELETE_IMAGES));
        }
        if(set.hasRecords()){
            menu.add(0, DELETE_RECORDS, 0, getString(DELETE_RECORDS));
        }
        menu.add(0, CHANGE_DESCRIPTION, 0, getString(CHANGE_DESCRIPTION));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        DataManager dataManager = LingApplication.getInstance().getDataManager();
        int position = info.position;
        final UsersSet set = mAdapter.getItem(position);
        switch (item.getItemId()){
            case DELETE_ALL:
                //new DeleteSetRunnable(set,getContext()).start();
                long setId = mAdapter.getItemId(position);
                new DeleteSetAsyncTask().execute(mAdapter.getItemId(position)); break;
            case DELETE_IMAGES:
                //new DeleteMediaRunnable(set, getContext(), DeleteMediaRunnable.IMAGES,dataManager ).start(); break;
                DeleteMediaAsyncTask imagesTask = new DeleteMediaAsyncTask(getContext(), MediaType.IMAGES, dataManager);
                imagesTask.setCallback(new DeleteMediaAsyncTask.Callback() {
                    @Override
                    public void onDelete(long setId, MediaType mediaType) {
                        mAdapter.getItemById(setId).setHasImages(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                imagesTask.execute(set.getId());
                break;
            case DELETE_RECORDS:
                DeleteMediaAsyncTask recordsTask = new DeleteMediaAsyncTask(getContext(), MediaType.RECORDS, dataManager);
                recordsTask.setCallback(new DeleteMediaAsyncTask.Callback() {
                    @Override
                    public void onDelete(long setId, MediaType mediaType) {
                        mAdapter.getItemById(setId).setHasRecords(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                recordsTask.execute(set.getId());
                break;
            case CHANGE_DESCRIPTION:
                DescriptionDialog dialog = new DescriptionDialog(getContext(), set);
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

    @Override
    public void onResume(){
        Log.d(getClass().getSimpleName(), "onResume");
        super.onResume();
        UsersSetsAsyncTask task = new UsersSetsAsyncTask();
        task.execute(LogPref.getLogin(getContext()));
    }

    public void deleteItem(Long id){
        if(id != null) {
            UsersSet set = mAdapter.getItemById(id);
            mAdapter.remove(set);
            mAdapter.notifyDataSetChanged();
        }


    }

    private class UsersSetsAdapter extends ArrayAdapter<UsersSet>{
        private Context mContext;
        private int mResource;
        private List<UsersSet> mItems;
        private DataManager mDataManager;

        public UsersSetsAdapter(Context context, int resource, List<UsersSet> data, DataManager dataManager){
            super(context,resource,data);
            mContext = context;
            mResource = resource;
            mItems = data;
            mDataManager = dataManager;
        }

        public UsersSet getItemById(long id){
            for(UsersSet item: mItems){
                if(item.getId() == id){
                    return item;
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position){
            return mItems.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent){
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
            UsersSet item = mItems.get(position);
            viewHolder.nameTextView.setText(item.getName());
            Language l1 = mDataManager.getLanguageById(item.getL1());
            if(l1 != null){
                viewHolder.l1TextView.setText(l1.getName());
            }
            Language l2 = mDataManager.getLanguageById(item.getL2());
            if(l2 != null){
                viewHolder.l1TextView.setText(l2.getName());
            }
            viewHolder.actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.showContextMenuForChild(v);
                }
            });

            return rowView;
        }
    }

    private static class ViewHolder{
        public TextView nameTextView;
        public TextView l1TextView;
        public TextView l2TextView;
        public ImageView actionButton;

        public ViewHolder(View view){
            nameTextView = (TextView)view.findViewById(R.id.name_text_view);
            l1TextView = (TextView)view.findViewById(R.id.l1_text_view);
            l2TextView = (TextView)view.findViewById(R.id.l2_text_view);
            actionButton = (ImageView)view.findViewById(R.id.action_button);
        }
    }

    private class UsersSetsAsyncTask extends AsyncTask<String, Void, List<UsersSet>>{

        @Override
        protected List<UsersSet> doInBackground(String... params) {
            UsersSetsRequest request = new UsersSetsRequest(LogPref.getLogin(getContext()), LogPref.getPassword(getContext()));
            try {
                HttpURLConnection connection = request.start(params[0]);
                UsersSetsResponse response = new UsersSetsResponse(connection);
                List<UsersSet> items = response.getUsersSetsList();
                response.closeConnection();
                return items;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<UsersSet> result){
            setAdapter(result);
            mListView.setVisibility(View.VISIBLE);
            mWaitingContainer.setVisibility(View.GONE);
        }
    }

    private class DeleteSetAsyncTask extends AsyncTask<Long, Void, Long>{

        @Override
        protected Long doInBackground(Long... params) {
            HttpURLConnection connection = null;
            long setId = params[0];
            try {
                connection = DeleteSetRequest.start(setId, LogPref.getLogin(getContext()), LogPref.getPassword(getContext()));
                DeleteSetResponse response = new DeleteSetResponse(connection);
                if(response.getResponse()){
                    DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
                    dataManager.updateImageUploaded(false, setId);
                    dataManager.updateRecordsUploaded(false, setId);
                    dataManager.updateSetGlobalId(null, setId);

                    return setId;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result){
            if(result != null) {
                deleteItem(result);
            }
        }
    }
}

class DeleteMediaAsyncTask extends AsyncTask<Long, Void, Void>{

    private Context mContext;
    private MediaType mMediaType;
    private DataManager mDataManager;
    private Callback mCallback;
    private long mSetId;

    public interface Callback{
        void onDelete(long setId, MediaType mediaType);
    }

    public DeleteMediaAsyncTask(Context context, MediaType mediaType, DataManager dataManager){
        mContext = context;
        mMediaType = mediaType;
        mDataManager = dataManager;
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Long... params) {
        HttpURLConnection connection = null;
        mSetId = params[0];
        try {
            switch (mMediaType){
                case IMAGES:
                    connection = DeleteImagesRequest.start(mSetId, LogPref.getLogin(mContext), LogPref.getPassword(mContext)); break;
                case RECORDS:
                    connection = DeleteRecordsRequest.start(mSetId, LogPref.getLogin(mContext), LogPref.getPassword(mContext)); break;
            }
            DeleteMediaResponse response = new DeleteMediaResponse(connection);
            response.getResponse();
            response.closeConnection();

            if(response.getResultCode() == DeleteMediaResponse.DELETED){
                switch (mMediaType){
                    case IMAGES:
                        mDataManager.updateImageUploaded(false, mSetId); break;
                    case RECORDS:
                        mDataManager.updateRecordsUploaded(false, mSetId); break;
                }
            }
            //connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        if(mCallback != null){
            mCallback.onDelete(mSetId, mMediaType);
        }
    }
}


class DescriptionDialog extends Dialog {
    private final int CONTENT_RESOURCE = R.layout.dialog_upload_set;

    private EditText mDescriptionEditText;
    private Button mUploadButton;
    private UsersSet mSet;
    private Context mContext;

    public DescriptionDialog(@NonNull Context context, UsersSet set) {
        super(context);
        mContext = context;
        mSet = set;
        setTitle(set.getName());
        setContentView(CONTENT_RESOURCE);
        setupControls();
        setListeners();
        setValues(set.getId());
    }

    private void setupControls(){
        mDescriptionEditText = (EditText)findViewById(R.id.description_edit_text);
        mUploadButton = (Button)findViewById(R.id.upload_button);
    }

    private void setListeners(){
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mDescriptionEditText.getText().toString();
                new ChangeDescriptionThread(mSet.getId(),description, LogPref.getLogin(mContext), LogPref.getPassword(mContext)).start();
                dismiss();
            }
        });
    }

    private void setValues(long setId){
        DescriptionAsyncTask task = new DescriptionAsyncTask();
        task.execute(setId);
    }

    private class ChangeDescriptionThread extends Thread{

        private long mSetId;
        private String mDescription;
        private String mUsername;
        private String mPassword;

        public ChangeDescriptionThread(long setId, String description, String username, String password){
            mSetId = setId;
            mDescription = description;
            mUsername = username;
            mPassword = password;
        }

        @Override
        public void run(){
            HttpURLConnection connection = null;
            try {
                connection = UpdateDescriptionRequest.start(mSetId, mDescription, mUsername, mPassword);
                UpdateDescriptionResponse response = new UpdateDescriptionResponse(connection);
                if(response.getResultCode() == UpdateDescriptionResponse.INSERTED){
                    response.getResponse();
                    response.closeConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
        }
    }

    private class DescriptionAsyncTask extends AsyncTask<Long, Void, String>{

        @Override
        protected String doInBackground(Long... setId) {
            HttpURLConnection connection = null;
            try {
                 connection = DescriptionRequest.start(setId[0], LogPref.getLogin(getContext()),
                        LogPref.getPassword(getContext()));
                DescriptionResponse response = new DescriptionResponse(connection);
                String description = null;
                if(response.getResultCode() == DescriptionResponse.OK){
                    description  = response.getDescription();
                    response.closeConnection();
                    return description;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if(result != null){
                mDescriptionEditText.setText(result);
            }
        }
    }
}

