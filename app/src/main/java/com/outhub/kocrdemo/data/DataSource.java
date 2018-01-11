package com.outhub.kocrdemo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SummerRC on 17/11/9.
 * description: Main entry point for accessing data
 */

public abstract class DataSource {

    private boolean mCacheIsDirty = false;
    private List<DataObserver> mObservers = new ArrayList<>();

    public abstract Object getData();

    public void refreshData() {
        mCacheIsDirty = true;
        notifyObserver();
    }

    public void addObserver(DataObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeObserver(DataObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyObserver() {
        for (DataObserver observer : mObservers) {
            observer.onDataChanged();
        }
    }

    public interface DataObserver {
        void onDataChanged();
    }
}
