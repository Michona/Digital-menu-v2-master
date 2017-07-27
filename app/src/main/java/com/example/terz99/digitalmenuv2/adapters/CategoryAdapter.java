package com.example.terz99.digitalmenuv2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.terz99.digitalmenuv2.fragments.CocktailFragment;
import com.example.terz99.digitalmenuv2.fragments.PizzaFragment;
import com.example.terz99.digitalmenuv2.R;
import com.example.terz99.digitalmenuv2.fragments.WineFragment;

import java.util.ArrayList;


public class CategoryAdapter extends FragmentPagerAdapter {

    // instance for the context/activity where the current action comes from
    private Context context;
    // number of fragments
    private int PAGE_COUNT;
    // titles for the slideable tabs
    private ArrayList<String> tabTitles;

    /**
     * Public constructor for the CategoryAdapter
     * @param fragmentManager - fragmentManager achieved from getSupportFragmentManager() method
     * @param context - parameter of the context/activity where the current action comes from
     */
    public CategoryAdapter(FragmentManager fragmentManager, Context context) {

        // Call super constructor
        super(fragmentManager);
        // Get the instance of the Context
        this.context = context;
        // Get the titles of the fragments for the slideable tab
        tabTitles = addData();
        // Initialize the PAGE_COUNT
        PAGE_COUNT = tabTitles.size();
    }

    /**
     * This method stores the data (titles for the slideable tabs) into an ArrayList<String>
     * @return ArrayList<String> with the titles for the slideable tabs
     */
    private ArrayList<String> addData() {

        ArrayList<String> titles = new ArrayList<String>();
        titles.add(context.getString(R.string.pizza));
        titles.add(context.getString(R.string.wine));
        titles.add(context.getString(R.string.cocktails));
        return titles;
    }

    /**
     * This method returns a Fragment instance so the user can slide left and right to navigate
     * through the categories
     * @param position - position of the fragment in the layout
     * @return a new Fragment instance adequate with the position
     */
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new PizzaFragment();
            case 1:
                return new WineFragment();
            default:
                return new CocktailFragment();
        }
    }

    /**
     * @return number of fragments
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * This method helps the Fragment and the TabLayout to be in sync
     * @param position - position of the fragment
     * @return CharSequence with the title of the fragment
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}