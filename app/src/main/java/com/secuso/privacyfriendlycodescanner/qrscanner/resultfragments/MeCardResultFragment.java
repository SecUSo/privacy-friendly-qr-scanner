package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class MeCardResultFragment extends ResultFragment {

    private String name;
    private String email;
    private String phone;
    private String address;

    public MeCardResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView resultTextName = (TextView) v.findViewById(R.id.result_text_name);
        TextView resultTextEmail = (TextView) v.findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) v.findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) v.findViewById(R.id.result_text_Address);

        final String contactResult = qrCodeString;

        String[] content = contactResult.substring(contactResult.indexOf(":") + 1).split(";");
        int name_id = 0;
        int email_id = 0;
        int phone_id = 0;
        int address_id = 0;


        for (int i = 0; i < content.length; i++) {
            if (content[i].startsWith("N:")) name_id = i;
            if (content[i].startsWith("EMAIL:")) email_id = i;
            if (content[i].startsWith("TEL:")) phone_id = i;
            if (content[i].startsWith("ADR:")) address_id = i;
        }

        name = content[name_id].substring(2);
        email = content[email_id].substring(6);
        phone = content[phone_id].substring(4);
        address = content[address_id].substring(4);

        resultTextName.setText("Name: " + name);
        resultTextEmail.setText("Email: " + email);
        resultTextPhone.setText("Tel: " + phone);
        resultTextAddress.setText("Address: " + address);

        return v;
    }

    public void onProceedPressed(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.vcard_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent contact = new Intent(ContactsContract.Intents.Insert.ACTION);
                                contact.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                                contact.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                                contact.putExtra(ContactsContract.Intents.Insert.NAME, name);
                                contact.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                                // contact.putExtra(ContactsContract.Intents.Insert.COMPANY, org);
                                // contact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title);

                                String caption = getResources().getStringArray(R.array.vcard_array)[0];
                                startActivity(Intent.createChooser(contact, caption));
                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
