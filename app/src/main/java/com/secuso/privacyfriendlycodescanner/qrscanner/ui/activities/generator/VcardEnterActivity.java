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

public class VcardEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcard_enter);
        final EditText qrFirstname = (EditText) findViewById(R.id.editFirstname);
        final EditText qrLastname = (EditText) findViewById(R.id.editLastname);
        final EditText qrPhone = (EditText) findViewById(R.id.editPhone);
        final EditText qrMail = (EditText) findViewById(R.id.editMail);
        final EditText qrAddress = (EditText) findViewById(R.id.editAddress);
        final EditText qrCity = (EditText) findViewById(R.id.editCity);
        final EditText qrZipCode = (EditText) findViewById(R.id.editZipCode);
        final EditText qrCountry = (EditText) findViewById(R.id.editCountry);
        final EditText qrTitle = (EditText) findViewById(R.id.editTitle);
        final EditText qrCompany = (EditText) findViewById(R.id.editCompany);


        int maxLength = 75;
        qrFirstname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrLastname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrMail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrAddress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrCity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrZipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrCountry.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        qrCompany.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});


        Button generate = (Button) findViewById(R.id.btnGenerate);


        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = "N:" + qrLastname.getText().toString() + ";" + qrFirstname.getText().toString() + "\n"
                        + "ORG:" + qrCompany.getText().toString() + "\n"
                        + "TITLE:" + qrTitle.getText().toString() + "\n"
                        + "ADR:;;" + qrAddress.getText().toString() + ";"
                        + qrCity.getText().toString() + ";"
                        + qrZipCode.getText().toString() + ";"
                        + qrCountry.getText().toString() + "\n"
                        + "TEL;CELL:" + qrPhone.getText().toString() + "\n"
                        + "EMAIL;WORK;INTERNET:" + qrMail.getText().toString() + "\n"
                        + "END:VCARD";
                Intent i = new Intent(VcardEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.V_Card);
                startActivity(i);
            }
        });
    }
}
