package com.example.terz99.digitalmenuv2.preferences;

import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.terz99.digitalmenuv2.R;

/**
 * This class is used only to store the preferences. The user will not need to checkout the
 * preferences in this class because these preferences store only the current database version.
 */
public class SharedPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);
    }

    public static class SharedPreferences extends PreferenceFragment{


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.database_pref);
        }
    }
}
