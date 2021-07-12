package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.ContactResultAdapter;

import java.util.ArrayList;

/**
 * Fragment to display the contact data. Most of the work is done in {@link ContactResultAdapter}
 *
 * @author Christopher Beckmann
 */
public class ContactResultFragment extends ResultFragment {

    AddressBookParsedResult result;

    RecyclerView resultList;

    public ContactResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        result = (AddressBookParsedResult) parsedResult;

        View v = inflater.inflate(R.layout.fragment_result_contact, container, false);

        resultList = v.findViewById(R.id.fragment_result_recycler_view);
        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setAdapter(new ContactResultAdapter(result));

        return v;
    }

    // TODO: Rework this...
    //TODO: Add missing: multiple names, all nicknames, pronunciation, instant messenger, birthday, geo
    //See: zxing/core/src/main/java/com/google/zxing/client/result/AddressBookParsedResult.java
    public void onProceedPressed(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.vcard_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent contact = new Intent(ContactsContract.Intents.Insert.ACTION);
                            contact.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                            contact.putExtra(ContactsContract.Intents.Insert.NAME, result.getNames()[0]);
                            contact.putExtra(ContactsContract.Intents.Insert.COMPANY, result.getOrg());
                            contact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, result.getTitle());
                            contact.putExtra(ContactsContract.Intents.Insert.NOTES, result.getNote());

                            //TODO: Improve the implementation of addressTypes,.... Google has some predefined values which we should map to for standard values ("home", "work", "")
                            //TODO: Maybe even use this class to store the information: ContactsContract.CommonDataKinds.StructuredPostal
                            if(result.getPhoneNumbers()!=null) for(String phoneNumber : result.getPhoneNumbers()) {
                                contact.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
                            }

                            if(result.getEmails()!=null) for(String email : result.getEmails()) {
                                contact.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                            }
                            if(result.getEmailTypes()!=null) for(String emailType : result.getEmailTypes()) {
                                contact.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, emailType);
                            }

                            if(result.getAddresses()!=null) for(String address :  result.getAddresses()){
                                contact.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                            }
                            if(result.getAddressTypes()!=null) for(String addressType :  result.getAddressTypes()){
                                if (addressType != null && !addressType.equals(""))
                                    contact.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, addressType);
                            }


                            // ---------- Add values not supported directly by the ContactsContract insert intent
                            // Defines an array list to contain the ContentValues objects for each row
                            ArrayList<ContentValues> contactData = new ArrayList<>();
                            // Sets up the row as a ContentValues object
                            ContentValues urlRow = new ContentValues();

                            // Specifies the MIME type for this data row (all data rows must be marked by their type)
                            urlRow.put(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                            );

                            // Adds the email address and its type to the row
                            if(result.getURLs()!=null) for(String url : result.getURLs()) {
                                urlRow.put(ContactsContract.CommonDataKinds.Website.URL, url);
                            }
                            // Adds the row to the array
                            contactData.add(urlRow);
                            contact.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                            String caption = getResources().getStringArray(R.array.vcard_array)[0];
                            startActivity(Intent.createChooser(contact, caption));
                            break;

                        default:
                    }
                });
        builder.create().show();
    }
}
