package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.History;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.MmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.SmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.TextActivity;


public class QrGenerator extends AppCompatActivity {


    ListView listView;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list_view);

        String[] generator = getResources().getStringArray(R.array.generator_array);
        //Row layout defined by Android: android.R.layout.simple_list_item_1
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, generator));
       // listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent appInfo = new Intent(QrGenerator.this, MailEnterActivity.class);
                    startActivity(appInfo);
                }
                if(position==1){
                    Intent english=new Intent(QrGenerator.this,UrlEnterActivity.class);
                    startActivity(english);
                }
                if(position==2){
                    Intent english=new Intent(QrGenerator.this,TelEnterActivity.class);
                    startActivity(english);
                }
                if(position==3){
                    Intent english=new Intent(QrGenerator.this,SmsEnterActivity.class);
                    startActivity(english);
                }

                if(position==4){
                    Intent english=new Intent(QrGenerator.this,GeoLocationEnterActivity.class);
                    startActivity(english);
                }

                if(position==5){
                    Intent english=new Intent(QrGenerator.this,MeCardEnterActivity.class);
                    startActivity(english);
                }

                if(position==6){
                    Intent english=new Intent(QrGenerator.this,BizCardEnterActivity.class);
                    startActivity(english);
                }
                if(position==7){
                    Intent english=new Intent(QrGenerator.this,MmsEnterActivity.class);
                    startActivity(english);
                }
                if(position==8){
                    Intent english=new Intent(QrGenerator.this,WifiEnterActivity.class);
                    startActivity(english);
                }


            }
        });

    }



}

