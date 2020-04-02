package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.ContactResultAdapter;

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

    public void onProceedPressed(Context context) {
        // TODO: Rework this...
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
