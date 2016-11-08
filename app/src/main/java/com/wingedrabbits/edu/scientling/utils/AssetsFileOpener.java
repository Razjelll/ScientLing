package com.wingedrabbits.edu.scientling.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Razjelll on 07.11.2016.
 */

public class AssetsFileOpener extends AndroidFileOpener {

    private Context mContext;
    public AssetsFileOpener(Context context)
    {
        mContext = context;
    }

    @Override
    public InputStream getStream(String fileName) throws IOException {
        return mContext.getAssets().open(fileName);
    }
}
