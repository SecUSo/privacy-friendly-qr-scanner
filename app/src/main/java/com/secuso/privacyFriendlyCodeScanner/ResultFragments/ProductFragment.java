package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class ProductFragment extends ResultFragment {
    private static boolean checked = false;
    private static boolean trust = false;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        final View resultRootView = super.setResult(rootView);

        TextView resultText = rootView.findViewById(R.id.result_field_product);
        resultText.setText(this.result);

        Button proceed = rootView.findViewById(R.id.btnProceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.product_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        saveScanned(true);
                                        search(resultRootView, R.array.product_array);
                                        break;
                                    case 1:
                                        saveScanned(true);
                                        abort();
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
