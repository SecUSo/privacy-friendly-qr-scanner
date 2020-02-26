package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;


public class QrGeneratorOverviewActivity extends AppCompatActivity {
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
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(QrGeneratorOverviewActivity.this, MailEnterActivity.class);
                        break;
                    case 1:
                        intent = new Intent(QrGeneratorOverviewActivity.this,UrlEnterActivity.class);
                        break;
                    case 2:
                        intent = new Intent(QrGeneratorOverviewActivity.this,TelEnterActivity.class);
                        break;
                    case 3:
                        intent = new Intent(QrGeneratorOverviewActivity.this,SmsEnterActivity.class);
                        break;
                    case 4:
                        intent = new Intent(QrGeneratorOverviewActivity.this,GeoLocationEnterActivity.class);
                        break;
                    case 5:
                        intent = new Intent(QrGeneratorOverviewActivity.this,MeCardEnterActivity.class);
                        break;
                    case 6:
                        intent = new Intent(QrGeneratorOverviewActivity.this,BizCardEnterActivity.class);
                        break;
                    case 7:
                        intent = new Intent(QrGeneratorOverviewActivity.this,MmsEnterActivity.class);
                        break;
                    case 8:
                        intent = new Intent(QrGeneratorOverviewActivity.this,WifiEnterActivity.class);
                        break;
                    case 9:
                        intent = new Intent(QrGeneratorOverviewActivity.this,VcardEnterActivity.class);
                        break;
                    case 10:
                        intent = new Intent(QrGeneratorOverviewActivity.this,MarketEnterActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }



}

