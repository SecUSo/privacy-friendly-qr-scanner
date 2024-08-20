/*
    Privacy Friendly QR Scanner
    Copyright (C) 2020-2025 Privacy Friendly QR Scanner authors and SECUSO

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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorListAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;


public class QrGeneratorOverviewActivity extends AppCompatActivity {
    ListView listView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (createCodeFromIntent()) { // Handle intent from share dialog
            finishAffinity();
            return;
        }

        setContentView(R.layout.activity_qr_generator);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list_view);

        String[] generator = getResources().getStringArray(R.array.generator_array);
        Integer[] icons = new Integer[]{
                R.drawable.ic_baseline_subject_24dp,
                R.drawable.ic_email_accent_24dp,
                R.drawable.ic_baseline_public_24dp,
                R.drawable.ic_phone_accent_24dp,
                R.drawable.ic_baseline_sms_24dp,
                R.drawable.ic_baseline_place_24dp,
                R.drawable.ic_person_accent_24dp,
                R.drawable.ic_person_accent_24dp,
                R.drawable.ic_baseline_wifi_24dp,
                R.drawable.ic_person_accent_24dp,
                R.drawable.ic_baseline_shopping_cart_24dp};

        // Remove mail element (position 1) and phone element (position 3) for Google Play version
        String[] finalGenerator = generator;
        Integer[] finalIcons = icons;
        generator = IntStream.range(0, finalGenerator.length).filter(value -> value != 3).filter(value -> value != 1).mapToObj(value -> finalGenerator[value]).toArray(String[]::new);
        icons = IntStream.range(0, finalIcons.length).filter(value -> value != 3).filter(value -> value != 1).mapToObj(value -> finalIcons[value]).toArray(Integer[]::new);
        GeneratorListAdapter adapter = new GeneratorListAdapter(this, generator, icons);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Skip position 1 and 3 for Google Play version
            if (position >= 1) position++;
            if (position >= 3) position++;
            Intent intent = switch (position) {
                case 0 -> new Intent(QrGeneratorOverviewActivity.this, TextEnterActivity.class);
                case 2 -> new Intent(QrGeneratorOverviewActivity.this, UrlEnterActivity.class);
                case 4 -> new Intent(QrGeneratorOverviewActivity.this, SmsEnterActivity.class);
                case 5 -> new Intent(QrGeneratorOverviewActivity.this, GeoLocationEnterActivity.class);
                case 6 -> new Intent(QrGeneratorOverviewActivity.this, MeCardEnterActivity.class);
                case 7 -> new Intent(QrGeneratorOverviewActivity.this, BizCardEnterActivity.class);
                case 8 -> new Intent(QrGeneratorOverviewActivity.this, WifiEnterActivity.class);
                case 9 -> new Intent(QrGeneratorOverviewActivity.this, VcardEnterActivity.class);
                case 10 -> new Intent(QrGeneratorOverviewActivity.this, MarketEnterActivity.class);
                default -> null;
            };
            startActivity(intent);
        });
    }

    private boolean createCodeFromIntent() {
        Intent intent = getIntent();
        if (intent == null || !Intent.ACTION_SEND.equals(intent.getAction())) {
            return false;
        }
        String text = null;
        Contents.Type type = Contents.Type.UNDEFINED;
        if ("text/plain".equals(intent.getType())) {
            text = intent.getStringExtra(Intent.EXTRA_TEXT) == null ? "" : intent.getStringExtra(Intent.EXTRA_TEXT);
            type = Contents.Type.TEXT;
        } else if ("text/x-vcard".equals(intent.getType())) {
            if (intent.getExtras() != null && intent.getExtras().get(Intent.EXTRA_STREAM) != null) {
                Uri uri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);
                StringBuilder sb = new StringBuilder();
                try (InputStream is = getContentResolver().openInputStream(uri)) {
                    int c;
                    try {
                        while ((c = is.read()) != -1) {
                            sb.append(Character.toChars(c));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                text = sb.toString();
                type = Contents.Type.V_CARD;
            }
        }
        if (type.equals(Contents.Type.UNDEFINED) || text == null) {
            return false;
        }
        Intent createCodeIntent = (Intent) intent.clone();
        createCodeIntent.setClass(this, QrGeneratorDisplayActivity.class);
        createCodeIntent.putExtra("gn", text);
        createCodeIntent.putExtra("type", type);
        createCodeIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(createCodeIntent);
        return true;
    }
}

