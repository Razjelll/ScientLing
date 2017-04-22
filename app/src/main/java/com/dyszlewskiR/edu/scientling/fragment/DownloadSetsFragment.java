package com.dyszlewskiR.edu.scientling.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetCreator;
import com.dyszlewskiR.edu.scientling.data.models.models.Language;
import com.dyszlewskiR.edu.scientling.data.models.models.SetItem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.net.URLConnector;
import com.dyszlewskiR.edu.scientling.net.requests.DownloadSetRequest;
import com.dyszlewskiR.edu.scientling.net.responses.DownloadSetResponse;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.json.SetsJsonReader;
import com.dyszlewskiR.edu.scientling.utils.Constants;
import com.dyszlewskiR.edu.scientling.utils.ResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DownloadSetsFragment extends Fragment {

    private final String LOG_TAG = "DownloadSetsFragment";
    private final int SIMPLE_ADAPTER_RESOURCE = R.layout.item_simple;

    private final String REQUEST_URI = Constants.SERVER_ADDRESS + "/sets";
    private final int ADAPTER_ITEM_RESOURCE = R.layout.item_downloaded_set;
    private final int TIME_OUT = 3000;

    private final int ITEMS_ON_LIST = 10;
    private final int EMPTY_LANGUAGE_ID = -1;

    private ListView mListView;
    private TextView mTextView;
    private DownloadSetAdapter mAdapter;
    private LanguageSpinnerAdapter mLanguageAdapter;

    private ViewGroup mLoadingContainer;
    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingTextView;

    private EditText mSearchEditText;
    private ViewGroup mFilterContainer;
    private Spinner mL1Spinner;
    private Spinner mL2Spinner;
    private Spinner mSortingSpinner;
    private Button mSearchButton;

    private View mListFooter;
    private Button mFooterButton;
    private ProgressBar mFooterProgressBar;
    private int mPage;

    public DownloadSetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_sets, container, false);
        setupControls(view);
        setListFooter();
        setAdapters();


        ServerAsyncTask task = new ServerAsyncTask(false);
        //task.execute("http://damianchodorek.com/wsexample/");
        //task.execute("http://10.0.2.2:3229");
        task.execute(prepareRequestUri(mPage));
        return view;
    }

    private void setupControls(View view) {
        mListView = (ListView) view.findViewById(R.id.list);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        mLoadingContainer = (ViewGroup) view.findViewById(R.id.loading_container);
        mLoadingProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress_bar);
        mLoadingTextView = (TextView) view.findViewById(R.id.loading_text_view);
        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mFilterContainer = (ViewGroup) view.findViewById(R.id.filter_container);
        mL1Spinner = (Spinner) view.findViewById(R.id.l1_spinner);
        mL2Spinner = (Spinner) view.findViewById(R.id.l2_spinner);
        mSortingSpinner = (Spinner) view.findViewById(R.id.sorting_spinner);
        mSearchButton = (Button) view.findViewById(R.id.get_button);
    }

    private void setListFooter() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mListFooter = inflater.inflate(R.layout.footer_download_set, null);
        mFooterButton = (Button) mListFooter.findViewById(R.id.more_button);
        mFooterProgressBar = (ProgressBar) mListFooter.findViewById(R.id.footer_progress_bar);
        mListView.addFooterView(mListFooter);
    }

    public void setAdapters() {
        mAdapter = new DownloadSetAdapter(getContext(), ADAPTER_ITEM_RESOURCE, new ArrayList<SetItem>());
        mListView.setAdapter(mAdapter);

        String[] sortingList = getContext().getResources().getStringArray(R.array.sorting_downloaded_set);
        mSortingSpinner.setAdapter(new ArrayAdapter<String>(getContext(), SIMPLE_ADAPTER_RESOURCE, sortingList));
        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        List<Language> languageList = dataManager.getLanguages();
        Language anyLanguage = new Language(EMPTY_LANGUAGE_ID);
        anyLanguage.setName("any");

        languageList.add(0, anyLanguage);
        mLanguageAdapter = new LanguageSpinnerAdapter(getContext(), SIMPLE_ADAPTER_RESOURCE, languageList);
        mL2Spinner.setAdapter(mLanguageAdapter);
        mL1Spinner.setAdapter(mLanguageAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListeners();
    }

    private void setListeners() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage = 0;
                String uri = prepareRequestUri(mPage);
                ServerAsyncTask task = new ServerAsyncTask(false);
                task.execute(uri);
            }
        });

        mFooterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = prepareRequestUri(++mPage);
                ServerAsyncTask task = new ServerAsyncTask(true);
                task.execute(uri);
            }
        });

        mSortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPage = 0;
                String uri = prepareRequestUri(mPage);
                ServerAsyncTask task = new ServerAsyncTask(false);
                task.execute(uri);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_download_sets, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                setSearchControlsVisibility();
                return true;
            case R.id.filter_button:
                setFilterContainerVisibility();
                return true;
            case R.id.sorting_button:
                setSortingControlsVisibility();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSearchControlsVisibility() {
        if (mSearchEditText.getVisibility() == View.VISIBLE) {
            mSearchEditText.setVisibility(View.GONE);
            if (mFilterContainer.getVisibility() == View.GONE) {
                mSearchButton.setVisibility(View.GONE);
            }
        } else {
            mSearchEditText.setVisibility(View.VISIBLE);
            mSearchButton.setVisibility(View.VISIBLE);
        }
    }

    private void setFilterContainerVisibility() {
        if (mFilterContainer.getVisibility() == View.VISIBLE) {
            mFilterContainer.setVisibility(View.GONE);
            if (mSearchEditText.getVisibility() == View.GONE) {
                mSearchButton.setVisibility(View.GONE);
            }
        } else {
            mFilterContainer.setVisibility(View.VISIBLE);
            mSearchButton.setVisibility(View.VISIBLE);
        }
    }

    private void setSortingControlsVisibility() {
        if (mSortingSpinner.getVisibility() == View.VISIBLE) {
            mSortingSpinner.setVisibility(View.GONE);
        } else {
            mSortingSpinner.setVisibility(View.VISIBLE);
        }
    }

    private final String PARAM_L2 = "l2";
    private final String PARAM_L1 = "l1";
    private final String PARAM_NAME = "name";
    private final String PARAM_SORT = "sort";
    private final String PARAM_PAGE = "page";
    private final String PARAM_LIMIT = "limit";

    private String prepareRequestUri(int page) {
        StringBuilder stringBuilder = new StringBuilder(REQUEST_URI);
        stringBuilder.append("?");
        long l2Id = mLanguageAdapter.getItem(mL2Spinner.getSelectedItemPosition()).getId();
        long l1Id = mLanguageAdapter.getItem(mL1Spinner.getSelectedItemPosition()).getId();
        if (l2Id != EMPTY_LANGUAGE_ID) {
            stringBuilder.append(PARAM_L2).append("=").append(String.valueOf(l2Id)).append("&");
        }
        if (l1Id != EMPTY_LANGUAGE_ID) {
            stringBuilder.append(PARAM_L1).append("=").append(String.valueOf(l1Id)).append("&");
        }
        if (mSearchEditText.getVisibility() == View.VISIBLE && !mSearchEditText.getText().toString().isEmpty()) {
            stringBuilder.append(PARAM_NAME).append("=").append(mSearchEditText.getText().toString().toLowerCase()).append("&");
        }
        if (mSortingSpinner.getSelectedItemPosition() != 0) {
            stringBuilder.append(PARAM_SORT).append("=").append(mSortingSpinner.getSelectedItemPosition()).append("&");
        }
        if (page != 0) {
            stringBuilder.append(PARAM_PAGE).append("=").append(page).append("&");
        }
        stringBuilder.append(PARAM_LIMIT).append("=").append(ITEMS_ON_LIST);
        return stringBuilder.toString();
    }

    //region ServerAsyncTask
    private class ServerAsyncTask extends AsyncTask<String, Void, List<SetItem>> {

        /**
         * określa czy pobieramy więcej elementów i dodajemy je do listy
         * false oznacza, że podczas pobierania usuwane są wszystkie elementy i dodawane pobrane
         */
        private boolean mDownloadMore;

        public ServerAsyncTask(boolean downloadMore) {
            mDownloadMore = downloadMore;
        }

        @Override
        protected void onPreExecute() {
            if (mDownloadMore) {
                mFooterButton.setVisibility(View.GONE);
                mFooterProgressBar.setVisibility(View.VISIBLE);


            } else {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();

                mListView.setVisibility(View.GONE);
                mLoadingContainer.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<SetItem> doInBackground(String... params) {
            try {
                URLConnection connection = URLConnector.getConnection(params[0], TIME_OUT);
                //InputStream response = connection.getInputStream();
                InputStream in = new BufferedInputStream(connection.getInputStream());
                List<SetItem> items = SetsJsonReader.readSetsFromInputStream(in);
                return items;
            } catch (SocketTimeoutException e) {
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<SetItem> result) {

            if (result != null) {
                mAdapter.addAll(result);
                if (mDownloadMore) { //dociąganie elementów
                    mFooterProgressBar.setVisibility(View.GONE);
                    mFooterButton.setVisibility(View.VISIBLE);
                    //jeśli pobraliśmy już wszystko co mogliśmy pobrać ukrywamy przycisk
                    if (result.size() < ITEMS_ON_LIST) {
                        mListFooter.setVisibility(View.GONE);
                    }
                } else { //pobieranie nowej listy
                    if (result.size() > 0) {
                        mLoadingContainer.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mListFooter.setVisibility(View.VISIBLE);
                    } else { // w przypadku jeśli nie będzie żadnych pasujących zestawów
                        mLoadingProgressBar.setVisibility(View.GONE);
                        mLoadingTextView.setText(getString(R.string.sets_not_found));
                    }
                }
            } else {
                mLoadingProgressBar.setVisibility(View.GONE);
                mLoadingTextView.setText(getString(R.string.connection_error));
            }
            //mTextView.setText(result);
        }
    }

    //endregion

    //region DownloadSetAdapter
    private class DownloadSetAdapter extends ArrayAdapter {

        private List<SetItem> mItems;
        private Context mContext;
        private int mResource;

        public DownloadSetAdapter(Context context, int resource, List<SetItem> data) {
            super(context, resource, data);
            mContext = context;
            mResource = resource;
            mItems = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(mResource, null);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.nameTextView.setText(mItems.get(position).getName());
            if (mItems.get(position).getLanguageL2() != null) {
                viewHolder.languageL2TextView.setText(ResourceUtils.getString(mItems.get(position).getLanguageL2(), getContext()));
            }
            if (mItems.get(position).getLanguageL1() != null) {
                viewHolder.languageL1TextView.setText(ResourceUtils.getString(mItems.get(position).getLanguageL1(), getContext()));
            }

            viewHolder.authorTextView.setText(mItems.get(position).getAuthor());
            viewHolder.numberWordsTextView.setText(String.valueOf(mItems.get(position).getWordsCount()));
            viewHolder.sizeTextView.setText(String.valueOf(mItems.get(position).getBasicSize()));
            viewHolder.ratingTextView.setText(String.valueOf(mItems.get(position).getRating()));
            viewHolder.downloadsTextView.setText(String.valueOf(mItems.get(position).getDownloads()));
            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadSetAsyncTask task = new DownloadSetAsyncTask();
                    task.execute(mItems.get(position).getId());
                }
            });

            viewHolder.downloadsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });

            return rowView;
        }
    }
    //endregion

    //region LanguageSpinnerAdapter
    private class LanguageSpinnerAdapter extends ArrayAdapter {
        private Context mContext;
        private int mResource;
        private List<Language> mLanguages;

        public LanguageSpinnerAdapter(Context context, int resource, List<Language> data) {
            super(context, resource, data);
            mContext = context;
            mResource = resource;
            mLanguages = data;
        }

        @Override
        public Language getItem(int position) {
            return mLanguages.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(mResource, null);
            }
            TextView textView = (TextView) rowView.findViewById(R.id.text_view);
            String name = mLanguages.get(position).getName();
            textView.setText(ResourceUtils.getString(name, mContext));

            return rowView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(mResource, null);
            }
            TextView textView = (TextView) rowView.findViewById(R.id.text_view);
            String name = mLanguages.get(position).getName();
            textView.setText(ResourceUtils.getString(name, mContext));

            return rowView;
        }
    }

    //endregion

    private static class ViewHolder {
        public TextView nameTextView;
        public TextView languageL1TextView;
        public TextView languageL2TextView;
        public TextView authorTextView;
        public TextView numberWordsTextView;
        public TextView sizeTextView;
        public TextView ratingTextView;
        public TextView downloadsTextView;
        public ImageView downloadButton;

        public ViewHolder(View view) {
            nameTextView = (TextView) view.findViewById(R.id.name_text_view);
            languageL1TextView = (TextView) view.findViewById(R.id.language_l1_text_view);
            languageL2TextView = (TextView) view.findViewById(R.id.language_l2_text_view);
            authorTextView = (TextView) view.findViewById(R.id.author_text_view);
            numberWordsTextView = (TextView) view.findViewById(R.id.number_words_text_view);
            sizeTextView = (TextView) view.findViewById(R.id.size_text_view);
            ratingTextView = (TextView) view.findViewById(R.id.rating_text_view);
            downloadsTextView = (TextView) view.findViewById(R.id.downloads_text_view);
            downloadButton = (ImageView) view.findViewById(R.id.download_button);

        }

    }

    private class DownloadSetAsyncTask extends AsyncTask<Long, Void, Void>{

        @Override
        protected Void doInBackground(Long... params) {
            DownloadSetRequest request = new DownloadSetRequest(params[0]);
            try {
                DownloadSetResponse response = new DownloadSetResponse(request.start());
                SetCreator setCreator = new SetCreator();
                VocabularySet set = setCreator.createFromJson(response.getSetJson());
                JsonNode node = null;
                while((node = response.getLessonJson()) != null){
                    Log.d("DownloadAT", node.toString());
                }
                while((node = response.getWordJson()) != null){
                    Log.d("DownloadAT", node.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
