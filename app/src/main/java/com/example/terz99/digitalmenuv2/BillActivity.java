package com.example.terz99.digitalmenuv2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terz99.digitalmenuv2.adapters.OrderAdapter;
import com.example.terz99.digitalmenuv2.data.BillContract;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.view.View.GONE;

public class BillActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<OrderItem>>{

    public static final int BILL_ID = 1321;

    // Loader id for the bill database
    private static final int BILL_LOADER_ID = 32131;

    // Array list which stores the data from the bill database
    private ArrayList<OrderItem> mData;

    // Adapter used for displaying the data from mData on the UI
    private OrderAdapter mAdapter;

    private static double totalPrice;

    private TextView requestBillFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.bill_my_toolbar);
        setSupportActionBar(myToolbar);

        requestBillFab =
                (TextView) findViewById(R.id.b_request_bill_fab);


        requestBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show an alert dialog to confirm order
                showConfirmationDialog();

            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String isBillRequested = preferences.getString(getString(R.string.bill_request_key), getString(R.string.bill_request_value));
        if(isBillRequested.equals("1")){
            requestBillFab.setVisibility(GONE);
        }

        if(mData == null || mData.size() == 0) {
            getSupportLoaderManager().initLoader(BILL_LOADER_ID, null, this);
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

        alertDialBuilder.setMessage(R.string.bill_request_dialog_msg);


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

                if(mData == null || mData.size() == 0){
                    Toast.makeText(BillActivity.this, R.string.bill_request_failed, Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BillActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.bill_request_key), "1");
                    editor.apply();

                    Toast.makeText(BillActivity.this, R.string.bill_request_successful, Toast.LENGTH_LONG)
                            .show();
                }


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

    @Override
    public Loader<ArrayList<OrderItem>> onCreateLoader(int id, Bundle args) {

        switch(id){

            case BILL_LOADER_ID:
                return new FetchBillDataTask(this);
            default:
                throw new IllegalArgumentException("Unknown loader id " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<OrderItem>> loader, ArrayList<OrderItem> data) {
        mData = data;
        setupContent();
    }

    private void setupContent() {

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.b_listview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new OrderAdapter(this, mData, BILL_ID);

        mRecyclerView.setAdapter(mAdapter);

        setTotalPrice();
    }

    private void setTotalPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        TextView totalPriceTextView = (TextView) findViewById(R.id.total_prize_bill);
        totalPriceTextView.setText(String.valueOf(decimalFormat.format(totalPrice)));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<OrderItem>> loader) {
        mData = null;
    }

    public static class FetchBillDataTask extends AsyncTaskLoader<ArrayList<OrderItem>>{

        Context sContext;

        public FetchBillDataTask(Context context) {
            super(context);
            sContext = context;
        }

        @Override
        public ArrayList<OrderItem> loadInBackground() {

            Cursor cursor;

            String[] projection = {
                    BillContract.BillEntry.COLUMN_NAME,
                    BillContract.BillEntry.COLUMN_PRICE,
                    BillContract.BillEntry.COLUMN_QUANTITY,
                    BillContract.BillEntry.COLUMN_PHOTO_ID
            };

            cursor = sContext.getContentResolver().query(BillContract.BillEntry.CONTENT_URI, projection, null, null, null);

            ArrayList<OrderItem> data = new ArrayList<OrderItem>();
            totalPrice = 0;

            while (cursor != null && cursor.moveToNext()){

                String name = cursor.getString(cursor.getColumnIndex(BillContract.BillEntry.COLUMN_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(BillContract.BillEntry.COLUMN_PHOTO_ID));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(BillContract.BillEntry.COLUMN_PRICE)));
                int quantity = cursor.getInt(cursor.getColumnIndex(BillContract.BillEntry.COLUMN_QUANTITY));

                data.add(new OrderItem(name, price, quantity, imageId));

                totalPrice += price*(double)quantity;
            }

            return data;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }
}
