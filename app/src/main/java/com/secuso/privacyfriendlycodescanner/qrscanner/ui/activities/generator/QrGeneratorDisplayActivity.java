package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ScannerActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.IconArrayAdapter;

import java.util.Arrays;

public class QrGeneratorDisplayActivity extends AppCompatActivity {

    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST = 0;

    String qrInputText = "";
    Contents.Type qrInputType = Contents.Type.UNDEFINED;

    private final String[] barcodeFormats = new String[]{
            BarcodeFormat.QR_CODE.name(),
            BarcodeFormat.AZTEC.name(),
            BarcodeFormat.DATA_MATRIX.name(),
            BarcodeFormat.PDF_417.name(),
            BarcodeFormat.CODE_128.name()};
    private final Integer[] barcodeFormatIcons = new Integer[]{
            R.drawable.ic_baseline_qr_code_24dp,
            R.drawable.ic_aztec_code_24dp,
            R.drawable.ic_data_matrix_code_24dp,
            R.drawable.ic_pdf_417_code_24dp,
            R.drawable.ic_barcode_24dp};
    private AutoCompleteTextView dropdownMenuBarcodeFormat;
    private BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
    private BarcodeFormat lastFormat = barcodeFormat;

    private final String[] errorCorrectionsQR = new String[]{ErrorCorrectionLevel.L.name(), ErrorCorrectionLevel.M.name(), ErrorCorrectionLevel.Q.name(), ErrorCorrectionLevel.H.name()};
    private final String[] errorCorrectionsAztec = new String[]{"25", "50", "75", "90"};
    private final String[] errorCorrectionsPDF417 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
    private ArrayAdapter<String> dropdownAdapterErrorCorrection;
    private AutoCompleteTextView dropdownMenuErrorCorrection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator_display);

        Button btnstore = findViewById(R.id.btnStore);

        dropdownMenuBarcodeFormat = findViewById(R.id.editBarcodeFormat);
        IconArrayAdapter dropdownAdapterBarcodeFormat = new IconArrayAdapter(this, R.layout.list_item_generator, barcodeFormats, barcodeFormatIcons);
        dropdownMenuBarcodeFormat.setAdapter(dropdownAdapterBarcodeFormat);
        dropdownMenuBarcodeFormat.setText(barcodeFormats[0], false);
        dropdownMenuBarcodeFormat.setAdapter(dropdownAdapterBarcodeFormat);


        dropdownMenuBarcodeFormat.setOnItemClickListener((parent, view, position, id) -> generateAndUpdateImage());

        dropdownMenuErrorCorrection = findViewById(R.id.editErrorCorrection);
        dropdownAdapterErrorCorrection = new ArrayAdapter<>(QrGeneratorDisplayActivity.this, android.R.layout.simple_spinner_item, errorCorrectionsQR);
        dropdownAdapterErrorCorrection.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dropdownMenuErrorCorrection.setAdapter(dropdownAdapterErrorCorrection);
        dropdownMenuErrorCorrection.setText(errorCorrectionsQR[0], false);
        dropdownMenuErrorCorrection.setAdapter(dropdownAdapterErrorCorrection);

        dropdownMenuErrorCorrection.setOnItemClickListener((adapterView, view, i, l) -> generateAndUpdateImage());


        Bundle QRData = getIntent().getExtras();//from QRGenerator
        qrInputText = QRData.getString("gn");
        qrInputType = (Contents.Type) QRData.getSerializable("type");

        setTitle(qrInputType.toLocalizedString(getApplicationContext()));

        generateAndUpdateImage();

        btnstore.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST);
                } else {
                    saveImageToStorage();
                }
            } else {
                saveImageToStorage();
            }
        });
    }

    private void generateAndUpdateImage() {
        ImageView myImage = findViewById(R.id.resultQRCodeImage);
        TextInputLayout errorCorrectionLayout = findViewById(R.id.editErrorCorrectionInputLayout);

        barcodeFormat = BarcodeFormat.valueOf(dropdownMenuBarcodeFormat.getText().toString());

        if (!barcodeFormat.equals(lastFormat)) {
            //format has changed
            lastFormat = barcodeFormat;
            String[] errorCorrectionOptions = null;
            if (barcodeFormat.equals(BarcodeFormat.QR_CODE)) {
                errorCorrectionOptions = errorCorrectionsQR;
            } else if (barcodeFormat.equals(BarcodeFormat.AZTEC)) {
                errorCorrectionOptions = errorCorrectionsAztec;
            } else if (barcodeFormat.equals(BarcodeFormat.PDF_417)) {
                errorCorrectionOptions = errorCorrectionsPDF417;
            }

            //only show error correction input field if the selected format supports error correction
            if (errorCorrectionOptions == null) {
                errorCorrectionLayout.setVisibility(View.INVISIBLE);
            } else {
                errorCorrectionLayout.setVisibility(View.VISIBLE);
                dropdownAdapterErrorCorrection = new ArrayAdapter<>(QrGeneratorDisplayActivity.this, android.R.layout.simple_spinner_item, errorCorrectionOptions);
                dropdownAdapterErrorCorrection.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                dropdownMenuErrorCorrection.setAdapter(dropdownAdapterErrorCorrection);
                dropdownMenuErrorCorrection.setText(errorCorrectionOptions[0], false);
                dropdownMenuErrorCorrection.setAdapter(dropdownAdapterErrorCorrection);
            }

            //Update icon
            ImageView barcodeFormatIcon = findViewById(R.id.iconImageView);
            Glide.with(this).load(AppCompatResources.getDrawable(this, barcodeFormatIcons[Arrays.asList(barcodeFormats).indexOf(barcodeFormat.name())])).into(barcodeFormatIcon);
        }

        String errorCorrectionLevel = dropdownMenuErrorCorrection.getText().toString();
        try {
            Glide.with(this).load(QRGeneratorUtils.createImage(this, qrInputText, qrInputType, barcodeFormat, errorCorrectionLevel)).into(myImage);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, R.string.code_generation_error, Toast.LENGTH_SHORT).show();
            Log.d(getClass().getSimpleName(), "Error during code generation.", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToStorage();
            } else {
                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveImageToStorage() {
        QRGeneratorUtils.saveCachedImageToExternalStorage(QrGeneratorDisplayActivity.this);

        Intent i = new Intent(QrGeneratorDisplayActivity.this, ScannerActivity.class);
        startActivity(i);
        Toast.makeText(QrGeneratorDisplayActivity.this, R.string.image_stored_in_gallery, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            QRGeneratorUtils.shareImage(this, QRGeneratorUtils.getCachedUri());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QRGeneratorUtils.purgeCacheFolder(this);
    }
}

