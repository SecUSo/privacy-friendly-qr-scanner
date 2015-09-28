package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * Created by Philipp on 12.09.2015.
 */
public class IntroFragment extends Fragment {

    public IntroFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_intro, container, false);

        TextView bull1 = (TextView) rootView.findViewById(R.id.bull1);
        TextView bull2 = (TextView) rootView.findViewById(R.id.bull2);
        TextView bull3 = (TextView) rootView.findViewById(R.id.bull3);
        TextView bull4 = (TextView) rootView.findViewById(R.id.bull4);

        bull1.setText(Html.fromHtml("&#8226;&nbsp;&nbsp;"));
        bull2.setText(Html.fromHtml("&#8226;&nbsp;&nbsp;"));
        bull3.setText(Html.fromHtml("&#8226;&nbsp;&nbsp;"));
        bull4.setText(Html.fromHtml("&#8226;&nbsp;&nbsp;"));

        final Button confirm = (Button) rootView.findViewById(R.id.confirmIntro);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                sharedPref.edit().putBoolean("firstVisit", false).commit();
                confirm.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                ((MainActivity)getActivity()).selectItem(0,false);
            }


        });
        return rootView;
    }
}
