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
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorKeyboardListener;

public class BizCardEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_card_enter);

        final EditText qrFirstname = (EditText) findViewById(R.id.editFirstname);
        final EditText qrLastname = (EditText) findViewById(R.id.editLastname);
        final EditText qrPhone = (EditText) findViewById(R.id.editPhone);
        final EditText qrMail = (EditText) findViewById(R.id.editMail);
        final EditText qrStreet = (EditText) findViewById(R.id.editAddress);
        final EditText qrCity = (EditText) findViewById(R.id.editCity);
        final EditText qrZipCode = (EditText) findViewById(R.id.editZipCode);
        final EditText qrTitle = (EditText) findViewById(R.id.editTitle);
        final EditText qrCompany = (EditText) findViewById(R.id.editCompany);

        int maxLength = 75;
        qrFirstname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrLastname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrMail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrStreet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrCity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrZipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});


        ExtendedFloatingActionButton generate = (ExtendedFloatingActionButton) findViewById(R.id.btnGenerate);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        GeneratorKeyboardListener listener = new GeneratorKeyboardListener(rootView, generate, R.id.btnGenerate, getApplicationContext().getResources().getDisplayMetrics().densityDpi);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                //BIZCARD:N:Sean;X:Owen;T:Software Engineer;C:Google;A:76 9th Avenue, New York, NY 10011;B:+12125551212;E:srowen@google.com;;

                result = "N:" + QRGeneratorUtils.escapeQRPropertyValue(qrFirstname.getText().toString())
                        + ";X:" + QRGeneratorUtils.escapeQRPropertyValue(qrLastname.getText().toString())
                        + ";T:" + QRGeneratorUtils.escapeQRPropertyValue(qrTitle.getText().toString())
                        + ";C:" + QRGeneratorUtils.escapeQRPropertyValue(qrCompany.getText().toString())
                        + ";A:" + QRGeneratorUtils.escapeQRPropertyValue(qrStreet.getText().toString())
                        + (!qrStreet.getText().toString().isEmpty() && !qrCity.getText().toString().isEmpty() ? "," : "")
                        + QRGeneratorUtils.escapeQRPropertyValue(qrCity.getText().toString())
                        + ((!qrStreet.getText().toString().isEmpty() || !qrCity.getText().toString().isEmpty()) && !qrZipCode.getText().toString().isEmpty() ? "," : "")
                        + QRGeneratorUtils.escapeQRPropertyValue(qrZipCode.getText().toString())
                        + ";B:" + QRGeneratorUtils.escapeQRPropertyValue(qrPhone.getText().toString())
                        + ";E:" + QRGeneratorUtils.escapeQRPropertyValue(qrMail.getText().toString())
                        + ";;";
                Intent i = new Intent(BizCardEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.BIZ_CARD);
                startActivity(i);
            }
        });
    }
}
