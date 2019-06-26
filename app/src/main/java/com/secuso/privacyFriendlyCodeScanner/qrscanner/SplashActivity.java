package com.secuso.privacyFriendlyCodeScanner.qrscanner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.helpers.PrefManager;

/**
 * @author Karola Marky
 * @version 20161022
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager firstStartPref = new PrefManager(this);

        Intent mainIntent;

        if(firstStartPref.isFirstTimeLaunch()) {
            mainIntent = new Intent(this, TutorialActivity.class);
        } else {
            mainIntent = new Intent(this, ScannerActivity.class);
        }

        startActivity(mainIntent);
        finish();
    }
}
