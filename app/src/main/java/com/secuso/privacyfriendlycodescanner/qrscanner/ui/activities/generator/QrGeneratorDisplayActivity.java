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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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

    private String[] barcodeFormats = new String[]{
            BarcodeFormat.QR_CODE.name(),
            BarcodeFormat.AZTEC.name(),
            BarcodeFormat.DATA_MATRIX.name(),
            BarcodeFormat.PDF_417.name(),
            BarcodeFormat.CODE_128.name()};
    private Integer[] barcodeFormatIcons = new Integer[]{
            R.drawable.ic_baseline_qr_code_24dp,
            R.drawable.ic_aztec_code_24dp,
            R.drawable.ic_data_matrix_code_24dp,
            R.drawable.ic_pdf_417_code_24dp,
            R.drawable.ic_barcode_24dp};
    private IconArrayAdapter barcodeFormatAdapter;
    private AutoCompleteTextView barcodeFormatMenu;
    private BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

    private final String[] errorCorrectionsQR = new String[]{ErrorCorrectionLevel.L.name(), ErrorCorrectionLevel.M.name(), ErrorCorrectionLevel.Q.name(), ErrorCorrectionLevel.H.name()};
    private final String[] errorCorrectionsAztec = new String[]{"25", "50", "75", "90"};
    private final String[] errorCorrectionsPDF417 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
    private String[] currentErrorCorrections = errorCorrectionsQR;
    private ArrayAdapter<String> errorCorrectionAdapter;
    private AutoCompleteTextView errorCorrectionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator_display);

        Button btnstore = findViewById(R.id.btnStore);
        barcodeFormatMenu = findViewById(R.id.editBarcodeFormat);
        errorCorrectionMenu = findViewById(R.id.editErrorCorrection);

        if (Build.VERSION.SDK_INT < 19) {
            barcodeFormats = new String[]{BarcodeFormat.QR_CODE.name(), BarcodeFormat.CODE_128.name()};
            barcodeFormatIcons = new Integer[]{R.drawable.ic_baseline_qr_code_24dp, R.drawable.ic_barcode_24dp};
        }

        barcodeFormatMenu.setOnItemClickListener((parent, view, position, id) -> {
            updateDropDownMenus();
            generateAndUpdateImage();
        });

        errorCorrectionMenu.setOnItemClickListener((adapterView, view, i, l) -> generateAndUpdateImage());


        Bundle QRData = getIntent().getExtras();//from QRGenerator
        qrInputText = QRData.getString("gn");
        qrInputType = (Contents.Type) QRData.getSerializable("type");

        setTitle(qrInputType.toLocalizedString(getApplicationContext()));

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

        initDropDownMenus();
    }

    private void initDropDownMenus() {
        barcodeFormatAdapter = newBarcodeFormatAdapter();
        barcodeFormatMenu.setAdapter(barcodeFormatAdapter);
        barcodeFormatMenu.setText(barcodeFormats[0], false);
        barcodeFormatMenu.setAdapter(barcodeFormatAdapter);

        errorCorrectionAdapter = newErrorCorrectionAdapter(errorCorrectionsQR);
        errorCorrectionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        errorCorrectionMenu.setAdapter(errorCorrectionAdapter);
        errorCorrectionMenu.setText(errorCorrectionsQR[0], false);
        errorCorrectionMenu.setAdapter(errorCorrectionAdapter);
    }

    private void updateDropDownMenus() {
        barcodeFormat = BarcodeFormat.valueOf(barcodeFormatMenu.getText().toString());

        if (barcodeFormat.equals(BarcodeFormat.QR_CODE)) {
            currentErrorCorrections = errorCorrectionsQR;
        } else if (barcodeFormat.equals(BarcodeFormat.AZTEC)) {
            currentErrorCorrections = errorCorrectionsAztec;
        } else if (barcodeFormat.equals(BarcodeFormat.PDF_417)) {
            currentErrorCorrections = errorCorrectionsPDF417;
        } else {
            currentErrorCorrections = null;
        }
        updateErrorCorrectionMenu();
        //Update icon
        ImageView barcodeFormatIcon = findViewById(R.id.iconImageView);
        Glide.with(this).load(AppCompatResources.getDrawable(this, barcodeFormatIcons[Arrays.asList(barcodeFormats).indexOf(barcodeFormat.name())])).into(barcodeFormatIcon);

    }

    private void updateErrorCorrectionMenu() {
        TextInputLayout errorCorrectionLayout = findViewById(R.id.editErrorCorrectionInputLayout);
        //only show error correction input field if the selected format supports error correction
        if (currentErrorCorrections == null) {
            errorCorrectionLayout.setVisibility(View.INVISIBLE);
        } else {
            errorCorrectionLayout.setVisibility(View.VISIBLE);
            errorCorrectionAdapter = newErrorCorrectionAdapter(currentErrorCorrections);
            errorCorrectionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            errorCorrectionMenu.setAdapter(errorCorrectionAdapter);

            if (!Arrays.asList(currentErrorCorrections).contains(errorCorrectionMenu.getText().toString())) {
                errorCorrectionMenu.setText(currentErrorCorrections[0], false);
                errorCorrectionMenu.setAdapter(errorCorrectionAdapter);
            }
        }
    }

    private void generateAndUpdateImage() {
        ImageView myImage = findViewById(R.id.resultQRCodeImage);

        barcodeFormat = BarcodeFormat.valueOf(barcodeFormatMenu.getText().toString());
        String errorCorrectionLevel = errorCorrectionMenu.getText().toString();
        try {
            Log.d(getClass().getSimpleName(), "Creating image...");
            Glide.with(this).asBitmap().load(QRGeneratorUtils.createImage(this, qrInputText, qrInputType, barcodeFormat, errorCorrectionLevel)).into(new BitmapImageViewTarget(myImage));
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, R.string.code_generation_error, Toast.LENGTH_SHORT).show();
            Log.d(getClass().getSimpleName(), "Error during code generation.", e);
        }
    }

    private IconArrayAdapter newBarcodeFormatAdapter() {
        return new IconArrayAdapter(this, R.layout.list_item_generator, barcodeFormats, barcodeFormatIcons);
    }

    private ArrayAdapter<String> newErrorCorrectionAdapter(String[] items) {
        return new ArrayAdapter<>(QrGeneratorDisplayActivity.this, android.R.layout.simple_spinner_item, items);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Set adapter on resume to prevent missing dropdown items in some cases (e.g. screen rotation)
        barcodeFormatAdapter = newBarcodeFormatAdapter();
        barcodeFormatMenu.setAdapter(barcodeFormatAdapter);

        errorCorrectionAdapter = newErrorCorrectionAdapter(currentErrorCorrections);
        errorCorrectionMenu.setAdapter(errorCorrectionAdapter);
        updateDropDownMenus();
        generateAndUpdateImage();
    }
}

