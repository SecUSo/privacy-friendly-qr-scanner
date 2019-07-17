package com.secuso.privacyfriendlycodescanner.qrscanner;

import android.annotation.SuppressLint;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.AppCompatPreferenceActivity;


public class SettingsActivity extends AppCompatPreferenceActivity {



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();



    }
    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


        }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}







