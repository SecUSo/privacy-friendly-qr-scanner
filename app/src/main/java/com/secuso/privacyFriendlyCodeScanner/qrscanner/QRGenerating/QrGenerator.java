package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;


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
                    Intent intent = new Intent(QrGenerator.this, MailEnterActivity.class);
                    startActivity(intent);
                }
                if(position==1){
                    Intent intent=new Intent(QrGenerator.this,UrlEnterActivity.class);
                    startActivity(intent);
                }
                if(position==2){
                    Intent intent=new Intent(QrGenerator.this,TelEnterActivity.class);
                    startActivity(intent);
                }
                if(position==3){
                    Intent intent=new Intent(QrGenerator.this,SmsEnterActivity.class);
                    startActivity(intent);
                }

                if(position==4){
                    Intent intent=new Intent(QrGenerator.this,GeoLocationEnterActivity.class);
                    startActivity(intent);
                }

                if(position==5){
                    Intent intent=new Intent(QrGenerator.this,MeCardEnterActivity.class);
                    startActivity(intent);
                }

                if(position==6){
                    Intent intent=new Intent(QrGenerator.this,BizCardEnterActivity.class);
                    startActivity(intent);
                }
                if(position==7){
                    Intent intent=new Intent(QrGenerator.this,MmsEnterActivity.class);
                    startActivity(intent);
                }
                if(position==8){
                    Intent intent=new Intent(QrGenerator.this,WifiEnterActivity.class);
                    startActivity(intent);
                }
                if(position==9){
                    Intent intent=new Intent(QrGenerator.this,VcardEnterActivity.class);
                    startActivity(intent);
                }

                if(position==10){
                    Intent intent=new Intent(QrGenerator.this,MarketEnterActivity.class);
                    startActivity(intent);
                }



            }
        });

    }



}

