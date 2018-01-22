package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.MeCardActivity;

public class MeCardEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_card_enter);


        final EditText qrName=(EditText) findViewById(R.id.editName);
        final EditText qrPhone=(EditText) findViewById(R.id.editPhone);
        final EditText qrMail=(EditText) findViewById(R.id.editMail);
        final EditText qrStreet=(EditText) findViewById(R.id.editStreet);
        final EditText qrCity=(EditText) findViewById(R.id.editCity);
        final EditText qrPostal=(EditText) findViewById(R.id.editPostal);

        Button generate=(Button) findViewById(R.id.generate);


        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {


                //MECARD:N:Owen,Sean;ADR:76 9th Avenue, 4th Floor, New York, NY 10011;TEL:12125551212;EMAIL:srowen@example.com;;


                result = qrName.getText().toString()+";ADR:"+qrStreet.getText().toString()+","+qrCity.getText().toString()+","+qrPostal.getText().toString()+";TEL:"+qrPhone.getText().toString()+";EMAIL:"+qrMail.getText().toString()+";;";
                Intent i = new Intent(MeCardEnterActivity.this, MeCardGnrActivity.class);
                i.putExtra("gn", result);
                startActivity(i);



            }

        });
    }
}
