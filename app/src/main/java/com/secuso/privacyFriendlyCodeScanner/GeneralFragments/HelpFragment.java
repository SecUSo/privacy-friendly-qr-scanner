package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * Created by philipprack on 25.09.16.
 */

public class HelpFragment extends PreferenceFragment {

    public HelpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.help);
    }
}
