package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class TextResultFragment extends ResultFragment {

    public TextResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView tv = v.findViewById(R.id.result_field_text);
        tv.setText(qrCodeString);

        return v;
    }

    public void onProceedPressed(Context context, String content) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String searchURL = pref.getString("pref_search_engine", "https://duckduckgo.com/?q=%s");
        Uri uri = Uri.parse(String.format(searchURL, content));

        Intent search = new Intent(Intent.ACTION_VIEW, uri);
        String caption = getResources().getStringArray(R.array.text_array)[0];
        startActivity(Intent.createChooser(search, caption));
    }
}
