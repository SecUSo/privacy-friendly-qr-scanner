package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class TextActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);


        TextView resultText = (TextView) findViewById(R.id.result_field_text);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnProceed = (Button) findViewById(R.id.btnProceed);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String text = QRData.getString("Rst");
        resultText.setText(text);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(TextActivity.this, ScannerActivity.class);
                startActivity(ca);

            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.text_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        Uri uri = Uri.parse("http://www.google.com/#q="+text);
                                        Intent search = new Intent(Intent.ACTION_VIEW, uri);
                                        String caption = getResources().getStringArray(R.array.text_array)[0];
                                        startActivity(Intent.createChooser(search, caption));
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
