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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.EmailResultAdapter;

public class EmailResultFragment extends ResultFragment {

    EmailAddressParsedResult result;

    RecyclerView resultList;

    public EmailResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_email, container, false);

        result = (EmailAddressParsedResult) parsedResult;

        resultList = v.findViewById(R.id.fragment_result_recycler_view);
        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setAdapter(new EmailResultAdapter(result));

        return v;
    }

    @Override
    public void onProceedPressed(Context context) {
        // TODO: rework this..
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(R.array.send_email_array, (dialog, which) -> {
                    String caption = "";
                    switch (which) {
                        case 0:
                            Intent email = new Intent(Intent.ACTION_SENDTO);
                            email.setData(Uri.parse("mailto:"));
                            email.putExtra(Intent.EXTRA_EMAIL, result.getTos());
                            email.putExtra(Intent.EXTRA_SUBJECT, result.getSubject());
                            email.putExtra(Intent.EXTRA_TEXT, result.getBody());
                            caption = getResources().getStringArray(R.array.send_email_array)[0];
                            startActivity(Intent.createChooser(email, caption));
                            break;
                        case 1:
                            final Uri mailUri = Uri.fromParts("mailto", result.getTos()[0], null);
                            final Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, mailUri);
                            caption = getResources().getStringArray(R.array.send_email_array)[1];
                            startActivity(Intent.createChooser(contact, caption));
                            break;
                        default:
                    }
                })
                .show();
    }


}
