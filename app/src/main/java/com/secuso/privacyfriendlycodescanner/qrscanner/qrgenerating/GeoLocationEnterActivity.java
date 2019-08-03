package com.secuso.privacyfriendlycodescanner.qrscanner.qrgenerating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class GeoLocationEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_enter);

        final EditText qrLatitude = (EditText) findViewById(R.id.editGeo1);
        final EditText qrLongitude = (EditText) findViewById(R.id.editGeo2);
        Button generate = (Button) findViewById(R.id.generate);

        int maxLength = 11;
        qrLatitude.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        int maxLength2 = 11;
        qrLongitude.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength2)});


        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {


                result = qrLatitude.getText().toString() + "," + qrLongitude.getText().toString();


                Intent i = new Intent(GeoLocationEnterActivity.this, GeoLocatioGnrActivity.class);
                i.putExtra("gn", result);
                startActivity(i);


            }

        });
    }
}
