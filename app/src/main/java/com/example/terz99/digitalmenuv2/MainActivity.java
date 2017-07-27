/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.terz99.digitalmenuv2;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.terz99.digitalmenuv2.adapters.CategoryAdapter;
import com.example.terz99.digitalmenuv2.data.BillContract;
import com.example.terz99.digitalmenuv2.data.MenuContract;
import com.example.terz99.digitalmenuv2.data.MenuDbHelper;
import com.example.terz99.digitalmenuv2.data.OrderContract;
import com.example.terz99.digitalmenuv2.fragments.CocktailFragment;
import com.example.terz99.digitalmenuv2.fragments.PizzaFragment;
import com.example.terz99.digitalmenuv2.fragments.WineFragment;

import java.util.ArrayList;

import static com.example.terz99.digitalmenuv2.fragments.CocktailFragment.*;
import static com.example.terz99.digitalmenuv2.fragments.PizzaFragment.*;
import static com.example.terz99.digitalmenuv2.fragments.WineFragment.*;


public class MainActivity extends AppCompatActivity {

    // ID for query loader
    private static final int FETCH_DATA_LOADER_ID = 5;
    // ID for the insert loader
    private static final int MENU_LOADER_ID = 0;
    // Helper ArrayList<Item> to store data for a short amount of time
    private ArrayList<Item> mItems;
    public static ArrayList<Item> mPizzaData;
    public static ArrayList<Item> mWineData;
    public static ArrayList<Item> mCocktailData;

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {

        // create the MainActivity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (checkDataBaseVersion()) {
            addData();
        } else {
            if (mPizzaData == null || mPizzaData.size() == 0 || mWineData == null || mWineData.size() == 0 || mCocktailData == null || mCocktailData.size() == 0) {
                getSupportLoaderManager().initLoader(FETCH_DATA_LOADER_ID, null, fetchLoaderCallbacks);
            } else {
                setupContent();
            }
        }
    }

    /**
     * This method adds the data to the newer version of the database
     * This is a beta-version of the app method, it will not be used in the real version
     */
    private void addData() {

        mItems = new ArrayList<Item>();

        // Adding pizzas
        mItems.add(new Item("New York Style Pizza", 19.20, "New York-style pizza is traditionally hand-tossed, consisting in its basic form of a light layer of tomato sauce and dry, grated, full-fat mozzarella cheese; additional toppings are placed atop the cheese.", R.drawable.rsz_new_york, PIZZA_ID));
        mItems.add(new Item("Sicilian Pizza", 17.00, "Topped with onions, anchovies, tomatoes, herbs and strong cheese such as caciocavallo and toma.", R.drawable.rsz_sicilian, PIZZA_ID));
        mItems.add(new Item("Chicago Deep Dish", 19.00, " The crust is covered with cheese (generally sliced mozzarella), followed by various meat options such as pepperoni or sausage" +
                "In addition to ordinary wheat flour, the pizza dough may contain corn meal, semolina, or food coloring, giving the crust a distinctly yellowish tone", R.drawable.rsz_chichago, PIZZA_ID));
        mItems.add(new Item("Detroit Style Pizza", 16.00, ". Pepperoni is placed on top of the crust, followed by cheese and other traditional toppings. The sauce is on top, in contrast to New York thin crust with visible toppings and Chicago deep-dish with sauce poured over toppings", R.drawable.rsz_detroit, PIZZA_ID));


        // Adding cocktails
        mItems.add(new Item("Lavender White Lady", 10.60, "4cl - Appleton Estate Rum, " +
                "Orange curacao, Lime juice, Orgeat, Grenadine, Pineapple juice",
                R.drawable.lavender_white_lady,
                COCKTAIL_ID));
        mItems.add(new Item("Absolut Gangster", 12.60, "4 cl - Absolut Elyx, " +
                "Lillet, " +
                "Sugar Syrup, " +
                "Vanilla Bitters", R.drawable.absolut_gangster, COCKTAIL_ID));
        mItems.add(new Item("Lavender Spring Sour", 11.40, "4cl - Vodka, Orange Juice, Lime.",
                R.drawable.lavender_spring_sour,
                COCKTAIL_ID));
        mItems.add(new Item("Hendrick's Summer Garden", 14.60, "4cl - Vodka, rum, brandy.",
                R.drawable.henrick_summer_gardem,
                COCKTAIL_ID));
        mItems.add(new Item("Mai Tai", 11.20, "4cl - Appleton 15 Year Old Rum" +
                "Lime juice, " +
                "Orgeat, " +
                "Sugar syrup, " +
                "Orange curacao, " +
                "1 Mint leaf.", R.drawable.mai_tai, COCKTAIL_ID));
        mItems.add(new Item("Lychee Collins", 10.50, "4cl - Beefeater Gin, " +
                "Lychee liqueur, " +
                "Lychee juice.", R.drawable.lychee_collins,
                COCKTAIL_ID));
        mItems.add(new Item("Passion Rum Punch", 10.40, "4cl - Appleton Estate Rum, " +
                "Orange curacao, " +
                "Lime juice, " +
                "Orgeat, " +
                "Grenadine, " +
                "Pineapple juice.", R.drawable.passion_rum_punch,
                COCKTAIL_ID));
        mItems.add(new Item("El Hefe", 12.70, "4cl - Plantation Guatemala & Belize Rum" +
                "Lillet, " +
                "Maraschino Syrup, " +
                "Dry Curacao.", R.drawable.el_jefe, COCKTAIL_ID));
        mItems.add(new Item("Wild Berry Spritzer", 10.00, "4cl - Vodka, rum, brandy",
                R.drawable.wild_berry_spritzer,
                COCKTAIL_ID));
        mItems.add(new Item("Hunter's Grog", 12.90, "Grapefruit Juice, " +
                "Lemon Juice, " +
                "Bitter Orange Marmalade, " +
                "Ginger Beer.", R.drawable.hunter_grog,
                COCKTAIL_ID));

        // Adding wines
        mItems.add(new Item("Reisling", 33.75, "750ml" +
                ", 100% Riesling from Columbia Valley, Washington (primarily the Evergreen Vineyard in the Ancient Lakes AVA and Zillah Ranch in Yakima Valley). The wine has a residual sugar level of 15.5 g/L." +
                " 12.0% Alcohol.", R.drawable.riesling, WINE_ID));
        mItems.add(new Item("Chardonnay", 31.90, "750ml," +
                " 98% Chardonnay and 2% Viognier from California. The wine has a residual sugar of 3.2g/L." +
                " 13.5% Alcohol.", R.drawable.chardonnay, WINE_ID));
        mItems.add(new Item("Sauvigon Blanc", 24.50, "750ml" +
                ", Sauvignon Blanc from Potter Valley, Mendocino County, California. Made with organically grown grapes. Fermented and aged in stainless steel." +
                " 14% Alcohol.", R.drawable.sauvignon_blanc, WINE_ID));
        mItems.add(new Item("Syrah", 22.60, "750ml" +
                ", 100% Syrah from the Mirage, Canyon Vineyard Ranch, Waterbrook Estate and Skyfall Vineyards in Columbia Valley, Washington. The Waterbrook Syrah spends 10 months in 20% new American oak and 80% 2-5 year old American, French and Hungarian oak." +
                " 13.4% Alcohol.", R.drawable.syrah, WINE_ID));
        mItems.add(new Item("Merlot", 23.60, "750ml" +
                ", 97% Merlot and 3% Malbec from Oakville, Carneros and Oak Knoll in Napa Valley, California. The wine spends 18 months in oak barrels." +
                " 13.5% Alcohol.", R.drawable.merlot, WINE_ID));
        mItems.add(new Item("Cabernet Saubignon", 25.20, "750ml" +
                ", 85% Cabernet Sauvignon and 15% Merlot from Colchagua Valley, Chile. 45% of the wine is aged in French oak barrels for eight months. The wine has residual sugar of just 2.55 grams/liter." +
                " 13.5% Alcohol.", R.drawable.cabernet_sauvignon, WINE_ID));
        mItems.add(new Item("Pinot Noir", 31.90, "750ml" +
                ", 95% Pinot Noir and 5% Barbera from California. The wine spends 10 months in 100% French oak (60% new, 40% seasoned)." +
                " 13.5% Alcohol.", R.drawable.pinot_noir, WINE_ID));

        getSupportLoaderManager().initLoader(MENU_LOADER_ID, null, uploadLoaderCallbacks);
    }

    /**
     * @return if there is a newer version of the database
     */
    private boolean checkDataBaseVersion() {

        Log.e(TAG, "Checking database version...");

        // Get link from the shared preferences. The database version is stored in shared
        // preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the current version stored in the shared preferences
        int databaseVersionFromPreferences = Integer.parseInt(
                preferences.getString(getString(R.string.database_version_key),
                        getString(R.string.database_version_default_value)));

        // See the current version stored in the shared preferences is equal to the
        // current version of the database. If so, then update the shared preferences
        // and return true
        if (databaseVersionFromPreferences != MenuDbHelper.getDatabaseVersion()) {
            databaseVersionFromPreferences = MenuDbHelper.getDatabaseVersion();
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putString(getString(R.string.database_version_key),
                    String.valueOf(databaseVersionFromPreferences));
            preferenceEditor.apply();
            Log.e(TAG, "" + databaseVersionFromPreferences);
            return true;
        } else {
            return false;
        }
    }

    // Overriden method which inflates the options menu on the App Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Overriden method which is called whenever a options menu item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Get the id of the clicked item
        int clickedItemId = item.getItemId();

        // Check which item was clicked and open adequate activity according to the item clicked
        switch (clickedItemId) {

            case R.id.action_bill:

                Intent openBillActivityIntent = new Intent(this, BillActivity.class);
                startActivity(openBillActivityIntent);
                return true;
            case R.id.action_my_order:

                Intent openMyOrderActivityIntent = new Intent(this, MyOrderActivity.class);
                startActivity(openMyOrderActivityIntent);
                return true;

            case R.id.action_refresh:
                showPasswordDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPasswordDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setCancelable(false);

        final View alertDialogView;
        alertDialogView = LayoutInflater.from(this).inflate(R.layout.password_prompt, null);

        alertDialogBuilder.setView(alertDialogView);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText passwordEditText = (EditText) alertDialogView.findViewById(R.id.password_prompt_edittext);
                String typedPassword = passwordEditText.getText().toString();

                if (typedPassword != null && typedPassword.equals(getString(R.string.password))) {
                    showConfirmationDialog();
                } else {
                    showDeniedDialog();
                }

                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

    }

    private void showDeniedDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.wrong_password);

        builder.setCancelable(false);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTitleColor(R.color.primary_text);
                if (dialog != null) {
                    dialog.dismiss();
                }
                showPasswordDialog();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTitleColor(R.color.primary_text);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    private void showConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.closing_dialog_msg);

        builder.setCancelable(false);

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                getContentResolver().delete(OrderContract.OrderEntry.CONTENT_URI, null, null);
                getContentResolver().delete(BillContract.BillEntry.CONTENT_URI, null, null);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.bill_request_key), "0");
                editor.apply();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

        Button neutralbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralbutton.setTextColor(Color.BLACK);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Item>> fetchLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Item>>() {

        @Override
        public Loader<ArrayList<Item>> onCreateLoader(int id, Bundle args) {

            switch (id) {

                case FETCH_DATA_LOADER_ID:
                    return new FetchMenuDataTask(MainActivity.this);
                default:
                    throw new IllegalArgumentException("Unknown loader id " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {

            mCocktailData = new ArrayList<Item>();
            mWineData = new ArrayList<Item>();
            mPizzaData = new ArrayList<Item>();

            for (int i = 0; i < data.size(); i++) {

                Item currItem = data.get(i);

                if (currItem.getmCategoryId() == PIZZA_ID) {
                    mPizzaData.add(currItem);
                } else if (currItem.getmCategoryId() == WINE_ID) {
                    mWineData.add(currItem);
                } else {
                    mCocktailData.add(currItem);
                }
            }

            setupContent();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Item>> loader) {
            mPizzaData = null;
            mCocktailData = null;
            mWineData = null;
        }
    };

    private void setupContent() {


        // get the viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // declare a new adapter for the viewpager
        CategoryAdapter pagerAdapter = new CategoryAdapter(getSupportFragmentManager(), this);
        // set the adapter to the viewpager
        viewPager.setAdapter(pagerAdapter);
        // get the tabLayout resource
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        // set tabLayout in sync with the viewpager
        tabLayout.setupWithViewPager(viewPager);


    }

    private LoaderManager.LoaderCallbacks<Integer> uploadLoaderCallbacks = new LoaderManager.LoaderCallbacks<Integer>() {
        @Override
        public Loader<Integer> onCreateLoader(int id, Bundle args) {
            return new UploadDataTask(MainActivity.this, mItems);
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer data) {
            getSupportLoaderManager().initLoader(FETCH_DATA_LOADER_ID, null, fetchLoaderCallbacks);
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {
            // Do nothing
        }
    };
}