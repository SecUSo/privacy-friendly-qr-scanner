/*
    Privacy Friendly QR Scanner
    Copyright (C) 2018-2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorKeyboardListener;

public class WifiEnterActivity extends AppCompatActivity {


    private String[] auth;
    private ArrayAdapter<String> dropdownAdapter;
    private AutoCompleteTextView dropdownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_enter);

        auth = new String[]{getString(R.string.no_encryption), "WEP", "WPA/WPA2"};

        dropdownMenu = (AutoCompleteTextView) findViewById(R.id.editWifiEncryption);
        dropdownAdapter = new ArrayAdapter<>(WifiEnterActivity.this, android.R.layout.simple_spinner_item, auth);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dropdownMenu.setAdapter(dropdownAdapter);
        dropdownMenu.setText(auth[2], false);
        dropdownMenu.setAdapter(dropdownAdapter);

        final EditText qrNetwork = (EditText) findViewById(R.id.editWifiSSID);
        final EditText qrPassword = (EditText) findViewById(R.id.editWifiPassword);

        int maxLength = 25;
        qrNetwork.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        int maxLength2 = 40;
        qrPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength2)});


        dropdownMenu.setOnItemClickListener((adapterView, view, i, l) -> {
            if (dropdownMenu.getText().toString().equals(auth[0])) {
                //disable password field if no encryption was selected
                findViewById(R.id.editWifiPasswordInputLayout).setEnabled(false);
            } else {
                findViewById(R.id.editWifiPasswordInputLayout).setEnabled(true);
            }
        });

        ExtendedFloatingActionButton generate = (ExtendedFloatingActionButton) findViewById(R.id.btnGenerate);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        GeneratorKeyboardListener listener = new GeneratorKeyboardListener(rootView, generate, R.id.btnGenerate, getApplicationContext().getResources().getDisplayMetrics().densityDpi);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  WIFI:S:mynetwork;T:WPA;P:mypass;;
                String result = "";

                result += "S:" + QRGeneratorUtils.escapeQRPropertyValue(qrNetwork.getText().toString());

                // Add encryption type if encryption was selected
                if (!dropdownMenu.getText().toString().equals(auth[0])) {
                    result += ";T:";
                    if (dropdownMenu.getText().toString().equals(auth[1])) {
                        result += "WEP";
                    } else if (dropdownMenu.getText().toString().equals(auth[2])) {
                        result += "WPA";
                    }

                    // Add password
                    if (!qrPassword.getText().toString().isEmpty()) {
                        result += ";P:" + QRGeneratorUtils.escapeQRPropertyValue(qrPassword.getText().toString());
                    }
                }
                result += ";;";

                Intent i = new Intent(WifiEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.WIFI);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set adapter on resume to prevent missing dropdown items in some cases
        dropdownAdapter = new ArrayAdapter<>(WifiEnterActivity.this, android.R.layout.simple_spinner_item, auth);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dropdownMenu.setAdapter(dropdownAdapter);
        if (dropdownMenu.getText().toString().equals(auth[0])) {
            //disable password field if no encryption was selected
            findViewById(R.id.editWifiPasswordInputLayout).setEnabled(false);
        } else {
            findViewById(R.id.editWifiPasswordInputLayout).setEnabled(true);
        }
    }
}
