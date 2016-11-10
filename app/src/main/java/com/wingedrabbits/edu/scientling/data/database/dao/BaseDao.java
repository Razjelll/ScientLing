package com.wingedrabbits.edu.scientling.data.database.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public abstract class BaseDao<T> {

    protected String mSelection;
    protected String[] mSelectionArgs;
    protected boolean mDistinct;
    protected String mGroupBy;
    protected String mHaving;
    protected String mOrderBy;
    protected String mLimit;

    protected SQLiteDatabase mDb;
    protected SQLiteStatement mInsertStatement;
    protected String[] mTableColumns;

    public abstract long save(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
    public abstract T get(long id);
    public abstract List<T> getAll();

    public BaseDao(SQLiteDatabase db)
    {
        mDb = db;
        mSelection = null;
        mSelectionArgs = null;
        mDistinct = false;
        mGroupBy = null;
        mHaving = null;
        mOrderBy = null;
        mLimit = null;
    }

    public boolean isDistinct() {
        return mDistinct;
    }

    public void setDistinct(boolean mDistinct) {
        this.mDistinct = mDistinct;
    }

    public String getGroupBy() {
        return mGroupBy;
    }

    public void setGroupBy(String mGroupBy) {
        this.mGroupBy = mGroupBy;
    }

    public String getHaving() {
        return mHaving;
    }

    public void setHaving(String mHaving) {
        this.mHaving = mHaving;
    }

    public String getOrderBy() {
        return mOrderBy;
    }

    public void setOrderBy(String mOrderBy) {
        this.mOrderBy = mOrderBy;
    }

    public String getLimit() {
        return mLimit;
    }

    public void setLimit(String mLimit) {
        this.mLimit = mLimit;
    }

    public String getmSelection() {
        return mSelection;
    }

    public void setmSelection(String mSelection) {
        this.mSelection = mSelection;
    }

    public String[] getmSelectionArgs() {
        return mSelectionArgs;
    }

    public void setmSelectionArgs(String[] mSelectionArgs) {
        this.mSelectionArgs = mSelectionArgs;
    }
}
