package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * Created by Philipp on 28.09.2015.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView)rootView.findViewById(R.id.website)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)rootView.findViewById(R.id.github)).setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }
}
