package com.example.terz99.digitalmenuv2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;



public class MenuContract {

    // String which represents the authority of the content URI
    public static final String CONTENT_AUTHORITY = "com.example.terz99.digitalmenuv2.data";
    // URI which represents the scheme and the authority of the content URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // The path of the table
    public static final String PATH_MENU = "menu";

    public static class MenuEntry implements BaseColumns{

        // This string helps the getType() method in the content provider to return the type of the
        // whole table
        public static final String CONTENT_LIST_TYPE  = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;

        // This string helps the getType() method in the content provider to return the type of
        // a single row
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU + "/#";

        // The main URI of the database content
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU)
                .build();

        // The name of the table. This name has to be the same as the path of the menu (PATH_MENU)
        public static final String TABLE_NAME = "menu";


        // The name of the category id column
        public static final String COLUMN_CATEGORY_ID = "category_id";
        // The name of the name column
        public static final String COLUMN_NAME = "name";
        // The name of the description column
        public static final String COLUMN_DESCRIPTION = "description";
        // The name of the price column
        public static final String COLUMN_PRICE = "price";
        // The name of the photo column
        public static final String COLUMN_PHOTO_ID = "photo_id";
    }
}
