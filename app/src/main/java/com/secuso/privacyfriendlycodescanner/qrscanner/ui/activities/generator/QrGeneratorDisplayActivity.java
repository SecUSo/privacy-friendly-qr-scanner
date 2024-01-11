package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import static com.secuso.privacyfriendlycodescanner.qrscanner.helpers.PrefManager.PREF_SAVE_REAL_IMAGE_TO_HISTORY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ScannerActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.IconArrayAdapter;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.ScannerViewModel;

import java.io.IOException;
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
    private final String[] errorCorrectionsPDF417 = new String[]{"2", "3", "4", "5", "6", "7", "8"};
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
        TextView codeContentTextView = findViewById(R.id.codeContentTextView);

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

        codeContentTextView.setText(qrInputText);

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
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            QRGeneratorUtils.shareImage(this, QRGeneratorUtils.getCachedUri());
            return true;
        } else if (item.getItemId() == R.id.save) {
            // Use an instance of ScannerViewModel to get the BarcodeResult by 'scanning' the image
            ScannerViewModel scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
            scannerViewModel.isScanComplete().observe(this, scanComplete -> {
                if (scanComplete) {
                    BarcodeResult result = scannerViewModel.getScanResult().getValue();
                    scannerViewModel.clearScanResult();
                    if (result == null) {
                        Toast.makeText(this, getText(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            //Get the Bitmap, create the HistoryItem and insert it into the db.
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), QRGeneratorUtils.getCachedUri());
                            HistoryItem historyItem = Utils.createHistoryItem(bitmap, result, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(PREF_SAVE_REAL_IMAGE_TO_HISTORY, false));
                            AppRepository.getInstance(getApplication()).insertHistoryEntry(historyItem);

                            Toast.makeText(this, getText(R.string.activity_result_toast_saved), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(this, getText(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            //Start the 'scan'
            scannerViewModel.getBarcodeResultFromImage(QRGeneratorUtils.getCachedUri());
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

        updateDropDownMenus();
        generateAndUpdateImage();
    }
}

