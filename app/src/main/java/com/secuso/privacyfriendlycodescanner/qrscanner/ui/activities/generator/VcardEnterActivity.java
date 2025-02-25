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
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorKeyboardListener;

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


        ExtendedFloatingActionButton generate = (ExtendedFloatingActionButton) findViewById(R.id.btnGenerate);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        GeneratorKeyboardListener listener = new GeneratorKeyboardListener(rootView, generate, R.id.btnGenerate, getApplicationContext().getResources().getDisplayMetrics().densityDpi);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);


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
                i.putExtra("type", Contents.Type.V_CARD);
                startActivity(i);
            }
        });
    }
}
