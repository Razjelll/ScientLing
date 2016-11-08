package com.wingedrabbits.edu.scientling.data.database.dao;

import java.util.List;

/**
 * Created by Razjelll on 08.11.2016.
 */

public abstract class BaseDao<T> {

    protected boolean mDistinct;
    protected String mGroupBy;
    protected String mHaving;
    protected String mOrderBy;
    protected String mLimit;

    public abstract long save(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
    public abstract T get(long id);
    public abstract List<T> getAll();

    public BaseDao()
    {
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
}
