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
        final String searchEngineURI = getSearchEngineURI(context);
        final Uri uri = Uri.parse(String.format(searchEngineURI, content));

        Intent search = new Intent(Intent.ACTION_VIEW, uri);
        String caption = getResources().getStringArray(R.array.text_array)[0];
        startActivity(Intent.createChooser(search, caption));
    }

    private String getSearchEngineURI(final Context context) {
        final String duckDuckGo =
                getResources().getStringArray(R.array.pref_search_engine_values)[0];
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        final String searchEngineType = pref.getString("pref_search_engine", duckDuckGo);
        return duckDuckGo.equals(searchEngineType)
                ? getResources().getString(R.string.pref_search_engine_uri_duckduckgo)
                : getResources().getString(R.string.pref_search_engine_uri_google);
    }
}
