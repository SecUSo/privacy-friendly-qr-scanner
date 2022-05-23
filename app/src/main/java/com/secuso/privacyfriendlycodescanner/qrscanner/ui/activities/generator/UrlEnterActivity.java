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

public class UrlEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_enter);

        final EditText qrResult = (EditText) findViewById(R.id.editURL);
        final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.editURLInputLayout);
        Button generate = (Button) findViewById(R.id.btnGenerate);

        // Enable text wrapping without line breaks.
        // (Only works if it is set via the code and not via the xml):
        qrResult.setMaxLines(Integer.MAX_VALUE);
        qrResult.setHorizontallyScrolling(false);

        // Set Input field helper text:
        inputLayout.setHelperText(getString(R.string.generator_url_explanation) + " " + getString(R.string.generator_example_url));

        int maxLength = 150;
        qrResult.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = qrResult.getText().toString();
                if (result.isEmpty()) {
                    Toast.makeText(UrlEnterActivity.this, R.string.activity_enter_toast_missing_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(UrlEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.WEB_URL);
                startActivity(i);
            }
        });


    }
}
