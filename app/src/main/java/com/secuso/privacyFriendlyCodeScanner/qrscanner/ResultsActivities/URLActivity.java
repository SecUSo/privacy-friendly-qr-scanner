package com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ScannerActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.secuso.privacyFriendlyCodeScanner.qrscanner.R.string.content_copied;

public class URLActivity extends AppCompatActivity {
    ClipboardManager clipboardManager;
    ClipData clipData;
    private static boolean checked = false;
    private static boolean trust = false;
    final Context context = this;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share,menu);
        getMenuInflater().inflate(R.menu.copy,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        Bundle QRData = getIntent().getExtras();//from ResultActivity
        final String qrurl = QRData.getString("Rst");
        final String qrurl2,qrurl3;

        Button chooseActionButton = (Button) findViewById(R.id.btnProceed);
        chooseActionButton.setText(R.string.choose_action_url);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setText(R.string.abort_action_url);



        TextView resultText = (TextView) findViewById(R.id.textDomain);
        TextView furtherInfo = (TextView) findViewById(R.id.textLink);
        furtherInfo.setMovementMethod(LinkMovementMethod.getInstance());

        qrurl2 = qrurl.replaceAll("\n", "");
        String domain = qrurl2;



        domain = domain.split("\n")[0];
        if(!domain.endsWith("/")) domain = domain + '/';

        Pattern pattern = Pattern.compile("([0-9a-zA-ZäöüÄÖÜß-]*.(co.uk|com.de|de.com|co.at|[a-z]{2,})/)");

        Matcher m = pattern.matcher(domain);
        if(m.find()) domain = m.group(1);

        if(domain.endsWith("/")) domain = domain.substring(0, domain.length()-1);

        int start = qrurl.indexOf(domain);
        int end = start + domain.length();

        Spannable WordtoSpan = new SpannableString(qrurl);
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        resultText.setText(WordtoSpan);

       // checked = trust = getBoolean("trust", false);

        final CheckBox knowDomain = (CheckBox) findViewById(R.id.checkBoxKnowRisks);

        // wenn bereits vertraut wurde, checkbox setzen
        if(trust)
            knowDomain.setChecked(true);

        knowDomain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (knowDomain.isChecked())
                {
                    checked = true;
                }
                else {
                    checked = false;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ca = new Intent(URLActivity.this, ScannerActivity.class);
                startActivity(ca);

            }
        });

        chooseActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!checked) {

                    Toast.makeText(context,R.string.conform_url,Toast.LENGTH_LONG).show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.choose_action)
                            .setItems(R.array.url_array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String caption = "";
                                    switch (which) {
                                        case 0:

                                            String qrurl3="";
                                           if(!qrurl.startsWith("http://") && !qrurl.startsWith("https://"))
                                           {
                                               qrurl3 = "http://" + qrurl;

                                            Intent url = new Intent(Intent.ACTION_VIEW);/// !!!!
                                            url.setData(Uri.parse(qrurl3));
                                            caption = getResources().getStringArray(R.array.url_array)[0];
                                            startActivity(Intent.createChooser(url, caption));}
                                            else {
                                               Intent url = new Intent(Intent.ACTION_VIEW);/// !!!!
                                               url.setData(Uri.parse(qrurl));
                                               caption = getResources().getStringArray(R.array.url_array)[0];
                                               startActivity(Intent.createChooser(url, caption));

                                           }
                                            break;


                                        default:
                                    }
                                }
                            });
                    builder.create().show();
                }
            }
        });
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


