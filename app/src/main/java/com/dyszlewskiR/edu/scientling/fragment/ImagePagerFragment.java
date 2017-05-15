package com.dyszlewskiR.edu.scientling.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.data.file.FileSystem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment {

    private String mFileName;
    private String mCatalogName;
    private ImageView mImageView;


    public ImagePagerFragment() {
        // Required empty public constructor
    }

    public void setImage(String filename, String catalogName) {
        mFileName = filename;
        mCatalogName = catalogName;
        setImage();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mFileName = bundle.getString("file");
        mCatalogName = bundle.getString("catalog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_pager, container, false);
        mImageView = (ImageView) view.findViewById(R.id.image_image_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setImage();
    }

    private void setImage() {
        Uri uri = FileSystem.getImageUri(mFileName, mCatalogName, getContext());
        mImageView.setImageURI(uri);
    }

}
