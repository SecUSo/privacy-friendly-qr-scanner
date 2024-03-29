package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorKeyboardListener;

public class SmsEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_enter);

        final EditText phoneNumber = (EditText) findViewById(R.id.editPhone);
        final EditText smsContent = (EditText) findViewById(R.id.editSMSContent);

        int maxLength = 15;
        phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        int maxLength2 = 600;
        smsContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength2)});

        ExtendedFloatingActionButton generate = (ExtendedFloatingActionButton) findViewById(R.id.btnGenerate);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        GeneratorKeyboardListener listener = new GeneratorKeyboardListener(rootView, generate, R.id.btnGenerate, getApplicationContext().getResources().getDisplayMetrics().densityDpi);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

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
