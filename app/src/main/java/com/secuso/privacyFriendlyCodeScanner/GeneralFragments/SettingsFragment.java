package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.ScrollView;

import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.R;

public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference button = getPreferenceManager().findPreference("buttonIntroAgain");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                ((MainActivity)getActivity()).switchToFragment(new IntroFragment(),false);
                return true;
            }
        });
    }

}
