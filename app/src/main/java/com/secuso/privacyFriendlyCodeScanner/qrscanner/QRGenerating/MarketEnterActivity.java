package com.secuso.privacyfriendlycodescanner.qrscanner.qrgenerating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class MarketEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_enter);

        final EditText qrMarket=(EditText) findViewById(R.id.editText);
        Button generate=(Button) findViewById(R.id.generate);

        int maxLength = 75;
        qrMarket.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {





                result = qrMarket.getText().toString();
                Intent i = new Intent(MarketEnterActivity.this, MarketGnrActivity.class);
                i.putExtra("gn", result);
                startActivity(i);



            }

        });
    }
}