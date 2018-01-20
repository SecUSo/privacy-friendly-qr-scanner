package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

public class MailEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_enter);

        final EditText qrResult=(EditText) findViewById(R.id.editMail);
        Button generate=(Button) findViewById(R.id.generate);

        generate.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {





                    result = qrResult.getText().toString();
                    Intent i = new Intent(MailEnterActivity.this, MailGnrActivity.class);
                    i.putExtra("gn", result);
                    startActivity(i);




            }

        });
    }
}
