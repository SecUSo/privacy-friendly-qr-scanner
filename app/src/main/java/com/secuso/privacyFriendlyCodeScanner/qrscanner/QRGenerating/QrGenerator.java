package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.MmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.SmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.TextActivity;


public class QrGenerator extends AppCompatActivity {





    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        final RadioButton btnMail = (RadioButton) findViewById(R.id.radioMail);
        final RadioButton btnTel = (RadioButton) findViewById(R.id.radioTel);
        final RadioButton btnUrl = (RadioButton) findViewById(R.id.radioUrl);
        final RadioButton btnSms = (RadioButton) findViewById(R.id.radioSms);
        final RadioButton btnGeo = (RadioButton) findViewById(R.id.radioGeo);

        final EditText qrResult=(EditText) findViewById(R.id.gnResult);
        final EditText qrSms=(EditText) findViewById(R.id.editTel);
        final EditText qrText=(EditText) findViewById(R.id.editText1);
        final EditText qrLatitude=(EditText) findViewById(R.id.editGeo1);
        final EditText qrLongitude=(EditText) findViewById(R.id.editGeo2);

        Button generate=(Button) findViewById(R.id.generate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId)
                {
                    case R.id.radioMail:
                        qrResult.setEnabled(true);
                        qrSms.setEnabled(false);
                        qrText.setEnabled(false);
                        qrLatitude.setEnabled(false);
                        qrLongitude.setEnabled(false);
                        break;

                    case R.id.radioTel:
                        qrResult.setEnabled(true);
                        qrSms.setEnabled(false);
                        qrText.setEnabled(false);
                        qrLatitude.setEnabled(false);
                        qrLongitude.setEnabled(false);
                        break;

                    case R.id.radioUrl:
                        qrResult.setEnabled(true);
                        qrSms.setEnabled(false);
                        qrText.setEnabled(false);
                        qrLatitude.setEnabled(false);
                        qrLongitude.setEnabled(false);
                        break;

                    case R.id.radioSms:
                        qrResult.setEnabled(false);
                        qrLatitude.setEnabled(false);
                        qrLongitude.setEnabled(false);
                        qrSms.setEnabled(true);
                        qrText.setEnabled(true);
                        break;

                    case R.id.radioGeo:
                        qrResult.setEnabled(false);
                        qrSms.setEnabled(false);
                        qrText.setEnabled(false);
                        qrLatitude.setEnabled(true);
                        qrLongitude.setEnabled(true);
                        break;
                }

            }
        });



        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find which radioButton is checked by id

                if(selectedId == btnMail.getId()) {

                    result = qrResult.getText().toString();
                    Intent i = new Intent(QrGenerator.this, MailGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);

                } else if(selectedId == btnTel.getId()) {

                    result = qrResult.getText().toString();
                    Intent i = new Intent(QrGenerator.this, TelGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);


                }  else if(selectedId == btnUrl.getId()) {

                    result = qrResult.getText().toString();
                    Intent i = new Intent(QrGenerator.this, UrlGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);
                } else if (selectedId==btnSms.getId()){

                    result = qrSms.getText().toString()+":"+qrText.getText().toString();
                    Intent i = new Intent(QrGenerator.this, SmsGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);

                }
                else if (selectedId==btnGeo.getId()){

                   result = qrLatitude.getText().toString()+","+qrLongitude.getText().toString();


                    Intent i = new Intent(QrGenerator.this, GeoLocatioGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);

                }

            }

        });




    }


}

