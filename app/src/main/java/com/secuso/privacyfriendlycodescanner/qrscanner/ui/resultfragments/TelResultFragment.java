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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.TelParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class TelResultFragment extends ResultFragment {

    TelParsedResult result;

    public TelResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_tel, container, false);
        result = (TelParsedResult) parsedResult;

        TextView resultField = (TextView) v.findViewById(R.id.result_field_tel);

        resultField.setText(result.getNumber());

        return v;
    }

    public void onProceedPressed(final Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(R.array.tel_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse(result.getTelURI()));
                            context.startActivity(Intent.createChooser(call, context.getResources().getStringArray(R.array.tel_array)[0]));
                            break;
                        case 1:
                            Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,Uri.parse(result.getTelURI()));
                            context.startActivity(Intent.createChooser(contact, context.getResources().getStringArray(R.array.tel_array)[1]));
                            break;
                        default:
                    }
                })
                .show();
    }
}
