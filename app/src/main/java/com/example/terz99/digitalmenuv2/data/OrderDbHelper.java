package com.example.terz99.digitalmenuv2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.terz99.digitalmenuv2.data.OrderContract.*;


public class OrderDbHelper extends SQLiteOpenHelper{

    // The name of the order database
    private static final String DATABASE_NAME = "order.db";
    // The database version
    private static int DATABASE_VERSION = 3;

    /**
     * Public constructor used to get a link from the database helper
     * @param context is the Context where the database helper is created in
     */
    public OrderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Overridden method which creates a table
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL command to create a database of the menu
        final String SQL_CREATE_TABLE = "CREATE TABLE " + OrderEntry.TABLE_NAME
                + "("
                + OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OrderEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + OrderEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + OrderEntry.COLUMN_PRICE + " TEXT NOT NULL, "
                + OrderEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + OrderEntry.COLUMN_PHOTO_ID + " INTEGER NOT NULL);";

        // Execute the command via SQLiteDatabase
        db.execSQL(SQL_CREATE_TABLE);
    }

    // Overridden method which deletes (drops) a database if exists and creates a new upgraded
    // version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // SQL command to drop (delete) a table in the database
        final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + OrderEntry.TABLE_NAME;

        // Execute the command via SQLiteDatabase
        db.execSQL(SQL_DELETE_TABLE);

        // Create a new database (upgrade)
        onCreate(db);
    }
}
