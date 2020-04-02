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

public class VcardEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcard_enter);
        final EditText qrName=(EditText) findViewById(R.id.editName);
        final EditText qrLastname=(EditText) findViewById(R.id.editLastname);
        final EditText qrPhone=(EditText) findViewById(R.id.editPhone);
        final EditText qrMail=(EditText) findViewById(R.id.editMail);
        final EditText qrStreet=(EditText) findViewById(R.id.editStreet);
        final EditText qrCity=(EditText) findViewById(R.id.editCity);
        final EditText qrPostal=(EditText) findViewById(R.id.editPostal);
        final EditText qrTitle=(EditText) findViewById(R.id.editTilte);
        final EditText qrCompany=(EditText) findViewById(R.id.editCompany);


        int maxLength = 75;
        qrName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        int maxLength2 = 75;
        qrPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength2)});

        int maxLength3 = 75;
        qrMail.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength3)});

        int maxLength4 = 75;
        qrStreet.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength4)});

        int maxLength5 = 75;
        qrCity.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength5)});

        int maxLength6 = 75;
        qrPostal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength6)});

        int maxLength7 = 75;
        qrTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength7)});

        int maxLength8 = 75;
        qrTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength8)});


        Button generate=(Button) findViewById(R.id.generate);


        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {


                result = "N:"+qrLastname.getText().toString()+"\n"+"FN:"+qrName.getText().toString()+"\n"+"ORG:"+qrCompany.getText().toString()+"\n"+"TITLE:"+qrTitle.getText().toString()+"\n"+"ADR:;;"+qrStreet.getText().toString()+";"+qrCity.getText().toString()+"\n"+qrPostal.getText().toString()+"\n"+"TEL;CELL:"+qrPhone.getText().toString()+"\n"+"EMAIL;WORK;INTERNET:"+qrMail.getText().toString()+"\n"+"END:VCARD";
                Intent i = new Intent(VcardEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.V_Card);
                startActivity(i);

            }

        });
    }
}
