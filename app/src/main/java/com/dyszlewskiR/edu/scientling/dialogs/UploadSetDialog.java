package com.dyszlewskiR.edu.scientling.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.services.net.ServerSet;

public class UploadSetDialog extends Dialog {

    private final int CONTENT_RESOURCE = R.layout.dialog_upload_set;

    private TextView mNameTextView;
    private EditText mDescriptionEditText;
    private Button mUploadButton;
    private boolean mUploadAll;
    private VocabularySet mSet;

    private boolean mEditDescription;

    public UploadSetDialog(@NonNull Context context, VocabularySet set, boolean uploadAll) {
        super(context);

        mUploadAll = uploadAll;
        mSet = set;
        setTitle(context.getString(R.string.upload));
        setContentView(CONTENT_RESOURCE);
        setupControls();
        setListeners();
        setValues();
    }

    public void setDescription(String description){
        mDescriptionEditText.setText(description);
        mEditDescription = true;
    }

    private void setupControls() {
        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mDescriptionEditText = (EditText) findViewById(R.id.description_edit_text);
        mUploadButton = (Button) findViewById(R.id.upload_button);
    }

    private void setListeners() {
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditDescription){

                } else {
                    if (mUploadAll) {
                        ServerSet.upload(mSet, mDescriptionEditText.getText().toString(), true, true, true, getContext());
                    } else {
                        ServerSet.upload(mSet, mDescriptionEditText.getText().toString(), true, false, false, getContext());
                    }
                }
                dismiss();
            }
        });
    }

    private void setValues() {
        mNameTextView.setText(mSet.getName());
    }
}
