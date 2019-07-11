package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import static com.secuso.privacyFriendlyCodeScanner.qrscanner.R.string.content_copied;

public class EmailActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_email);

        TextView resultField = (TextView) findViewById(R.id.result_field_email);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnProceed = (Button) findViewById(R.id.btnProceed);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        String email = QRData.getString("Rst");
        final String eAddress=email.substring(7);
       // final String eAddress=email;
        resultField.setText(eAddress);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.send_email_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String caption = "";
                                switch (which) {
                                    case 0:

                                        Intent email = new Intent(Intent.ACTION_SEND);
                                        email.setType("plain/text");
                                       String[] recipient = new String[]{eAddress};
                                        email.putExtra(Intent.EXTRA_EMAIL, recipient);
                                       email.putExtra(Intent.EXTRA_SUBJECT, "");
                                       email.putExtra(Intent.EXTRA_TEXT, "");
                                        caption = getResources().getStringArray(R.array.send_email_array)[0];
                                        startActivity(Intent.createChooser(email, caption));
                                        break;
                                    case 1:

                                        //Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,Uri.parse(s));
                                        final Uri mailUri = Uri.fromParts("mailto", eAddress, null);
                                        final Intent contact = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, mailUri);
                                        caption = getResources().getStringArray(R.array.send_email_array)[1];
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
        final String qrurl = QRData.getString("Rst");

        switch (item.getItemId()){
            case R.id.share:
                shareIt(qrurl);
                return true;
            case R.id.copy:
                clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                clipData= ClipData.newPlainText("Text",qrurl);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),content_copied,Toast.LENGTH_LONG).show();
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
