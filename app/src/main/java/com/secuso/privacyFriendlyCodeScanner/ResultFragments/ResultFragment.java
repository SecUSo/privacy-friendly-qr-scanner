package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.R;
import com.secuso.privacyFriendlyCodeScanner.Utility.History;

public abstract class ResultFragment extends Fragment {
    protected String result;
    protected Bitmap bitmap;
    protected String toast = "erfolgreich";
    protected boolean fromHistory = false;

    public ResultFragment() {
    }

    /*
        To be implemented
     */
    protected void createBitmap() {
    }

    protected View setResult(View view) {
        result = getArguments().getString("result_content");
        fromHistory = getArguments().getBoolean("history", false);
        if (fromHistory) {
            ((Button) view.findViewById(R.id.btnProceed)).setText(R.string.proceed);
            ((Button) view.findViewById(R.id.btnCancel)).setText(R.string.back);
        }

        Button abortActionButton = view.findViewById(R.id.btnCancel);
        abortActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abort();
            }
        });

        return view;
    }

    protected void abort() {
        if (fromHistory) {
            ((MainActivity) getActivity()).selectItem(1, false);
        } else {
            ((MainActivity) getActivity()).selectItem(0, false);
        }
    }

    protected void search(final View rootView, final int captionId) {
        final String searchURI = getSearchEngine(rootView);

        Uri uri = Uri.parse(searchURI + result);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        String caption = getActivity().getResources().getStringArray(captionId)[0];
        startActivity(Intent.createChooser(intent, caption));
    }

    protected void saveScanned(boolean trust) {
        if (!fromHistory)
            History.saveScan(result, trust, getActivity());
    }

    protected void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
        }
    }

    private String getSearchEngine(View rootView) {
        final String DUCK_DUCK_GO =
                getResources().getStringArray(R.array.pref_search_engine_values)[0];

        final String searchEnginePref = PreferenceManager
                .getDefaultSharedPreferences(rootView.getContext())
                .getString("pref_search_engine", DUCK_DUCK_GO);

        return DUCK_DUCK_GO.equals(searchEnginePref)
                ? "https://duckduckgo.com/?q="
                : "https://www.google.com/search?q=";
    }

}
