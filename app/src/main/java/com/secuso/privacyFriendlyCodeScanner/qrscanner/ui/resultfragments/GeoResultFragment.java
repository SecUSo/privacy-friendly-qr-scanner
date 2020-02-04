package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

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

import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class GeoResultFragment extends ResultFragment {

    GeoParsedResult result;

    private String geoResult;

    public GeoResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_geo_info, container, false);

        result = (GeoParsedResult) parsedResult;

        TextView resultField = (TextView) v.findViewById(R.id.result_field_geo);

        resultField.setText(result.getGeoURI());

        return v;
    }

    public void onProceedPressed(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.geo_array, (dialog, which) -> {
                    String caption = "";
                    switch (which) {
                        case 0:
                            Uri gmmIntentUri = Uri.parse(result.getGeoURI());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                            mapIntent.setData(gmmIntentUri);

                            startActivity(Intent.createChooser(mapIntent, caption));


                            break;

                        default:
                    }
                });
        builder.create().show();
    }
}
