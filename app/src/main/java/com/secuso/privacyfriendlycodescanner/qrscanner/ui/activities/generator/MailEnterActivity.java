package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;

public class MailEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_enter);

        final EditText emailAddress = (EditText) findViewById(R.id.editMail);
        final EditText emailSubject = (EditText) findViewById(R.id.editMailSubject);
        final EditText emailContent = (EditText) findViewById(R.id.editMailContent);
        Button generate = (Button) findViewById(R.id.btnGenerate);

        int maxLength = 50;
        emailAddress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = emailAddress.getText().toString();
                if (result.isEmpty()) {
                    Toast.makeText(MailEnterActivity.this, R.string.activity_enter_toast_missing_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean subjectAdded = false;
                if (!emailSubject.getText().toString().isEmpty()) {
                    result += "?" + "subject=" + Uri.encode(emailSubject.getText().toString());
                    subjectAdded = true;
                }
                if (!emailContent.getText().toString().isEmpty()) {
                    result += (subjectAdded ? "&" : "?") + "body=" + Uri.encode(emailContent.getText().toString());
                }
                Intent i = new Intent(MailEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.EMAIL);
                startActivity(i);
            }

        });
    }
}
