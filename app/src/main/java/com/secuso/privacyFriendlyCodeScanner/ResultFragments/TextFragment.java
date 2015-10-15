package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TextFragment extends ResultFragment {
    private static boolean checked = false;
    private static boolean trust = false;

    public TextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text, container, false);

        rootView = super.setResult(rootView);

        TextView resultText = (TextView) rootView.findViewById(R.id.result_field_text);
        resultText.setText(this.result);

        Button chooseActionButton = (Button) rootView.findViewById(R.id.btnProceed);
        chooseActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.text_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        saveScanned(true);
                                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                                        search.putExtra(SearchManager.QUERY, result);
                                        String caption = getActivity().getResources().getStringArray(R.array.text_array)[0];
                                        startActivity(Intent.createChooser(search, caption));
                                        break;
                                    case 1:
                                        saveScanned(true);
                                        break;
                                    default:
                                }
                            }
                        });
                builder.create().show();
            }
        });

        return rootView;
    }
}