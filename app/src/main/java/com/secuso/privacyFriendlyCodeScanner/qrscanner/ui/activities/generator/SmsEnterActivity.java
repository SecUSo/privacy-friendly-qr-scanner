package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;

public class SmsEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_enter);

        final EditText qrSms=(EditText) findViewById(R.id.editTel);
        final EditText qrText=(EditText) findViewById(R.id.editText1);
        Button generate=(Button) findViewById(R.id.generate);

        int maxLength = 15;
        qrSms.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        int maxLength2 = 300;
        qrText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength2)});


        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {
                result = qrSms.getText().toString()+":"+qrText.getText().toString();
                Intent i = new Intent(SmsEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.SMS);
                startActivity(i);
            }
        });
    }
}
