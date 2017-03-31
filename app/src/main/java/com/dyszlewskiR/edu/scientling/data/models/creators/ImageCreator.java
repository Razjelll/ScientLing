package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Image;
import com.dyszlewskiR.edu.scientling.utils.BitmapUtils;

import static com.dyszlewskiR.edu.scientling.data.database.tables.ImagesTable.ImagesColumns.*;

/**
 * Created by Razjelll on 18.01.2017.
 */

public class ImageCreator implements IModelCreator{

    public Image createFromCursor(Cursor cursor) {
        Image image = new Image();
        int columnsCount = cursor.getColumnCount();
        for(int columnIndex = 0; columnIndex< columnsCount; columnIndex++){
            switch (cursor.getColumnName(columnIndex)){
                case WORD_FK:
                    image.setWordId(cursor.getLong(columnIndex)); break;
                case BITMAP:
                    image.setBitmap(getBitmapFromCursor(cursor, columnIndex)); break;
                case PATH:
                    if(!cursor.isNull(columnIndex)){
                        image.setPath(cursor.getString(columnIndex));
                    }
                    break;
            }
        }
        return image;
    }

    private Bitmap getBitmapFromCursor(Cursor cursor, int columnIndex){
        if(!cursor.isNull(columnIndex)){
            byte[] imageBytes = cursor.getBlob(columnIndex);
            return BitmapUtils.getBitmap(imageBytes);
        }
        return null;
    }
}
