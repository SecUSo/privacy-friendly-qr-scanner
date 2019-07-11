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

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ScannerActivity;

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

public class ContactActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_contact);

        TextView resultTextName = (TextView) findViewById(R.id.result_text_contact);
        TextView resultTextLastname = (TextView) findViewById(R.id.result_text_lastname);
        TextView resultTextEmail = (TextView) findViewById(R.id.result_text_Email);
        TextView resultTextPhone = (TextView) findViewById(R.id.result_text_Phone);
        TextView resultTextAddress = (TextView) findViewById(R.id.result_text_Address);
        TextView resultTextCompany = (TextView) findViewById(R.id.result_text_Company);
        TextView resultTextTitle = (TextView) findViewById(R.id.result_text_Title);

        Button btnProceed = (Button) findViewById(R.id.btnProceed);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String contactResult = QRData.getString("Rst");

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


        String compan = null;
        for (int i = 0; i < content.length; i++) {
            if (content[i].startsWith("FN:")) name_id = i;
            if (content[i].startsWith("N:")) lastname_id = i;
            if (content[i].startsWith("EMAIL;WORK;INTERNET:")) email_id = i;
            if (content[i].startsWith("TEL;CELL:")) phone_id = i;
            if (content[i].startsWith("ADR:;;")) address_id = i;
            if (content[i].startsWith("ORG:")) company_id = i;
            if (content[i].startsWith("TITLE:")) title_id = i;
        }

        final String name = content[name_id].substring(3);
        final String lastname = content[lastname_id].substring(2);
        final String email = content[email_id].substring(20);
        final String phone = content[phone_id].substring(9);
        final String address = content[address_id].substring(6);
        final String company = content[company_id].substring(4);
        final String title = content[title_id].substring(6);


        resultTextName.setText("Name: " + name);
        //resultTextLastname.setText("Lastname: " + lastname);
        resultTextEmail.setText("Email: " + email);
        resultTextPhone.setText("Tel: " + phone);
        resultTextAddress.setText("Address: " + address);
        resultTextCompany.setText("Company: " + company);
        resultTextTitle.setText("Title: " + title);


       /* ***************************************************************************************************/


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(ContactActivity.this, ScannerActivity.class);
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
        });

    }

    private Uri createVCard() throws IOException {


        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String contactResult = QRData.getString("Rst");

        File f = new File(this.getExternalFilesDir(null),"contact.vcf");
        FileWriter fw;
        fw = new FileWriter(f);
        Uri uri = null;
        try (FileOutputStream fop = new FileOutputStream(f)) {

            if (f.exists()) {

                fop.write(contactResult.getBytes());
                //Now read the content of the vCard after writing data into it
                BufferedReader br = null;
                String sCurrentLine;
                br = new BufferedReader(new FileReader("contact.vcf"));

                //close the output stream and buffer reader
                fop.flush();
                fop.close();
                System.out.println("The data has been written");
                Log.e("TESTEST", f.getPath());

                uri = FileProvider.getUriForFile(context, "com.secuso.privacyFriendlyCodeScanner", f);
                grantUriPermission("com.google.android.contacts", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                grantUriPermission("com.android.contacts", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
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
        final String qrurl = QRData.getString("Rst");

        switch (item.getItemId()){
            case R.id.share:
                shareIt(qrurl);
                return true;

            case R.id.copy:
                clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                clipData= ClipData.newPlainText("Text",qrurl);
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

}

