package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

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

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class ContactResultFragment extends ResultFragment {

    AddressBookParsedResult result;

    public ContactResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_contact, container, false);

        result = (AddressBookParsedResult) parsedResult;

        TextView resultTextName = (TextView) v.findViewById(R.id.result_text_contact);
        TextView resultTextLastName = (TextView) v.findViewById(R.id.result_text_lastname);
        TextView resultTextEmail = (TextView) v.findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) v.findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) v.findViewById(R.id.result_text_Address);
        TextView resultTextCompany = (TextView) v.findViewById(R.id.result_text_Company);
        TextView resultTextTitle = (TextView) v.findViewById(R.id.result_text_Title);

        resultTextName.setText("Name: " + result.getNames()[0]);
        resultTextLastName.setText("Lastname: " + result.getNames()[0]);
        resultTextEmail.setText("Email: " + result.getEmails()[0]);
        resultTextPhone.setText("Tel: " + result.getPhoneNumbers()[0]);
        resultTextAddress.setText("Address: " + result.getAddresses()[0]);
        resultTextCompany.setText("Company: " + result.getOrg());
        resultTextTitle.setText("Title: " + result.getTitle());

        return v;
    }

    public void onProceedPressed(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.vcard_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent contact = new Intent(ContactsContract.Intents.Insert.ACTION);
                            contact.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                            contact.putExtra(ContactsContract.Intents.Insert.PHONE, result.getPhoneNumbers()[0]);
                            contact.putExtra(ContactsContract.Intents.Insert.NAME, result.getNames()[0]);
                            contact.putExtra(ContactsContract.Intents.Insert.EMAIL, result.getEmails()[0]);
                            contact.putExtra(ContactsContract.Intents.Insert.COMPANY, result.getOrg());
                            contact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, result.getTitle());

                            String caption = getResources().getStringArray(R.array.vcard_array)[0];
                            startActivity(Intent.createChooser(contact, caption));
                            break;

                        default:
                    }
                });
        builder.create().show();
    }
}
