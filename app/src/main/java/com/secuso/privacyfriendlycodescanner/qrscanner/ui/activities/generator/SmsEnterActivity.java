package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;

public class SmsEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_enter);

        final EditText phoneNumber = (EditText) findViewById(R.id.editPhone);
        final EditText smsContent = (EditText) findViewById(R.id.editSMSContent);
        Button generate = (Button) findViewById(R.id.btnGenerate);

        int maxLength = 15;
        phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = phoneNumber.getText().toString() + ":" + smsContent.getText().toString();
                Intent i = new Intent(SmsEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.SMS);
                startActivity(i);
            }
        });
    }
}
