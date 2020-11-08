package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;

public class LongTextEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_text_enter);

        final EditText qrText = findViewById(R.id.editText);
        Button generate = findViewById(R.id.generate);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // Intent sent through an external application
        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String textData = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (textData != null) {
                    qrText.setText(textData);
                }
            }
        }

        int maxLength = 1138;
        qrText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {

                result = qrText.getText().toString();
                Intent i = new Intent(LongTextEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.TEXT);
                startActivity(i);
            }

        });
    }
}
