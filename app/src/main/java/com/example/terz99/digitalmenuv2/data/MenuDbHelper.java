package com.example.terz99.digitalmenuv2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;

import static com.example.terz99.digitalmenuv2.data.MenuContract.*;



public class MenuDbHelper extends SQLiteOpenHelper{

    // The current version of the database
    private static final int DATABASE_VERSION = 41;

    /**
     * @return the database version
     */
    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    // The name of the database
    private static final String DATABASE_NAME = "menu.db";

    /**
     * Public constructor used to create a new database helper
     * (if already exists it just links it to the old one)
     * @param context is the Context from where the database helper is created
     */
    public MenuDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Overridden method which creates a table
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL command to create a database of the menu
        final String SQL_CREATE_TABLE = "CREATE TABLE " + MenuEntry.TABLE_NAME
                + "("
                + MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MenuEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
                + MenuEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MenuEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + MenuEntry.COLUMN_PRICE + " TEXT NOT NULL, "
                + MenuEntry.COLUMN_PHOTO_ID + " INTEGER NOT NULL);";

        // Execute the command via SQLiteDatabase
        db.execSQL(SQL_CREATE_TABLE);
    }


    // Overridden method which deletes (drops) a database if exists and creates a new upgraded
    // version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // SQL command to drop (delete) a table in the database
        final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + MenuEntry.TABLE_NAME;

        // Execute the command via SQLiteDatabase
        db.execSQL(SQL_DELETE_TABLE);

        // Create a new database (upgrade)
        onCreate(db);
    }
}
