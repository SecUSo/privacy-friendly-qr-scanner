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

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.client.result.TelParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class TelResultFragment extends ResultFragment {

    TelParsedResult result;

    private String tel;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.tel_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent call = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel, null));
                            context.startActivity(Intent.createChooser(call, context.getResources().getStringArray(R.array.tel_array)[0]));
                            break;
                        case 1:
                            Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,Uri.fromParts("tel", tel, null));
                            context.startActivity(Intent.createChooser(contact, context.getResources().getStringArray(R.array.tel_array)[1]));
                            break;
                        default:
                    }
                });
        builder.create().show();
    }
}
