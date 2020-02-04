package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

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

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class EmailResultFragment extends ResultFragment {

    EmailAddressParsedResult result;

    private String address;
    private String subject;
    private String message;

    public EmailResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_send_email, container, false);

        result = (EmailAddressParsedResult) parsedResult;

        address = result.getTos()[0];
        subject = result.getSubject();
        message = result.getBody();

        TextView resultFieldAddress = (TextView) v.findViewById(R.id.textResultSendEmailAdress);
        resultFieldAddress.setText(address);
        TextView resultFieldSubject = (TextView) v.findViewById(R.id.textResultSendEmailSubject);
        resultFieldSubject.setText(subject);
        TextView resultField = (TextView) v.findViewById(R.id.textResultSendEmail);
        resultField.setText(message);

        return v;
    }

    @Override
    public void onProceedPressed(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.send_email_array, (dialog, which) -> {
                    String caption = "";
                    switch (which) {
                        case 0:
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.setType("plain/text");
                            String[] recipient = new String[] {address};
                            email.putExtra(Intent.EXTRA_EMAIL, recipient);
                            email.putExtra(Intent.EXTRA_SUBJECT, subject);
                            email.putExtra(Intent.EXTRA_TEXT, message);
                            caption =getResources().getStringArray(R.array.send_email_array)[0];
                            startActivity(Intent.createChooser(email, caption));
                            break;
                        case 1:
                            final Uri mailUri = Uri.fromParts("mailto", address, null);
                            final Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, mailUri);
                            caption = getResources().getStringArray(R.array.send_email_array)[1];
                            startActivity(Intent.createChooser(contact, caption));
                            break;
                        default:
                    }
                });
        builder.create().show();
    }


}
