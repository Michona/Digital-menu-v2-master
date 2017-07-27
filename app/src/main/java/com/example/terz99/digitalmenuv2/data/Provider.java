package com.example.terz99.digitalmenuv2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;



public class Provider extends ContentProvider{

    // Log tag
    private static final String TAG = Provider.class.getSimpleName();

    // URI matcher
    private UriMatcher sUriMatcher;

    // Constants for the URI matcher
    private final static int MENU = 100;
    private final static int MENU_ID = 101;
    private final static int ORDER = 200;
    private final static int ORDER_ID = 201;
    private final static int BILL = 300;
    private final static int BILL_ID = 301;

    // Database helpers for every database
    private MenuDbHelper menuDbHelper;
    private OrderDbHelper orderDbHelper;
    private BillDbHelper billDbHelper;

    // Overriden method which instantiates the content provider
    @Override
    public boolean onCreate() {

        // Initialize uri matcher to NO_MATCH
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add URI matching code for the whole table of menu table
        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU, MENU);
        // Add URI matching code for the whole table of order table
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_ORDER, ORDER);
        // Add URI matching code for the whole table of bill table
        sUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_BILL, BILL);
        // Add URI matching code for a single row in the menu table
        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU + "/#", MENU_ID);
        // Add URI matching code for a single row in the order table
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_ORDER + "/#", ORDER_ID);
        // Add URI matching code for a single row in the bill table
        sUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_BILL + "/#", BILL_ID);

        // Initialize the databases
        menuDbHelper = new MenuDbHelper(getContext());
        orderDbHelper = new OrderDbHelper(getContext());
        billDbHelper = new BillDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Database object
        SQLiteDatabase db;

        // Match the uri using sUriMatcher
        int match = sUriMatcher.match(uri);

        // Returning argument of the function
        Cursor retCursor;

        // Check the path matcher
        switch (match){

            case MENU_ID:
            case MENU:

                // Get readable database
                db = menuDbHelper.getReadableDatabase();

                // Send a query to the database
                retCursor = db.query(MenuContract.MenuEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case ORDER:
            case ORDER_ID:

                // Get readable database
                db = orderDbHelper.getReadableDatabase();

                // Send a query to the database
                retCursor = db.query(OrderContract.OrderEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case BILL:
            case BILL_ID:

                // Get readable database
                db = billDbHelper.getReadableDatabase();

                // Send a query to the database
                retCursor = db.query(BillContract.BillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default: // Otherwise, thorw an exception stating that this is an unknown uri
                throw new IllegalArgumentException("Unknown uri " + uri.toString());
        }

        // Set notification to the uri and content resolver
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the data
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        // Match the provided URI with the originals
        int match = sUriMatcher.match(uri);

        switch (match){

            case MENU:
                return MenuContract.MenuEntry.CONTENT_LIST_TYPE;
            case MENU_ID:
                return MenuContract.MenuEntry.CONTENT_ITEM_TYPE;
            case ORDER:
                return OrderContract.OrderEntry.CONTENT_LIST_TYPE;
            case ORDER_ID:
                return OrderContract.OrderEntry.CONTENT_ITEM_TYPE;
            case BILL:
                return BillContract.BillEntry.CONTENT_LIST_TYPE;
            case BILL_ID:
                return BillContract.BillEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // Database
        SQLiteDatabase db;

        // Match the provided URI with the original URIs
        final int match = sUriMatcher.match(uri);

        // The id of the newly inserted row
        long id;
        switch (match){

            case MENU:

                // Get writable database
                db = menuDbHelper.getWritableDatabase();

                // Insert data into the database and get the new row's id
                id = db.insert(MenuContract.MenuEntry.TABLE_NAME, null, values);

                // If the id is -1 then the insertion failed
                if(id == -1){
                    Log.i(TAG, "Failed to insert element in menu database: " + uri.toString());
                    return null;
                }

                // Otherwise notify the changes
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the uri of the new row
                return ContentUris.withAppendedId(uri, id);

            case ORDER:

                // Get writable database
                db = orderDbHelper.getWritableDatabase();

                // Insert data into the database and get the new row's id
                id = db.insert(OrderContract.OrderEntry.TABLE_NAME, null, values);

                // If the id is -1 then the insertion failed
                if(id == -1){
                    Log.i(TAG, "Failed to insert element in order database: " + uri.toString());
                    return null;
                }

                // Otherwise notify the changes
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the uri of the new row
                return ContentUris.withAppendedId(uri, id);

            case BILL:

                // Get writable database
                db = billDbHelper.getWritableDatabase();

                // Insert data into the database and get the new row's id
                id = db.insert(BillContract.BillEntry.TABLE_NAME, null, values);

                // If the id is -1 then the insertion failed
                if(id == -1){
                    Log.i(TAG, "Failed to insert element in bill database: " + uri.toString());
                    return null;
                }

                // Otherwise notify the changes
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the uri of the new row
                return ContentUris.withAppendedId(uri, id);

            default: // Otherwise throw an exception
                throw new IllegalArgumentException("Unknown uri " + uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Database
        SQLiteDatabase db;

        // Match the provided URI with the originals
        final int match = sUriMatcher.match(uri);

        // The number of rows which will be deleted. Return argument of the method
        int rowsDeleted;

        switch (match){

            // If the deletion is on the menu table
            case MENU:
            case MENU_ID:

                // Get writable menu database
                db = menuDbHelper.getWritableDatabase();

                // Execute deletion
                rowsDeleted = db.delete(MenuContract.MenuEntry.TABLE_NAME, selection, selectionArgs);

                // Notify change if there are any rows deleted
                if(rowsDeleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows deleted
                return rowsDeleted;

            // If the deletion is on the order table
            case ORDER:
            case ORDER_ID:

                // Get writable order database
                db = orderDbHelper.getWritableDatabase();

                // Execute deletion
                rowsDeleted = db.delete(OrderContract.OrderEntry.TABLE_NAME, selection, selectionArgs);

                // Notify change if there are any rows deleted
                if(rowsDeleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows deleted
                return rowsDeleted;

            // If the deletion is on the bill table
            case BILL:
            case BILL_ID:

                // Get writable bill database
                db = billDbHelper.getWritableDatabase();

                // Execute deletion
                rowsDeleted = db.delete(BillContract.BillEntry.TABLE_NAME, selection, selectionArgs);

                // Notify change if there are any rows deleted
                if(rowsDeleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows deleted
                return rowsDeleted;

            default: // Otherwise throw an exception
                throw new IllegalArgumentException("Unknown uri " + uri.toString());
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Database
        SQLiteDatabase db;

        // Match the URI with the originals
        final int match = sUriMatcher.match(uri);

        // The number of rows updated. Return argument of the method
        int rowsUpdated;
        switch (match){

            case MENU:
            case MENU_ID:

                // Get writable database
                db = menuDbHelper.getWritableDatabase();

                // Update the database and return the number of rows which are updated
                rowsUpdated = db.update(MenuContract.MenuEntry.TABLE_NAME, values, selection, selectionArgs);

                // If the number of rows which are updated is greater than 0 (there are updated rows) notify the changes
                if(rowsUpdated > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows which are updated
                return rowsUpdated;

            case ORDER:
            case ORDER_ID:

                // Get writable database
                db = orderDbHelper.getWritableDatabase();

                // Update the database and return the number of rows which are updated
                rowsUpdated = db.update(OrderContract.OrderEntry.TABLE_NAME, values, selection, selectionArgs);

                // If the number of rows which are updated is greater than 0 (there are updated rows) notify the changes
                if(rowsUpdated > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows which are updated
                return rowsUpdated;

            case BILL:
            case BILL_ID:

                // Get writable database
                db = billDbHelper.getWritableDatabase();

                // Update the database and return the number of rows which are updated
                rowsUpdated = db.update(BillContract.BillEntry.TABLE_NAME, values, selection, selectionArgs);

                // If the number of rows which are updated is greater than 0 (there are updated rows) notify the changes
                if(rowsUpdated > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows which are updated
                return rowsUpdated;

            default: // Otherwise throw an exception
                throw new IllegalArgumentException("Unknown uri " + uri.toString());
        }
    }
}
