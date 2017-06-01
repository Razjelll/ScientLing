package com.dyszlewskiR.edu.scientling.data.models.creators;

import android.database.Cursor;

import com.dyszlewskiR.edu.scientling.data.models.models.SetDownloadInfo;


public class SetDownloadInfoCreator {
    public static SetDownloadInfo createFromCursor(Cursor cursor){
        if(cursor!=null){
            SetDownloadInfo info = new SetDownloadInfo();
            for(int i=0; i<cursor.getColumnCount(); i++){
                switch (cursor.getColumnName(i)){
                    /*case SetsTable.SetsColumns.IMAGES_DOWNLOADED:
                        info.setImagesDownloaded(cursor.getInt(i));
                    case SetsTable.SetsColumns.RECORDS_DOWNLOADED:
                        info.setRecordsDownloaded(cursor.getInt(i));*/
                }
            }
            return info;
        }
        return null;
    }
}
