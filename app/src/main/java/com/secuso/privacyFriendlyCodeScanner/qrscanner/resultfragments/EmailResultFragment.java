package com.secuso.privacyFriendlyCodeScanner.qrscanner.resultfragments;

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
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

public class EmailResultFragment extends ResultFragment {

    private String eAddress;

    public EmailResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        TextView resultField = (TextView) v.findViewById(R.id.result_field_email);

        String email = qrCodeString;
        eAddress = email.substring(7);

        // final String eAddress=email;
        resultField.setText(eAddress);

        return v;
    }

    public void onProceedPressed(Context context, String content) {AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.send_email_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String caption = "";
                        switch (which) {
                            case 0:

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("plain/text");
                                String[] recipient = new String[]{eAddress};
                                email.putExtra(Intent.EXTRA_EMAIL, recipient);
                                email.putExtra(Intent.EXTRA_SUBJECT, "");
                                email.putExtra(Intent.EXTRA_TEXT, "");
                                caption = getResources().getStringArray(R.array.send_email_array)[0];
                                startActivity(Intent.createChooser(email, caption));
                                break;
                            case 1:

                                //Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,Uri.parse(s));
                                final Uri mailUri = Uri.fromParts("mailto", eAddress, null);
                                final Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, mailUri);
                                caption = getResources().getStringArray(R.array.send_email_array)[1];
                                startActivity(Intent.createChooser(contact, caption));
                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
