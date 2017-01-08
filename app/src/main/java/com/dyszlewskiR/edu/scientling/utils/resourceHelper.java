package com.dyszlewskiR.edu.scientling.utils;

import android.content.Context;

/**
 * Created by Razjelll on 04.12.2016.
 */

public class ResourceHelper {
    public static String getString(String resourceName, Context context) {
        int resource = context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
        return context.getResources().getString(resource);
    }

}
