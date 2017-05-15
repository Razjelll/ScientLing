package com.dyszlewskiR.edu.scientling.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;

import com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetDownloadInfoCreator;
import com.dyszlewskiR.edu.scientling.data.models.creators.SetListItemCreator;
import com.dyszlewskiR.edu.scientling.data.models.models.SetDownloadInfo;
import com.dyszlewskiR.edu.scientling.data.models.models.SetListItem;
import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.SetsColumns;
import static com.dyszlewskiR.edu.scientling.data.database.tables.SetsTable.TABLE_NAME;

/**
 * Created by Razjelll on 08.11.2016.
 */

public class SetDao extends BaseDao<VocabularySet> {


    private final String INSERT_STATEMENT =
            "INSERT INTO " + SetsTable.TABLE_NAME + "("
                    + SetsColumns.NAME + ", " + SetsColumns.LANGUAGE_L2_FK
                    + ", " + SetsColumns.LANGUAGE_L1_FK
                    + ", " + SetsColumns.CATALOG +
                    ", " + SetsColumns.GLOBAL_ID
                    +", " + SetsColumns.UPLOADED + ", " + SetsColumns.IMAGES_DOWNLOADED
                    +", " + SetsColumns.RECORDS_DOWNLOADED+ ") VALUES (?,?,?,?,?,?,?,?)";
    private final String WHERE_ID = SetsColumns.ID + "= ?";

    public SetDao(SQLiteDatabase db) {
        super(db);
        mInsertStatement = mDb.compileStatement(INSERT_STATEMENT);
        mTableColumns = SetsTable.getColumns();
    }

    @Override
    public long save(VocabularySet entity) {
        mInsertStatement.clearBindings();
        mInsertStatement.bindString(SetsColumns.NAME_POSITION, entity.getName());
        mInsertStatement.bindLong(SetsColumns.LANGUAGE_L2_FK_POSITION, entity.getLanguageL2().getId());
        mInsertStatement.bindLong(SetsColumns.LANGUAGE_L1_FK_POSITION, entity.getLanguageL1().getId());
        mInsertStatement.bindString(SetsColumns.CATALOG_POSITION, entity.getCatalog());
        if(entity.getGlobalId() != null){
            mInsertStatement.bindLong(SetsColumns.GLOBAL_ID_POSITION, entity.getGlobalId());
        } else {
            mInsertStatement.bindNull(SetsColumns.GLOBAL_ID_POSITION);
        }
        if(entity.getUploaded()!=null){
            mInsertStatement.bindLong(SetsColumns.UPLOADED_POSITION, entity.getUploaded());
        } else {
            mInsertStatement.bindNull(SetsColumns.UPLOADED_POSITION);
        }
        if(entity.getImagesDownloaded() != null){
            mInsertStatement.bindLong(SetsColumns.IMAGES_DOWNLOADED_POSITION, entity.getImagesDownloaded());
        } else {
            mInsertStatement.bindNull(SetsColumns.IMAGES_DOWNLOADED_POSITION);
        }
        if(entity.getRecordsDownloaded()!=null){
            mInsertStatement.bindLong(SetsColumns.RECORDS_DOWNLOADED_POSITION, entity.getRecordsDownloaded());
        } else {
            mInsertStatement.bindNull(SetsColumns.RECORDS_DOWNLOADED_POSITION);
        }
        return mInsertStatement.executeInsert();
    }

    @Override
    public void update(VocabularySet entity) {
        final ContentValues values = new ContentValues();
        values.put(SetsColumns.NAME, entity.getName());
        if (entity.getLanguageL2() != null) {
            values.put(SetsColumns.LANGUAGE_L2_FK, entity.getLanguageL2().getId());
        } else {
            values.putNull(SetsColumns.LANGUAGE_L2_FK);
        }
        if (entity.getLanguageL1() != null) {
            values.put(SetsColumns.LANGUAGE_L1_FK, entity.getLanguageL1().getId());
        } else {
            values.putNull(SetsColumns.LANGUAGE_L1_FK);
        }
        values.put(SetsColumns.CATALOG, entity.getCatalog());
        if(entity.getGlobalId() !=null){
            values.put(SetsColumns.GLOBAL_ID, entity.getGlobalId());
        } else{
            values.putNull(SetsColumns.GLOBAL_ID);
        }
        if(entity.isUploaded()!=null){
            values.put(SetsColumns.UPLOADED, entity.isUploaded());
        } else {
            values.putNull(SetsColumns.UPLOADED);
        }
        if(entity.getImagesDownloaded() != null){
            values.put(SetsColumns.IMAGES_DOWNLOADED, entity.isImagesDownloaded());
        } else {
            values.putNull(SetsColumns.IMAGES_DOWNLOADED);
        }
        if(entity.getRecordsDownloaded()!=null){
            values.put(SetsColumns.RECORDS_DOWNLOADED, entity.isRecordsDownloaded());
        } else {
            values.putNull(SetsColumns.RECORDS_DOWNLOADED);
        }


        String[] whereArguments = new String[]{String.valueOf(entity.getId())};
        mDb.update(TABLE_NAME, values, WHERE_ID, whereArguments);
    }

    public void update(String column, boolean value, String selection, String[] selectionArgs){
       final ContentValues values = new ContentValues();
        values.put(column, value);
        mDb.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void update(String column, Long value, String selection, String[] selectionArgs){
        final ContentValues values = new ContentValues();
        values.put(column, value);
        mDb.update(TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(VocabularySet entity) {
        long id = entity.getId();
        if (id > 0) {
            String[] whereArguments = new String[]{String.valueOf(id)};
            return mDb.delete(TABLE_NAME, WHERE_ID, whereArguments);
        }
        return 0;
    }

    @Override
    public VocabularySet get(long id) {
        VocabularySet set = null;
        String[] whereArguments = new String[]{String.valueOf(id)};
        Cursor cursor = mDb.query(TABLE_NAME, mTableColumns, WHERE_ID, whereArguments,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            set = SetCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return set;
    }

    public VocabularySet get(String[] columns, String selection, String[] selectionArgs){
        VocabularySet set = null;
        Cursor cursor = mDb.query(TABLE_NAME, columns, selection, selectionArgs,null,null,null,null);
        if(cursor.moveToFirst()){
            set = SetCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return set;
    }


    @Override
    public List<VocabularySet> getAll(boolean distinct, String[] columns, String selection, String[] selectionArgs,
                                      String groupBy, String having, String orderBy, String limit) {
        List<VocabularySet> setsList = new ArrayList<>();
        Cursor cursor = mDb.query(distinct, TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            VocabularySet set = null;
            do {
                set = SetCreator.createFromCursor(cursor);
                if (set != null) {
                    setsList.add(set);
                }
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return setsList;
    }

    public List<SetListItem> getAllListItem(String[]columns, String selection, String[] selectionArgs,
                                            String groupBy, String having, String orderBy, String limit){
        List<SetListItem> setsList = new ArrayList<>();
        Cursor cursor = mDb.query(TABLE_NAME, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        if(cursor.moveToFirst()){
            SetListItem item = null;
            do{
                item = SetListItemCreator.createFromCursor(cursor);
                if(item != null){
                    setsList.add(item);
                }
            } while(cursor.moveToNext());
        }
        closeCursor(cursor);
        return setsList;
    }

    public SetDownloadInfo getSetDownloadInfo(String[] columns, String selection, String[] selectionArgs){
        Cursor cursor = mDb.query(TABLE_NAME, columns, selection,selectionArgs,null,null,null,null);
        SetDownloadInfo info = null;
        if(cursor.moveToFirst()){
            info= SetDownloadInfoCreator.createFromCursor(cursor);
        }
        closeCursor(cursor);
        return info;
    }

}
