package com.example.terz99.digitalmenuv2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terz99.digitalmenuv2.adapters.OrderAdapter;
import com.example.terz99.digitalmenuv2.data.BillContract;
import com.example.terz99.digitalmenuv2.data.OrderContract;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MyOrderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<OrderItem>>{

    public static final int ORDER_ID = 1241;

    private static final String TAG = MyOrderActivity.class.getSimpleName();

    private static final int ORDER_LOADER_ID = 10;

    private OrderAdapter mAdapter;

    private ArrayList<OrderItem> mData;

    public static double totalPrice;

    public static OrderItem deletedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.o_my_toolbar);
        setSupportActionBar(myToolbar);

        // Get link from the floating action button
        TextView oSubmitOrderFab =
                (TextView) findViewById(R.id.o_submit_order_fab);

        // Set the floating action button to say OK instead of having a picture
        // we call textAsBitmap() method because the setImageBitmap accepts only bitmap as a
        // parameter and textAsBitmap returns some text in type of Bitmaps


        // Set on click listener to the order button
        oSubmitOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show an alert dialog to confirm order
                showConfirmationDialog();

            }
        });

        if(mData == null || mData.size() == 0) {
            getSupportLoaderManager().initLoader(ORDER_LOADER_ID, null, this);
        } else {
            setupContent();
        }
    }

    /**
     * This method builds an alert dialog and displays it.
     * This method also sets the functions of the negative and the positive button of the
     * dialog
     */
    private void showConfirmationDialog() {

        AlertDialog.Builder alertDialBuilder = new AlertDialog.Builder(this);

        alertDialBuilder.setMessage(R.string.confirmation_dialog_msg);

        alertDialBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

        alertDialBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Check if there are items to be ordered
                if(mData == null || mData.size() == 0){
                    Toast.makeText(MyOrderActivity.this, R.string.no_order, Toast.LENGTH_SHORT).show();
                } else {

                    for(int i = 0; i < mData.size(); i++){

                        // Get link from the current item
                        OrderItem currItem = mData.get(i);

                        /**
                         * Add all the values to a ContentValues object, except for the quantity value
                         * Quantity value is put in the ContentValues later in the object
                         */
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(BillContract.BillEntry.COLUMN_NAME, currItem.getmName());
                        contentValues.put(BillContract.BillEntry.COLUMN_PHOTO_ID, currItem.getmImageId());
                        contentValues.put(BillContract.BillEntry.COLUMN_PRICE, String.valueOf(currItem.getmPrice()));
                        contentValues.put(BillContract.BillEntry.COLUMN_DESCRIPTION, "");

                        /**
                         * Write projection, selection and selectionArgs
                         * The projection is needed when we check the bill database whether or not it already consist of an item
                         * identical to currItem. If so, then we update this item in the bill database, otherwise we insert it
                         */
                        String[] projection = {
                                BillContract.BillEntry.COLUMN_NAME,
                                BillContract.BillEntry.COLUMN_QUANTITY
                        };

                        String selection = BillContract.BillEntry.COLUMN_NAME + "=?";

                        String[] selectionArgs = {
                                currItem.getmName()
                        };

                        // Check whether or not there is an item in the bill database identical to the bill database
                        Cursor checkForItem = getContentResolver().query(BillContract.BillEntry.CONTENT_URI,
                                projection,
                                selection,
                                selectionArgs,
                                null);

                        // If there is an identical item update the bill database
                        if(checkForItem != null && checkForItem.getCount() > 0){

                            checkForItem.moveToNext();
                            // Add the previous quantity and the new quantity and then update the item in the bill database
                            int previousQuantity = checkForItem.getInt(checkForItem.getColumnIndex(BillContract.BillEntry.COLUMN_QUANTITY));
                            contentValues.put(BillContract.BillEntry.COLUMN_QUANTITY, previousQuantity + currItem.getmQuantity());
                            getContentResolver().update(BillContract.BillEntry.CONTENT_URI, contentValues, selection, selectionArgs);
                        } else { // Otherwise, insert another row/item in the bill database
                            contentValues.put(BillContract.BillEntry.COLUMN_QUANTITY, currItem.getmQuantity());
                            getContentResolver().insert(BillContract.BillEntry.CONTENT_URI, contentValues);
                        }

                        checkForItem.close();
                    }

                    // Delete all sources of data from the MyOrderActivity since the order has been submitted already
                    mData = null;
                    getContentResolver().delete(OrderContract.OrderEntry.CONTENT_URI, null, null);
                    mAdapter.notifyDataSetChanged();

                    // Toast a message to the user that the order has been submitted successfully
                    Toast.makeText(MyOrderActivity.this, R.string.order_successful, Toast.LENGTH_SHORT).show();
                }

                // Return to MainActivty
                finish();
            }
        });

        AlertDialog alertDialog = alertDialBuilder.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    /**
     * This method transforms some text into a Bitmap object
     * @param text the text which will be transformed into a Bitmap Object
     * @param textSize the size of the text
     * @param textColor the color of the text
     * @return a Bitmap object displaying text
     */
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    void setupContent(){

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.o_listview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new OrderAdapter(this, mData, ORDER_ID);

        mRecyclerView.setAdapter(mAdapter);

        setTotalPrice();

        deletedItem = null;

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {

                if(deletedItem != null){
                    totalPrice -= deletedItem.getmPrice()*(double)deletedItem.getmQuantity();
                    deletedItem = null;
                    setTotalPrice();
                }
            }
        });
    }

    public void setTotalPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        TextView totalPriceTextView = (TextView) findViewById(R.id.total_prize_order);
        totalPriceTextView.setText(String.valueOf(decimalFormat.format(totalPrice)));
    }

    @Override
    public Loader<ArrayList<OrderItem>> onCreateLoader(int id, Bundle args) {

        switch (id){

            case ORDER_LOADER_ID:
                return new FetchDataTask(this);
            default:
                throw new IllegalArgumentException("Unknown loader id " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<OrderItem>> loader, ArrayList<OrderItem> data) {
        mData = data;
        setupContent();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<OrderItem>> loader) {
        mData = null;
    }

    private static class FetchDataTask extends AsyncTaskLoader<ArrayList<OrderItem>>{

        Context sContext;

        public FetchDataTask(Context context) {
            super(context);
            sContext = context;
        }

        @Override
        public ArrayList<OrderItem> loadInBackground() {

            Cursor cursor;

            String[] projection = {
                    OrderContract.OrderEntry.COLUMN_NAME,
                    OrderContract.OrderEntry.COLUMN_PRICE,
                    OrderContract.OrderEntry.COLUMN_QUANTITY,
                    OrderContract.OrderEntry.COLUMN_PHOTO_ID
            };

            cursor = sContext.getContentResolver().query(OrderContract.OrderEntry.CONTENT_URI, projection, null, null, null);

            ArrayList<OrderItem> data = new ArrayList<OrderItem>();
            totalPrice = 0;

            while (cursor != null && cursor.moveToNext()){

                String name = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PHOTO_ID));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE)));
                int quantity = cursor.getInt(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY));

                data.add(new OrderItem(name, price, quantity, imageId));

                totalPrice += price*(double)quantity;
            }

            if (cursor != null) {
                cursor.close();
            }

            return data;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }

}
