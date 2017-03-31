package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.activity.CategoryActivity;
import com.dyszlewskiR.edu.scientling.activity.CategorySelectionActivity;
import com.dyszlewskiR.edu.scientling.activity.HintsListActivity;
import com.dyszlewskiR.edu.scientling.activity.LessonActivity;
import com.dyszlewskiR.edu.scientling.activity.LessonSelectionActivity;
import com.dyszlewskiR.edu.scientling.activity.MainActivity;
import com.dyszlewskiR.edu.scientling.activity.SentencesListActivity;
import com.dyszlewskiR.edu.scientling.asyncTasks.SaveWordAsyncTask;
import com.dyszlewskiR.edu.scientling.data.models.params.SaveWordParams;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Category;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Definition;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Hint;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Lesson;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.PartOfSpeech;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Record;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Sentence;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Translation;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Word;
import com.dyszlewskiR.edu.scientling.dialogs.CategoryDialog;
import com.dyszlewskiR.edu.scientling.dialogs.DefinitionDialog;
import com.dyszlewskiR.edu.scientling.dialogs.DifficultDialog;
import com.dyszlewskiR.edu.scientling.dialogs.ImageDialog;
import com.dyszlewskiR.edu.scientling.dialogs.PartOfSpeechDialog;
import com.dyszlewskiR.edu.scientling.dialogs.RecordDialog;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.utils.Constants;
import com.dyszlewskiR.edu.scientling.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordEditFragment extends Fragment implements DefinitionDialog.Callback, PartOfSpeechDialog.Callback,
        DifficultDialog.Callback, CategoryDialog.Callback, RecordDialog.Callback, ImageDialog.Callback {

    private final String TAG = "WordEditFragment";
    private final int EMPTY_CATEGORY_STRING = R.string.lack;

    private final int SENTENCE_REQUEST = 7968;
    private final int HINTS_REQUEST = 4468;
    private final int LESSON_REQUEST = 5377;


    /**
     * Kontener przechowujący dodatkowe informacje o słóku, domyślnie jest ukryty, aby nie pokazywać
     * użytkownikowi zbyt wielu kontrolek. Jeśli będą mu potrzebne odkrywa je klikając na mMoreButton
     */
    private LinearLayout mAdvancedGroup;
    /**
     * Treść słóka
     */
    private EditText mWordEditText;
    /**
     * Tłumaczenie słówka. Jeśli słowko ma wiele tłumaczeń należy je odzielić znakiem określonym w
     * Constants.SEPARATOR
     */
    private EditText mTranslationEditText;
    /**
     * Przycisk do wyboru lekcji do jakiej zostanie przypisane słówko. Po naciśnięciu otwiera się
     * aktywność z listą lekcji.
     */
    private Button mLessonButton;
    /**
     * Przycisk definicji. Po naciśnięciu wyskakuje okno dialogowe w którym znajdują się kontrolki
     * do wpisania definicji i tłumaczenie definicji
     */
    private Button mDefinitionButton;
    /**
     * Przycik wybotu części mowy słówka. Po naciśnięciu wyskakuje lista części mowy. W tym wypadku
     * można zastosować listę, ponieważ liczba wartośći będzie zawsze stałą, ponieważ nie ma możliwościo
     * dodania części mowy
     */
    private Button mPartOfSpeechButton;
    /**
     * Przycisk wyboru trudności włóka. Po naciśnięciu wyskakuje lista poziomów trudności(wartości od 1
     * do 5 oraz brak poziomu). Można zastosować listę, ponieważ wartość poziomów trudności jest stała
     */
    private Button mDifficultButton;
    /**
     * Przycisk wyboru kategorii. Po naciśnięciu uruchamiana jest nowa aktywność (StartActivityForResult).
     * W nowej aktywności będzie możliwość wyboru istniejącej kategorii, a także stworzenie nowej.
     * Z tego powodu nie stosujemy tutaj listy. Implementacja dodawania nowej kategorii korzystając
     * z listy mogłoby być dość uciążliwe
     */
    private Button mCategoryButton;
    /**
     * Przycisk dodania przyskładowych zdań dla danego słówka. Po naciśnięciu uruchamiana jest akrtywność
     * która powinna zwracać w rezultacie listę obiektów Sentences.
     */
    private Button mSentencesButton;
    /**
     * Przycisk wybory podpowiedzi. Po naciśnięciu uruchamiana jest nowa aktywność (StartActivityForResult).
     * W nowej aktywności będzie możliwość wyboru istniejącej podpowiedzi, a także stworzenie nowej.
     */
    private Button mHintsButton;
    /**
     * Przycisk wyboru obrazka dla tworzonego slówka. Po naciśnięciu powinno pokazać się okno dialogowe
     * w którym będzie możliwość wybrania istniejącego obrazka lub skorzystanie z aparatu, aby
     * stworzyć nowe zdjęcie
     */
    private Button mImageButton;
    private ImageView mImageView;
    /**
     * Przycisk wyboru nagrania dla tworzonego słowka. Po naciśnięciu powinno pokazać się okno dialogowe
     * w którym będzie możliwośc wyboru istniejącego pliku dźwiękowego, a także skorzystania
     * z urządzenia nagrywajacego aby nagrać dźwięk
     */
    private Button mRecordButton;
    /**
     * Przycisk rozwijający i zwijający kontener z dodatkowymi kontrolkami. Domyślnie kontener jest ukryty.
     */
    private ImageButton mMoreButton;
    /**
     * Przycisk zapisujący tworzone słówko. Po naciśnięciu powinna nastąpić walidacja(słówko i tłumaczenie)
     * a nastepnie powinien rozpocząć się proces zapisu
     */
    private Button mSaveButton;

    /**
     * Zestaw do którego będziemy dodawać słówko
     */
    private VocabularySet mSet;
    private long mCurrentSetId;
    private Word mWord;
    private Uri mImageUri;
    private Uri mRecordUri;

    /**Zmienna określająca czy zakończenie aktywności nastąpiło ze względu zmiany konfiguracji(np obrotu ekranu)*/
    private boolean mIsStateChange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWord = new Word();
        mCurrentSetId = ((LingApplication)getActivity().getApplication()).getCurrentSetId();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy");
        if(!mIsStateChange){
            Log.d(TAG, "clearCache");
            ImageDialog.clearCache(getContext());
            RecordDialog.clearCache(getContext());
        }
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_edit, container, false);
        setupControls(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        mIsStateChange = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mIsStateChange = false;
    }

    private void setupControls(View view) {
        mAdvancedGroup = (LinearLayout) view.findViewById(R.id.advanced_edit_group);
        mWordEditText = (EditText) view.findViewById(R.id.word_edit_text);
        mTranslationEditText = (EditText) view.findViewById(R.id.translation_edit_text);
        mLessonButton = (Button)view.findViewById(R.id.lesson_button);
        mDefinitionButton = (Button) view.findViewById(R.id.definition_button);
        mPartOfSpeechButton = (Button) view.findViewById(R.id.part_of_speech_button);
        mDifficultButton = (Button) view.findViewById(R.id.difficult_button);
        mCategoryButton = (Button) view.findViewById(R.id.category_button);
        mSentencesButton = (Button) view.findViewById(R.id.sentences_button);
        mHintsButton = (Button) view.findViewById(R.id.hints_button);
        mImageButton = (Button) view.findViewById(R.id.image_button);
        mImageView = (ImageView)view.findViewById(R.id.image_image_view);
        mRecordButton = (Button) view.findViewById(R.id.record_button);
        mMoreButton = (ImageButton) view.findViewById(R.id.more_image_button);
        mSaveButton = (Button) view.findViewById(R.id.save_button);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListeners();
    }

    private void setListeners() {
        mLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "LessonButton Click");
                startLessonActivity();
            }
        });
        mDefinitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DefinitionButton Click");
                DefinitionDialog dialog = new DefinitionDialog();
                dialog.setCallback(WordEditFragment.this);
                dialog.setDefinition(mWord.getDefinition());
                dialog.show(getFragmentManager(), "DefinitionDialog");
            }
        });
        mPartOfSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PartOfSpeechButton Click");
                PartOfSpeechDialog dialog = new PartOfSpeechDialog();
                dialog.setCallback(WordEditFragment.this);
                dialog.show(getFragmentManager(), "PartOfSpeechDialog");
            }
        });
        mDifficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DifficultButton Click");
                DifficultDialog dialog = new DifficultDialog();
                dialog.setCallback(WordEditFragment.this);
                dialog.show(getFragmentManager(), "DifficultDialog");
            }
        });
        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CategoryButton Click");
                CategoryDialog dialog = new CategoryDialog();
                dialog.setCallback(WordEditFragment.this);
                dialog.show(getFragmentManager(), "CateogryDialog");

            }
        });
        mSentencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SentencesButton Click");
                startSentenceActivity();
            }
        });
        mHintsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "HintsButton Click");
                startHintsActivity();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ImageButton Click");
                ImageDialog dialog = new ImageDialog();
                dialog.setCallback(WordEditFragment.this);
                if(mImageUri != null){
                    dialog.setImageUri(mImageUri);
                }
                dialog.show(getFragmentManager(), "ImageDialog");
            }
        });
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "RecordButton Click");
                RecordDialog dialog = new RecordDialog();
                dialog.setCallback(WordEditFragment.this);
                if(mRecordUri != null){
                    dialog.setRecordUri(mRecordUri);
                }
                //TODO setRecord
                dialog.show(getFragmentManager(), "RecordDialog");

            }
        });
        mMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "MoreButton Click");
                changeAdvancedGroupVisibility();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SaveButton Click");
                saveWord();
            }
        });
    }

    private void saveWord(){
        if(validate()){
            DataManager dataManager = ((LingApplication)getActivity().getApplication()).getDataManager();
            //TODO zobaczyć czy da radęw inny sposób pobrać zestaw
            long currentSetId = ((LingApplication)getActivity().getApplication()).getCurrentSetId();
            VocabularySet currentSet = dataManager.getSetById(currentSetId);
            SaveWordAsyncTask task = new SaveWordAsyncTask(dataManager, getActivity());
            SaveWordParams params = new SaveWordParams();
            params.setEdit(false);
            params.setWord(getWord());
            params.setImageUri(mImageUri);
            params.setRecordUri(mRecordUri);
            params.setSet(currentSet);
            task.execute(params);
        }
    }

    private Word getWord(){
        Word word = mWord;
        word.setContent(mWordEditText.getText().toString());
        word.setTranslations(getTranslations());
        word.setOwn(true);
        //TODO ustawić lekcję
        //TODO reszta powinna być ustawiana podczasp wybierania, sprawdzić czy to dobrze działa
        return word;
    }

    private ArrayList<Translation> getTranslations(){
        String[] translations = mTranslationEditText.getText().toString().split(Constants.TRANSLATION_SEPARATOR);
        Translation translation;
        ArrayList<Translation> translationsList = new ArrayList<>();
        for(String s : translations){
            translation = new Translation();
            translation.setContent(s);
            translationsList.add(translation);
        }
        return translationsList;
    }

    private boolean validate(){
        boolean correct = true;
        if(mWordEditText.getText().toString().equals("")){
            mWordEditText.setError(getString(R.string.field_not_empty));
            correct = false;
        }
        if(mTranslationEditText.getText().toString().equals("")){
            mTranslationEditText.setError(getString(R.string.field_not_empty));
            correct = false;
        }
        return correct;
    }

    private void startSentenceActivity() {
        Intent intent = new Intent(getContext(), SentencesListActivity.class);
        if (mWord.getSentences() != null) {
            intent.putParcelableArrayListExtra("list", mWord.getSentences());
        }
        startActivityForResult(intent, SENTENCE_REQUEST);
    }

    private void startHintsActivity() {
        Intent intent = new Intent(getContext(), HintsListActivity.class);
        if (mWord.getHints() != null) {
            intent.putParcelableArrayListExtra("list", mWord.getHints());
        }
        startActivityForResult(intent, HINTS_REQUEST);
    }

    private void startLessonActivity(){
        Intent intent = new Intent(getContext(), LessonSelectionActivity.class);
        intent.putExtra("set", mCurrentSetId);
        startActivityForResult(intent, LESSON_REQUEST);
    }

    private void changeAdvancedGroupVisibility() {
        if (mAdvancedGroup.getVisibility() == View.VISIBLE) {
            mAdvancedGroup.setVisibility(View.GONE);
        } else {
            mAdvancedGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDefinitionDialogOk(Definition definition) {
        Log.d(TAG, "onDefinitionDialogOk");
        if (definition != null) {
            mDefinitionButton.setText(definition.getContent() + "\n" + definition.getTranslation());
        } else {
            mDefinitionButton.setText("");
            mDefinitionButton.setHint(getString(R.string.definition));
        }
        mWord.setDefinition(definition);
    }

    @Override
    public void onPartOfSpeechOk(PartOfSpeech partOfSpeech) {
        if (partOfSpeech != null) {
            mWord.setPartsOfSpeech(partOfSpeech);
            mPartOfSpeechButton.setText(ResourceUtils.getString(partOfSpeech.getName(), getContext()));
        } else {
            mWord.setPartsOfSpeech(null);
            mPartOfSpeechButton.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SENTENCE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Sentence> sentences = data.getParcelableArrayListExtra("result");
                mWord.setSentences(sentences);
                setSentenceText(sentences.size());
            }
        }
        if (requestCode == HINTS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Hint> hints = data.getParcelableArrayListExtra("result");
                mWord.setHints(hints);
                setHintText(hints.size());
            }
        }

        if(requestCode == LESSON_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Lesson lesson = data.getParcelableExtra("result");
                mWord.setLessonId(lesson.getId());
                mLessonButton.setText(lesson.getName());
            }
        }
    }

    private void setSentenceText(int elementsCount) {
        String text = "";
        if (elementsCount > 0) {
            text = getString(R.string.sentence) + " (" + elementsCount + " )";
        }
        mSentencesButton.setText(text);
    }

    private void setHintText(int elementsCount) {
        String text = "";
        if (elementsCount > 0) {
            text = getString(R.string.hint) + " (" + elementsCount + ")";
        }
        mHintsButton.setText(text);
    }

    @Override
    public void onDifficultOk(int difficult) {
        if (difficult != 0) {
            mWord.setDifficult((byte) difficult);
            mDifficultButton.setText(String.valueOf(difficult));
        } else {
            //TODO zobaczyć jaka wartość odpowiada braku poziomu trudności
            mWord.setDifficult((byte) -1);
            mDifficultButton.setText("");
        }
    }

    /**
     * Metoda zwrotna dialogu kategorii. Do dialogu została sztucznie wprowadzona pozycja "brak"
     * z powodu łatwiejszego filtrowania. Pozycja ta ma id -1. Jeżeli zostanie zwrócona ta pozycja
     * ustawiamy kategorię na null i usuwamy tekst, aby pokazał się ustawiony hint
     *
     * @param category
     */
    @Override
    public void onCategoryOk(Category category) {
        if (category.getId() > 0) {
            mWord.setCategory(category);
            mCategoryButton.setText(ResourceUtils.getString(category.getName(), getContext()));
        } else {
            mWord.setCategory(null);
            mCategoryButton.setText("");
        }
    }

    @Override
    public void onRecordOk(Uri recordUri) {
        /*mRecordPath = recordPath;
        if (recordPath != null) {
            mRecordButton.setText(getString(R.string.record));
        } else {
            mRecordButton.setText("");
        }*/
        mRecordUri = recordUri;
        if(recordUri != null){
            mRecordButton.setText(getString(R.string.record));
        }else {
            mRecordButton.setText("");
        }
    }

    @Override
    public void onImageOk(Uri imageUri) {
        mImageUri = imageUri;
        if(imageUri != null){
            mImageButton.setText(getString(R.string.image));
        } else {
            mImageButton.setText("");
        }
    }
}

