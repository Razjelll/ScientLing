package com.dyszlewskiR.edu.scientling.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dyszlewskiR.edu.scientling.LingApplication;
import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.adapters.LanguagesAdapter;
import com.dyszlewskiR.edu.scientling.services.DataManager;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.Language;
import com.dyszlewskiR.edu.scientling.data.models.tableModels.VocabularySet;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetFragment extends Fragment {

    private DataManager mDataManager;

    private EditText mNameEditText;
    private Spinner mL2Spinner;
    private Spinner mL1Spinner;
    private Button mSaveButton;

    private List<Language> mLanguages;

    public SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        mDataManager = ((LingApplication) getActivity().getApplication()).getDataManager();

        mNameEditText = (EditText) view.findViewById(R.id.set_name_edit_text);
        mL2Spinner = (Spinner) view.findViewById(R.id.l2_spinner);
        mL1Spinner = (Spinner) view.findViewById(R.id.l1_spinner);
        mSaveButton = (Button) view.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VocabularySet set = prepareSet();
                boolean result = saveSet(set);
                if (result) {
                    setResultAndFinish(set);
                } else {
                    showAlert();
                }
            }
        });
        mLanguages = mDataManager.getLanguages();


        return view;
    }

    private VocabularySet prepareSet() {
        VocabularySet set = new VocabularySet();
        set.setName(String.valueOf(mNameEditText.getText()));
        set.setLanguageL2(mLanguages.get(mL2Spinner.getSelectedItemPosition()));
        set.setLanguageL1(mLanguages.get(mL1Spinner.getSelectedItemPosition()));
        return set;
    }

    private boolean saveSet(VocabularySet set) {
        long id = mDataManager.saveSet(set);
        if (id > 0) {
            set.setId(id);
            return true;
        }
        return false;
    }

    private void setResultAndFinish(VocabularySet set) {
        Intent data = new Intent();
        data.putExtra("result", set);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    private void showAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage(getResources().getString(R.string.set_save_failed));
        alertBuilder.setNeutralButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        LanguagesAdapter adapter = new LanguagesAdapter(getActivity(), R.layout.item_language, mLanguages);
        mL1Spinner.setAdapter(adapter);
        mL2Spinner.setAdapter(adapter);
    }


}
