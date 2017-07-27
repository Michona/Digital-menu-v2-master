package com.example.terz99.digitalmenuv2;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.terz99.digitalmenuv2.data.MenuContract;

import java.util.ArrayList;



class UploadDataTask extends AsyncTaskLoader<Integer> {

    private static final String TAG = UploadDataTask.class.getSimpleName();
    private Context mContext;

    private ArrayList<Item> mItems;

    public UploadDataTask(Context baseContext, ArrayList<Item> items) {
        super(baseContext);
        mContext = baseContext;
        mItems = items;
    }



    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {

        for(int i = 0; i < mItems.size(); i++){

            Item item = mItems.get(i);

            ContentValues cv = new ContentValues();

            cv.put(MenuContract.MenuEntry.COLUMN_NAME, item.getmName());
            cv.put(MenuContract.MenuEntry.COLUMN_CATEGORY_ID, item.getmCategoryId());
            cv.put(MenuContract.MenuEntry.COLUMN_PHOTO_ID, item.getmImageId());
            cv.put(MenuContract.MenuEntry.COLUMN_DESCRIPTION, item.getmDescription());
            cv.put(MenuContract.MenuEntry.COLUMN_PRICE, String.valueOf(item.getmPrice()));

            mContext.getContentResolver().insert(MenuContract.MenuEntry.CONTENT_URI, cv);
            Log.i(TAG, "Uploaded");
        }

        return mItems.size();
    }
}
