package com.example.terz99.digitalmenuv2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.terz99.digitalmenuv2.data.MenuContract;

import java.util.ArrayList;

class FetchMenuDataTask extends AsyncTaskLoader<ArrayList<Item>> {

    private Context sContext;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public FetchMenuDataTask(Context context) {
        super(context);
        sContext = context;
    }

    @Override
    public ArrayList<Item> loadInBackground() {

        String[] projection = {
                MenuContract.MenuEntry.COLUMN_NAME,
                MenuContract.MenuEntry.COLUMN_DESCRIPTION,
                MenuContract.MenuEntry.COLUMN_PRICE,
                MenuContract.MenuEntry.COLUMN_PHOTO_ID,
                MenuContract.MenuEntry.COLUMN_CATEGORY_ID,
        };

        Cursor cursor = sContext.getContentResolver().query(MenuContract.MenuEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        ArrayList<Item> data = new ArrayList<Item>();

        while(cursor != null && cursor.moveToNext()){

            String name = cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_DESCRIPTION));
            double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_PRICE)));
            int imageId = cursor.getInt(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_PHOTO_ID));
            int categoryId = cursor.getInt(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_CATEGORY_ID));

            data.add(new Item(name, price, description, imageId, categoryId));
        }

        if (cursor != null) {
            cursor.close();
        }

        return data;
    }
}
