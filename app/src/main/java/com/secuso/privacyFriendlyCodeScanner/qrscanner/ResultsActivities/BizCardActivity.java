package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ScannerActivity;

import static com.secuso.privacyFriendlyCodeScanner.qrscanner.R.string.content_copied;

public class BizCardActivity extends AppCompatActivity {
    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_biz_card);

        TextView resultTextName = (TextView) findViewById(R.id.result_text_Name);
        TextView resultTextEmail = (TextView) findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) findViewById(R.id.result_text_Address);
        TextView resultTextCompany = (TextView) findViewById(R.id.result_text_Company);
        TextView resultTextTitle = (TextView) findViewById(R.id.result_text_Title);

        Button btnProceed = (Button) findViewById(R.id.btnProceed);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String contactResult = QRData.getString("Rst");


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

        final String name = content[name_id].substring(2);
        final String email = content[email_id].substring(2);
        final String phone = content[phone_id].substring(2);
        final String address = content[address_id].substring(2);
        final String company = content[company_id].substring(2);
        final String title = content[title_id].substring(2);


        resultTextName.setText("Name: " + name);
        resultTextEmail.setText("Email: " + email);
        resultTextPhone.setText("Tel: " + phone);
        resultTextAddress.setText("Address: " + address);
        resultTextCompany.setText("Company: " + company);
        resultTextTitle.setText("Title: " + title);







        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(BizCardActivity.this, ScannerActivity.class);
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

        final String name = content[name_id].substring(2);
        final String email = content[email_id].substring(2);
        final String phone = content[phone_id].substring(2);
        final String address = content[address_id].substring(2);
        final String company = content[company_id].substring(2);
        final String title = content[title_id].substring(2);



        final String ss="name:"+name +"; phone nummber:"+phone+"; Address"+address+"; title: "+title+"; E-mail:"+email+"; Company:"+company;

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
