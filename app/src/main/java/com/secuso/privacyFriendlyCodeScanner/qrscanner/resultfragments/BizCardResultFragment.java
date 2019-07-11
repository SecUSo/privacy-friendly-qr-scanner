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
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.BizCardActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ScannerActivity;

public class BizCardResultFragment extends ResultFragment {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String company;
    private String title;

    public BizCardResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        TextView resultTextName = (TextView) v.findViewById(R.id.result_text_Name);
        TextView resultTextEmail = (TextView) v.findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) v.findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) v.findViewById(R.id.result_text_Address);
        TextView resultTextCompany = (TextView) v.findViewById(R.id.result_text_Company);
        TextView resultTextTitle = (TextView) v.findViewById(R.id.result_text_Title);

        final String contactResult = qrCodeString;

        String[] content = contactResult.substring(contactResult.indexOf(":") + 1).split(";");
        int name_id = 0;
        int email_id = 0;
        int phone_id = 0;
        int address_id = 0;
        int company_id = 0;
        int title_id = 0;

//BIZCARD:N:Sean;X:Owen;T:Software Engineer;C:Google;A:76 9th Avenue, New York, NY 10011;B:+12125551212;E:srowen@google.com;;

        for(int i=0; i < content.length; i++) {
            if(content[i].startsWith("N:")) name_id = i;
            if(content[i].startsWith("E:")) email_id = i;
            if(content[i].startsWith("B:")) phone_id = i;
            if(content[i].startsWith("A:")) address_id = i;
            if(content[i].startsWith("C:")) company_id = i;
            if(content[i].startsWith("T:")) title_id = i;
        }

        name = content[name_id].substring(2);
        email = content[email_id].substring(2);
        phone = content[phone_id].substring(2);
        address = content[address_id].substring(2);
        company = content[company_id].substring(2);
        title = content[title_id].substring(2);

        resultTextName.setText("Name: " + name);
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
                                contact.putExtra(ContactsContract.Intents.Insert.EMAIL,email);
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
