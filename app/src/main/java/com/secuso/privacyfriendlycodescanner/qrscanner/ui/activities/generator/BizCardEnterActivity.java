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


        Button generate = (Button) findViewById(R.id.btnGenerate);

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                //BIZCARD:N:Sean;X:Owen;T:Software Engineer;C:Google;A:76 9th Avenue, New York, NY 10011;B:+12125551212;E:srowen@google.com;;

                result = "N:" + qrFirstname.getText().toString()
                        + ";X:" + qrLastname.getText().toString()
                        + ";T:" + qrTitle.getText().toString()
                        + ";C:" + qrCompany.getText().toString()
                        + ";A:" + qrStreet.getText().toString() + "," + qrCity.getText().toString() + "," + qrZipCode.getText().toString()
                        + ";B:" + qrPhone.getText().toString()
                        + ";E:" + qrMail.getText().toString()
                        + ";;";
                Intent i = new Intent(BizCardEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.BIZ_CARD);
                startActivity(i);
            }
        });
    }
}
