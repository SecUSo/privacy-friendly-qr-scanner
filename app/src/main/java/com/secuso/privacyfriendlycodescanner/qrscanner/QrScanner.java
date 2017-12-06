package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.os.Build.VERSION.SDK_INT;


public class QrScanner extends AppCompatActivity {



        private Button scan_bt;
        private Activity activity;


        @SuppressLint("RestrictedApi")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            setContentView(R.layout.activity_qr_scanner);
            scan_bt = (Button) findViewById(R.id.btScan);
            final Activity activity = this;
            scan_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                  //  SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                  //  integrator.setBeepEnabled(prefs.getBoolean("beep", true));
                    integrator.setOrientationLocked(false);
                    //integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            String dataResult;
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
                } else {
                    dataResult=result.getContents();
                    Intent i=new Intent(this, ResultActivity.class);
                    i.putExtra("QRResult",dataResult);
                    startActivity(i);


                   // Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
