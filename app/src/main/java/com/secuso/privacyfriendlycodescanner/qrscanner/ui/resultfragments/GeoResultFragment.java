/*
    Privacy Friendly QR Scanner
    Copyright (C) 2020-2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.GeoParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.text.DecimalFormat;

public class GeoResultFragment extends ResultFragment {

    private static final DecimalFormat df = new DecimalFormat("0.000000");

    GeoParsedResult result;

    public GeoResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_geo_info, container, false);

        result = (GeoParsedResult) parsedResult;

        TextView latitude = (TextView) v.findViewById(R.id.fragment_result_sms_to);
        TextView longitude = (TextView) v.findViewById(R.id.fragment_result_sms_via);
        TextView altitude = (TextView) v.findViewById(R.id.fragment_result_sms_subject);
        TextView altitude_label = (TextView) v.findViewById(R.id.fragment_result_sms_subject_label);
        TextView query = (TextView) v.findViewById(R.id.fragment_result_sms_body);
        ImageView queryImage = v.findViewById(R.id.item_result_location_query_image);

        latitude.setText(df.format(result.getLatitude()));
        longitude.setText(df.format(result.getLongitude()));
        altitude.setText(df.format(result.getAltitude()));

        query.setText(result.getQuery());

        altitude.setVisibility(result.getAltitude() != 0 ? View.VISIBLE : View.GONE);
        altitude_label.setVisibility(result.getAltitude() != 0 ? View.VISIBLE : View.GONE);

        query.setVisibility(!TextUtils.isEmpty(result.getQuery()) ? View.VISIBLE : View.GONE);
        queryImage.setVisibility(!TextUtils.isEmpty(result.getQuery()) ? View.VISIBLE : View.GONE);

        return v;
    }

    public void onProceedPressed(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
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
                })
                .show();
    }
}
