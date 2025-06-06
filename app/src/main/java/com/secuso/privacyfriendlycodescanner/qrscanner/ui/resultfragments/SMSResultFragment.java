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
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.SMSParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class SMSResultFragment extends ResultFragment {

    SMSParsedResult result;

    public SMSResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_sms, container, false);
        result = (SMSParsedResult) parsedResult;

        TextView to = (TextView) v.findViewById(R.id.fragment_result_sms_to);
        TextView toLabel = (TextView) v.findViewById(R.id.fragment_result_sms_to_label);
        TextView via = (TextView) v.findViewById(R.id.fragment_result_sms_via);
        TextView viaLabel = (TextView) v.findViewById(R.id.fragment_result_sms_via_label);
        TextView subject = (TextView) v.findViewById(R.id.fragment_result_sms_subject);
        TextView subjectLabel = (TextView) v.findViewById(R.id.fragment_result_sms_subject_label);
        TextView body = (TextView) v.findViewById(R.id.fragment_result_sms_body);

        if (result != null) {
            StringBuilder numberSb = new StringBuilder();
            ParsedResult.maybeAppend(result.getNumbers(), numberSb);

            StringBuilder viaSb = new StringBuilder();
            ParsedResult.maybeAppend(result.getVias(), viaSb);

            to.setText(numberSb.toString());
            via.setText(viaSb.toString());
            subject.setText(result.getSubject());
            body.setText(result.getBody());

            to.setVisibility(TextUtils.isEmpty(numberSb.toString()) ? View.GONE : View.VISIBLE);
            toLabel.setVisibility(TextUtils.isEmpty(numberSb.toString()) ? View.GONE : View.VISIBLE);
            via.setVisibility(TextUtils.isEmpty(viaSb.toString()) ? View.GONE : View.VISIBLE);
            viaLabel.setVisibility(TextUtils.isEmpty(viaSb.toString()) ? View.GONE : View.VISIBLE);
            subject.setVisibility(TextUtils.isEmpty(result.getSubject()) ? View.GONE : View.VISIBLE);
            subjectLabel.setVisibility(TextUtils.isEmpty(result.getSubject()) ? View.GONE : View.VISIBLE);
            body.setVisibility(TextUtils.isEmpty(result.getBody()) ? View.GONE : View.VISIBLE);
        }

        return v;
    }

    public void onProceedPressed(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(R.array.sms_array, (dialog, which) -> {
                    String caption = "";
                    switch (which) {
                        case 0:
                            Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + result.getNumbers()[0]));
                            sms.putExtra("address", result.getNumbers()[0]);
                            sms.putExtra("sms_body", result.getBody());
                            caption = getResources().getStringArray(R.array.sms_array)[0];
                            startActivity(Intent.createChooser(sms, caption));
                            break;
                        case 1:
                            Intent call = new Intent("android.intent.action.DIAL");
                            call.setData(Uri.parse("tel:" + result.getNumbers()[0]));
                            caption = getResources().getStringArray(R.array.sms_array)[1];
                            startActivity(Intent.createChooser(call, caption));
                            break;
                        case 2:
                            Intent contact = new Intent(
                                    ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                                    Uri.parse("tel:" + result.getNumbers()[0]));
                            caption = getResources().getStringArray(R.array.sms_array)[2];
                            startActivity(Intent.createChooser(contact, caption));
                            break;
                        default:
                    }
                })
                .show();
    }
}
