package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class TelResultFragment extends ResultFragment {

    private String tel;

    public TelResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        TextView resultField = (TextView) v.findViewById(R.id.result_field_tel);

        tel = qrCodeString.substring(4);
        resultField.setText(tel);

        return v;
    }

    public void onProceedPressed(final Context context, final String qrCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.tel_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent call = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel, null));
                                context.startActivity(Intent.createChooser(call, context.getResources().getStringArray(R.array.tel_array)[0]));
                                break;
                            case 1:
                                Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.fromParts("tel", tel, null));
                                context.startActivity(Intent.createChooser(contact, context.getResources().getStringArray(R.array.tel_array)[1]));
                                break;
                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
