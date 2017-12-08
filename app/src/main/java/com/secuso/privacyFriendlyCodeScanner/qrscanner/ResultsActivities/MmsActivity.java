package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

public class MmsActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms);

        TextView numberField = (TextView) findViewById(R.id.textResultMMS);
        TextView contentField = (TextView) findViewById(R.id.textContentMMS);
        Button btnProceed = (Button) findViewById(R.id.btnProceed);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String smsResult = QRData.getString("Rst");

        String content = smsResult.substring(smsResult.indexOf(":") + 1);
        final String number = content.substring(0, content.indexOf(":"));
        final String message = content.substring(content.indexOf(":") + 1);

        numberField.setText(number);
        contentField.setText(message);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(MmsActivity.this, MainActivity.class);
                startActivity(ca);

            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.sms_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String caption = "";
                                switch (which) {
                                    case 0:
                                        Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("mmsto:" + number));
                                        sms.putExtra("address",  number);
                                        sms.putExtra("mms_body", message);
                                        caption = getResources().getStringArray(R.array.sms_array)[0];
                                        startActivity(Intent.createChooser(sms, caption));

                                        break;
                                    case 1:
                                        Intent call = new Intent("android.intent.action.DIAL");
                                        call.setData(Uri.parse("tel:" + number));
                                        caption = getResources().getStringArray(R.array.sms_array)[1];
                                        startActivity(Intent.createChooser(call, caption));

                                        break;
                                    case 2:
                                        Intent contact = new Intent(
                                                ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                                                Uri.parse("tel:" + number));
                                        caption = getResources().getStringArray(R.array.sms_array)[2];
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
}
