package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;

public class MarketEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_enter);

        final EditText qrMarket = (EditText) findViewById(R.id.editPackageName);
        final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.editPackageNameInputLayout);
        Button generate = (Button) findViewById(R.id.btnGenerate);

        int maxLength = 150;
        qrMarket.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        inputLayout.setHelperText(getString(R.string.generator_market_explanation)
                + " " + getString(R.string.generator_example_package)
                + "\n" + getString(R.string.generator_market_explanation_2));

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = qrMarket.getText().toString();
                if (result.isEmpty()) {
                    Toast.makeText(MarketEnterActivity.this, R.string.activity_enter_toast_missing_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MarketEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.MARKET);
                startActivity(i);
            }

        });
    }
}