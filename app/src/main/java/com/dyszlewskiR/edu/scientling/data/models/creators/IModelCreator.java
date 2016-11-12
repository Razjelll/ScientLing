package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

/**
 * Created by Razjelll on 12.11.2016.
 */

public interface IModelCreator<T> {

    public T createFromCursor(Cursor cursor);
}
