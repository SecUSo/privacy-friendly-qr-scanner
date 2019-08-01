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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactResultFragment extends ResultFragment {

    private String title;
    private String company;
    private String address;
    private String phone;
    private String email;
    private String lastname;
    private String name;

    public ContactResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        TextView resultTextName = (TextView) v.findViewById(R.id.result_text_contact);
        TextView resultTextLastname = (TextView) v.findViewById(R.id.result_text_lastname);
        TextView resultTextEmail = (TextView) v.findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) v.findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) v.findViewById(R.id.result_text_Address);
        TextView resultTextCompany = (TextView) v.findViewById(R.id.result_text_Company);
        TextView resultTextTitle = (TextView) v.findViewById(R.id.result_text_Title);

        final String contactResult = qrCodeString;

     /*   final String firstname ;
        final String lastname ;
        final String email ;
        final String phone ;
        final String address ;
        final String company ;
        final String title ; */


        // resultTextContact.setText(vCard);


        Pattern pattern = Pattern.compile("((\\n|;|:)(FN:|N:|EMAIL;WORK;INTERNET:|TEL;CELL:|TITLE:|ADR:;;|ORG:)[0-9a-zA-Z-\\säöüÄÖÜß,]*(\\n|;))");

        Matcher m = pattern.matcher(contactResult);

        /* ***************************************************************************************************/

        String[] content = contactResult.substring(contactResult.indexOf(":|| ;||;;;||\n") + 1).split("\n");
        int name_id = 0;
        int lastname_id = 0;
        int email_id = 0;
        int phone_id = 0;
        int address_id = 0;
        int company_id = 0;
        int title_id = 0;

        for (int i = 0; i < content.length; i++) {
            if (content[i].startsWith("FN:")) name_id = i;
            if (content[i].startsWith("N:")) lastname_id = i;
            if (content[i].startsWith("EMAIL;WORK;INTERNET:")) email_id = i;
            if (content[i].startsWith("TEL;CELL:")) phone_id = i;
            if (content[i].startsWith("ADR:;;")) address_id = i;
            if (content[i].startsWith("ORG:")) company_id = i;
            if (content[i].startsWith("TITLE:")) title_id = i;
        }

        // TODO this must be reworked .. this way of parsing just leads to unexpected crashes.
        name = content[name_id].substring(3);
        lastname = content[lastname_id].substring(2);
        email = content[email_id].substring(20);
        phone = content[phone_id].substring(9);
        address = content[address_id].substring(6);
        company = content[company_id].substring(4);
        title = content[title_id].substring(6);


        resultTextName.setText("Name: " + name);
        resultTextLastname.setText("Lastname: " + lastname);
        resultTextEmail.setText("Email: " + email);
        resultTextPhone.setText("Tel: " + phone);
        resultTextAddress.setText("Address: " + address);
        resultTextCompany.setText("Company: " + company);
        resultTextTitle.setText("Title: " + title);

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
                                contact.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                                contact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title);

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
