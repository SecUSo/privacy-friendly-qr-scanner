package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ScannerActivity;

public class QrGeneratorDisplayActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;

    String qrInputText = "";
    Contents.Type qrInputType = Contents.Type.UNDEFINED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator_display);

        Button btnstore = findViewById(R.id.btnStore);
        ImageView myImage = findViewById(R.id.resultQRCodeImage);

        Bundle QRData = getIntent().getExtras();//from QRGenerator
        qrInputText = QRData.getString("gn");
        qrInputType = (Contents.Type) QRData.getSerializable("type");

        setTitle(qrInputType.toLocalizedString(getApplicationContext()));

        Glide.with(this).load(QRGeneratorUtils.createImage(this, qrInputText, qrInputType)).into(myImage);

        btnstore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QRGeneratorUtils.saveCachedImageToExternalStorage(QrGeneratorDisplayActivity.this);

                Intent i = new Intent(QrGeneratorDisplayActivity.this, ScannerActivity.class);
                startActivity(i);
                Toast.makeText(QrGeneratorDisplayActivity.this, "QR code stored in gallery", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                QRGeneratorUtils.shareImage(this, QRGeneratorUtils.getCachedUri());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

