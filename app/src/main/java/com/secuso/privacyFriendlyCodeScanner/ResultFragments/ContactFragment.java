package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactFragment extends ResultFragment {
    private static boolean checked = false;
    private static boolean trust = false;

    public ContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        rootView = super.setResult(rootView);

        TextView resultTextContact = (TextView) rootView.findViewById(R.id.result_text_contact);

        Pattern pattern = Pattern.compile("((\\n|;|:)(FN:|N:)[0-9a-zA-Z-\\säöüÄÖÜß,]*(\\n|;))");

        Matcher m = pattern.matcher(result);

        String name = "";

        if(m.find())  {
            name = m.group(1).substring(1);

            if(name.startsWith("N:"))
                resultTextContact.setText("Name: " + name.substring(2).replace(';', ' '));
            else if(name.startsWith("FN:"))
                resultTextContact.setText("Name: " + name.substring(3).replace(';', ' '));
        }
        Button chooseActionButton = (Button) rootView.findViewById(R.id.btnProceed);
        chooseActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.vcard_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        saveScanned(true);
                                        Uri uri = createVCard();
                                        Intent contact = new Intent();
                                        contact.setAction(Intent.ACTION_VIEW);
                                        contact.setType("text/x-vcard");
                                        contact.setData(uri);
                                        String caption = getActivity().getResources().getStringArray(R.array.vcard_array)[0];
                                        startActivity(Intent.createChooser(contact, caption));
                                        break;
                                    case 1:
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
    private Uri createVCard() {
        try {
            File sdcard = getActivity().getFilesDir();
            File file = new File(sdcard,"vCard.vcf");


            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, false)));

            out.append(result);
            out.flush();
            out.close();

            Uri uri = FileProvider.getUriForFile(getActivity(), "com.secuso.privacyFriendlyCodeScanner", file);
            getActivity().grantUriPermission("com.android.contacts", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            return uri;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

}