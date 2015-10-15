package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.AlertDialog;
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

import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SendEmailFragment extends ResultFragment {
    private static boolean checked = false;
    private static boolean trust = false;

    public SendEmailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_email, container, false);

        rootView = super.setResult(rootView);

        final String email = result;

        int subjectPos = 0;
        int messagePos = 0;

        for(int i=10;i<email.length();i++) {
            if(email.substring(i).startsWith(";SUB:"))
                subjectPos = i + 5;
        }

        for(int i=10;i<email.length();i++) {
            if(email.substring(i).startsWith(";BODY:"))
                messagePos = i + 6;
        }

        final String address = email.substring(10,subjectPos-5);
        final String subject = email.substring(subjectPos,messagePos-6);
        final String message = email.substring(messagePos,email.length()-2);

        TextView resultFieldAddress = (TextView) rootView.findViewById(R.id.textResultSendEmailAdress);
        resultFieldAddress.setText(address);
        TextView resultFieldSubject = (TextView) rootView.findViewById(R.id.textResultSendEmailSubject);
        resultFieldSubject.setText(subject);
        TextView resultField = (TextView) rootView.findViewById(R.id.textResultSendEmail);
        resultField.setText(message);

        Button chooseActionButton = (Button) rootView.findViewById(R.id.btnProceed);
        chooseActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.send_email_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String caption = "";
                                switch (which) {
                                    case 0:
                                        saveScanned(true);
                                        Intent email = new Intent(Intent.ACTION_SEND);
                                        email.setType("plain/text");
                                        String[] recipient = new String[] {address};
                                        email.putExtra(Intent.EXTRA_EMAIL, recipient);
                                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                                        email.putExtra(Intent.EXTRA_TEXT, message);
                                        caption = getActivity().getResources().getStringArray(R.array.send_email_array)[0];
                                        startActivity(Intent.createChooser(email, caption));
                                        break;
                                    case 1:
                                        saveScanned(true);
                                        Intent contact = new Intent(
                                                ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                                                Uri.parse(result));
                                        caption = getActivity().getResources().getStringArray(R.array.send_email_array)[1];
                                        startActivity(Intent.createChooser(contact, caption));
                                        break;
                                    case 2:
                                        saveScanned(true);
                                        break;
                                    default:
                                }
                            }
                        });
                builder.create().show();
            }
        });

        return rootView;
    }
}