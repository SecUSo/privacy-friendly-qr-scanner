package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.secuso.privacyFriendlyCodeScanner.qrscanner.R.string.content_copied;

public class MeCardActivity extends AppCompatActivity {
    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_card);

        TextView resultTextContact = (TextView) findViewById(R.id.result_text_contact);
        Button btnProceed = (Button) findViewById(R.id.btnProceed);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String contactResult = QRData.getString("Rst");

        Pattern pattern = Pattern.compile("((\\n|;|:)(FN:|N:|TEL:|EMAIL: | EMAIL:|URL:|TEL: |NOTE:|ADR:|ORG:)[0-9a-zA-Z-\\säöüÄÖÜß,]*(\\n|;))");

        Matcher m = pattern.matcher(contactResult);

        String name = "";
        String te="";
        String mai="ooo";




        if (m.find()) {
            name = m.group(1).substring(1);




            if (name.startsWith("N:"))
                resultTextContact.setText("Name: " + name.substring(2).replace(';', ' '));

            else
                resultTextContact.setText(R.string.noname);
        }
        if (m.find()) {

            te=m.group(1).substring(1);
            if (te.startsWith("TEL:")){
                te=te;
            }

        }




        final String n=name.substring(2).replace(';', ' ');// get name from the string


        final String tel=te.substring(4).replace(';', ' ');


       // final String tel=between(contactResult,"TEL:",";EMAIL");
       final String mail=between(contactResult,"EMAIL:",";;");
        //final String title=between(contactResult,"T:",";C");
       // final String org=between(contactResult,"C:",";A");





        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(MeCardActivity.this, MainActivity.class);
                startActivity(ca);

            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.vcard_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:


                                        Intent contact = new Intent(ContactsContract.Intents.Insert.ACTION);
                                        contact.setType(ContactsContract.RawContacts.CONTENT_TYPE);


                                        contact.putExtra(ContactsContract.Intents.Insert.PHONE, tel);
                                        contact.putExtra(ContactsContract.Intents.Insert.NAME, n);
                                        contact.putExtra(ContactsContract.Intents.Insert.EMAIL,mail);
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
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share,menu);
        getMenuInflater().inflate(R.menu.copy,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String contactResult = QRData.getString("Rst");

        Pattern pattern = Pattern.compile("((\\n|;|:)(FN:|N:|TEL:|EMAIL:|URL:|TEL: |NOTE:|ADR:|ORG:)[0-9a-zA-Z-\\säöüÄÖÜß,]*(\\n|;))");

        Matcher m = pattern.matcher(contactResult);


        String name = "";


        if (m.find()) {
            name = m.group(1).substring(1);
        }

        final String n=name.substring(2).replace(';', ' ');// get name from the string

        final String tel=between(contactResult,"TEL:",";EMAIL");
        final String mail=between(contactResult,"EMAIL:",";;");
        final String adr=between(contactResult,"ADR:",";TEL");


        final String ss="name:"+n +"; phone nummber:"+tel+"; E-mail:"+mail+"; address:"+adr;

        switch (item.getItemId()){
            case R.id.share:
                shareIt(ss);
                return true;

            case R.id.copy:
                clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                clipData= ClipData.newPlainText("Text",ss);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), content_copied,Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void shareIt(String result)
    {
        Intent sharingIntent= new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,result);
        startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_via)));
    }
    static String between(String value, String a, String b) {
        // Return a substring between the two strings.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(b);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }
}
