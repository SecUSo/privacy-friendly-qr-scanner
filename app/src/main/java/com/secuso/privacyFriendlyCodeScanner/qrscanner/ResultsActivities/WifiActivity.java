package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class WifiActivity extends AppCompatActivity {
    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_wifi);


        TextView resultText = (TextView) findViewById(R.id.result_field_wifi);
        Button btnProceed = (Button) findViewById(R.id.btnProceed);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String wifiResult = QRData.getString("Rst");


        String[] content = wifiResult.substring(wifiResult.indexOf(":") + 1).split(";");
        int ssid_id = 0;
        int encryption_id = 0;
        int pw_id = 0;
        for(int i=0; i < content.length; i++) {
            if(content[i].startsWith("S:")) ssid_id = i;
            if(content[i].startsWith("T:")) encryption_id = i;
            if(content[i].startsWith("P:")) pw_id = i;
        }

        final String ssid = content[ssid_id].substring(2);
        String encryption = content[encryption_id].substring(2);
        final String pw = content[pw_id].substring(2);

        TextView resultField = (TextView) findViewById(R.id.result_field_wifi);
        TextView resultFieldEncryption = (TextView) findViewById(R.id.result_field_wifi_encryption);
        TextView resultFieldPassword = (TextView) findViewById(R.id.result_field_wifi_pw);
        resultField.setText("SSID: " + ssid);
        resultFieldEncryption.setText(getString(R.string.encryption) + ": " + encryption);
        resultFieldPassword.setText("PW: " + pw);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(WifiActivity.this, ScannerActivity.class);
                startActivity(ca);

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.wifi_array, new DialogInterface.OnClickListener() {
                            @Override
                            @SuppressWarnings("deprecation")
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setText(pw);

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
                clipData=ClipData.newPlainText("Text",qrurl);
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