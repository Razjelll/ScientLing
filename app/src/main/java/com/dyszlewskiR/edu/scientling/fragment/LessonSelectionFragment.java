package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.WordsManagerActivity;
import com.dyszlewskiR.edu.scientling.app.LingApplication;
import com.dyszlewskiR.edu.scientling.data.models.models.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.dialogs.LessonDialog;
import com.dyszlewskiR.edu.scientling.services.data.DataManager;
import com.dyszlewskiR.edu.scientling.services.data.DeletingLessonService;
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

    private int mLastEditedPosition;

    /**
     * Określa czy aktywność działa w trybie managera(true) czy w trybie wyboru lekcji(false)
     */
    private boolean mManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setHasOptionsMenu(true);
    }

    private void getData() {
        Intent intent = getActivity().getIntent();
        mSetId = intent.getLongExtra("set", Constants.DEFAULT_SET_ID);
        mManager = intent.getBooleanExtra("manager", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT_RESOURCE, container, false);
        setupControls(view);
        setListeners();
        loadData();
        return view;
    }

    private void setupControls(View view) {
        mListView = (ListView) view.findViewById(R.id.list);
    }

    private void setListeners() {
        if (!mManager) {
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
    }

    private void loadData() {
        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        //można stworzyć nowy zbiór, ponieważ przy zapytaniu brane pod uwagę jest tylko id
        mItems = dataManager.getLessons(new VocabularySet(mSetId));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new LessonAdapter(getActivity(), ADAPTER_ITEM_RESOURCE);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lesson_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private void openLessonDialog() {
        LessonDialog dialog = new LessonDialog();
        dialog.setCallback(this);
        //TODO może będzie potrzebne przekazanie lekcji
        dialog.show(getFragmentManager(), "LessonDialog");
    }

    @Override
    public void onLessonOk(Lesson lesson, boolean edit) {
        long lessonId = saveLessonInDb(lesson, edit);
        lesson.setId(lessonId);
        if (!edit) { //dodawanie nowej lekcji
            mItems.add(lesson);
        } else { //edycja
            mItems.set(mLastEditedPosition, lesson);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Zapisuje lub aktualizuje lekcje w bazie danych.
     *
     * @param lesson model lekcji który ma zostać zapisany
     * @param update określa czy lekcja ma zostać zaktualizowana lub zapisana
     * @return numer identyfikacyjny zapisanej lekcji
     */
    private long saveLessonInDb(Lesson lesson, boolean update) {
        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        //TODO chyba można tak zrobić, przetestować czy będzie poprawnie zapisywać
        lesson.setSet(new VocabularySet(mSetId));
        long id;
        if (update) {
            dataManager.updateLesson(lesson);
            id = lesson.getId();
        } else {
            id = dataManager.saveLesson(lesson);

        }
        return id;
    }

    private void deleteLesson(Lesson lesson, long newLessonId) {
        DataManager dataManager = ((LingApplication) getActivity().getApplication()).getDataManager();
        //TODO pobranie listy obrazków i nagrań
        dataManager.deleteLesson(lesson, newLessonId);
        //TODO jeśli wszystko poszło ok usunięcie obrazków i nagrań
    }

    //region LessonAdapter
    private class LessonAdapter extends BaseAdapter {

        private final int MENU_EDIT = R.string.edit;
        private final int MENU_DELETE = R.string.delete;
        private final int MENU_WORDS = R.string.words;
        private final int WORDS_REQUEST = 9673;

        public LessonAdapter(Context context, int resource) {
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
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(mResource, null);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            if (mItems.get(position).getName().equals("")) {
                viewHolder.lessonNameTextView.setText(getString(R.string.unallocated));
                viewHolder.lessonNumberTextView.setText(DEFUALT_LESSON_NUMBER);
            } else {
                viewHolder.lessonNameTextView.setText(mItems.get(position).getName());
                viewHolder.lessonNumberTextView.setText(String.valueOf(mItems.get(position).getNumber()));
            }
            //if(mItems.get(position).getNumber() != Constants.DEFAULT_LESSON_NUMBER){
            setupMenu(position, viewHolder);
            //} else {
            //ukrywamy przycisk akcji, ponieważ nie chcemy aby ktoś modyfikował lekcję domyślną
            // viewHolder.actionButton.setVisibility(View.GONE);
            //}

            return rowView;
        }

        private void setupMenu(final int position, final ViewHolder viewHolder) {
            viewHolder.actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.actionButton);
                    if (mItems.get(position).getNumber() != Constants.DEFAULT_LESSON_NUMBER) {
                        popupMenu.getMenu().add(getString(MENU_EDIT));
                        popupMenu.getMenu().add(getString(MENU_DELETE));
                    }

                    if (mManager) {
                        popupMenu.getMenu().add(getString(MENU_WORDS));
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals(getString(MENU_EDIT))) {
                                editItem(position);
                                mLastEditedPosition = position;
                            }
                            if (item.getTitle().equals(getString(MENU_DELETE))) {
                                deleteItem(position);
                            }
                            if (item.getTitle().equals(getString(MENU_WORDS))) {
                                startWordsManagerActivity(position);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void editItem(int itemPosition) {
            LessonDialog dialog = new LessonDialog();
            dialog.setCallback(LessonSelectionFragment.this);
            dialog.setLesson(mItems.get(itemPosition));
            dialog.show(getFragmentManager(), "LessonDialog");
        }

        private void deleteItem(int itemPosition) {
            DeleteDialog dialog = new DeleteDialog(mContext, mItems.get(itemPosition));
            dialog.show();
        }

        private void startWordsManagerActivity(int itemPosition) {
            Intent intent = new Intent(mContext, WordsManagerActivity.class);
            intent.putExtra("set", mSetId);
            intent.putExtra("lesson", mItems.get(itemPosition));
            startActivity(intent);
        }

    }

    private static class ViewHolder {
        public TextView lessonNameTextView;
        public TextView lessonNumberTextView;
        public ImageView actionButton;

        public ViewHolder(View view) {
            lessonNameTextView = (TextView) view.findViewById(R.id.lesson_text_view);
            lessonNumberTextView = (TextView) view.findViewById(R.id.lesson_number_text_view);
            actionButton = (ImageView) view.findViewById(R.id.action_button);
        }
    }
    //endregion

    //region DeleteDialog
    private class DeleteDialog extends AlertDialog implements LessonDialog.Callback {

        protected DeleteDialog(@NonNull Context context, final Lesson lesson) {
            super(context);
            this.setTitle(getString(R.string.deleting_lesson));
            String message = getString(R.string.sure_delte_lesson) + "\n" + lesson.getName();
            this.setMessage(message);
            this.setButton(BUTTON_POSITIVE, getString(R.string.delete_with_words), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //deleteLesson(lesson, -1);
                    startDeletingService(lesson, -1, mSetId);
                    mItems.remove(lesson);
                    mAdapter.notifyDataSetChanged();
                }
            });
            /*this.setButton(BUTTON_NEUTRAL, getString(R.string.delete_without_words), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LessonDialog lessonDialog = new LessonDialog();

                    lessonDialog.setTitle(getString(R.string.change_lesson));
                    lessonDialog.setCallback(DeleteDialog.this);
                    lessonDialog.show(getFragmentManager(), "LessonDialog");

                }
            });*/
            this.setButton(BUTTON_NEGATIVE, getString(R.string.no), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }

        private void startDeletingService(Lesson lesson, long newLessonId, long setId) {
            Intent intent = new Intent(getContext(), DeletingLessonService.class);
            intent.putExtra("lesson", lesson.getId());
            intent.putExtra("newLessonId", newLessonId);
            intent.putExtra("set", setId);
            getActivity().startService(intent);
        }

        @Override
        public void onLessonOk(Lesson lesson, boolean edit) {
            //TODO zobaczyć gdzie to jest wywoływane
            //deleteLesson(lesson, lesson.getId());
            startDeletingService(lesson, lesson.getId(), mSetId);
        }
    }
    //endregion

    //TODO to dokończyć kiedyć w przyszłości
    private class LessonSelectionAdapter extends ArrayAdapter {

        private List<Lesson> mItems;
        private Context mContext;
        private int mResource;

        public LessonSelectionAdapter(Context context, int resource, List<Lesson> data) {
            super(context, resource, data);
            mContext = context;
            mResource = resource;
            mItems = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            return rowView;
        }
    }
}
