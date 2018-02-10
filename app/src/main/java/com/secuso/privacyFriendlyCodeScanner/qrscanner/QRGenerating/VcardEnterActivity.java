package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

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


        Button generate=(Button) findViewById(R.id.generate);


        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {





                result = "N:"+qrLastname.getText().toString()+"\n"+"FN:"+qrName.getText().toString()+"\n"+"ORG:"+qrCompany.getText().toString()+"\n"+"TITLE:"+qrTitle.getText().toString()+"\n"+"ADR:;;"+qrStreet.getText().toString()+";"+qrCity.getText().toString()+"\n"+qrPostal.getText().toString()+"\n"+"TEL;CELL:"+qrPhone.getText().toString()+"\n"+"EMAIL;WORK;INTERNET:"+qrMail.getText().toString()+"\n"+"END:VCARD";
                Intent i = new Intent(VcardEnterActivity.this, VcardGnrActivity.class);
                i.putExtra("gn", result);
                startActivity(i);



            }

        });
    }
}
