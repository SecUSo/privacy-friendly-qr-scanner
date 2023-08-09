package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.ContactResultAdapter;
import com.secuso.privacyfriendlycodescanner.qrscanner.util.ContactUtil;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        result = (AddressBookParsedResult) parsedResult;

        View v = inflater.inflate(R.layout.fragment_result_contact, container, false);

        resultList = v.findViewById(R.id.fragment_result_recycler_view);
        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setAdapter(new ContactResultAdapter(result));

        return v;
    }

    //TODO: Add missing: multiple names, all nicknames, pronunciation, instant messenger, birthday, geo
    //See: zxing/core/src/main/java/com/google/zxing/client/result/AddressBookParsedResult.java
    public void onProceedPressed(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(R.array.vcard_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent contact = new Intent(ContactsContract.Intents.Insert.ACTION);
                            contact.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                            contact.putExtra(ContactsContract.Intents.Insert.NAME, result.getNames()[0]);
                            contact.putExtra(ContactsContract.Intents.Insert.COMPANY, result.getOrg());
                            contact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, result.getTitle());
                            contact.putExtra(ContactsContract.Intents.Insert.NOTES, result.getNote());

                            ArrayList<ContentValues> contactData = new ArrayList<>();

                            // add phone numbers
                            contactData.addAll(ContactUtil.buildPhoneValues(result.getPhoneNumbers(), result.getPhoneTypes()));

                            // add emails
                            contactData.addAll(ContactUtil.buildEmailValues(result.getEmails(), result.getEmailTypes()));

                            // add addresses
                            contactData.addAll(ContactUtil.buildAddressValues(result.getAddresses(), result.getAddressTypes()));

                            // add websites
                            contactData.addAll(ContactUtil.buildWebsiteValues(result.getURLs()));

                            contact.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                            String caption = getResources().getStringArray(R.array.vcard_array)[0];
                            startActivity(Intent.createChooser(contact, caption));
                            break;

                        default:
                    }
                })
                .show();
    }
}
