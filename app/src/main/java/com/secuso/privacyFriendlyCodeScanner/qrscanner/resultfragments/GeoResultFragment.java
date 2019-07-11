package com.secuso.privacyFriendlyCodeScanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

public class GeoResultFragment extends ResultFragment {

    private String geoResult;

    public GeoResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView resultField = (TextView) v.findViewById(R.id.result_field_geo);

        resultField.setText(qrCodeString);

        return v;
    }

    public void onProceedPressed(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.geo_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String caption = "";
                        switch (which) {
                            case 0:
                                Uri gmmIntentUri = Uri.parse(qrCodeString);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                                mapIntent.setData(gmmIntentUri);

                                startActivity(Intent.createChooser(mapIntent, caption));


                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
