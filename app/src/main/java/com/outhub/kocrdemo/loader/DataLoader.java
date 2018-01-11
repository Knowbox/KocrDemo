package com.outhub.kocrdemo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.outhub.kocrdemo.data.DataSource;
import com.outhub.kocrdemo.data.OcrAnalyzeData;


/**
 * Created by SummerRC on 18/1/9.
 * description:
 */

public class DataLoader extends AsyncTaskLoader<Object> implements OcrAnalyzeData.DataObserver {
    private DataSource mDataSource;

    public DataLoader(Context context, DataSource dataSource) {
        super(context);
        mDataSource = dataSource;
        mDataSource.addObserver(this);
    }

    @Override
    public Object loadInBackground() {
        return mDataSource.getData();
    }

    @Override
    public void deliverResult(Object data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public void onDataChanged() {
        forceLoad();
    }
}
