package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.BaseActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.ScannerViewModel;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Handles the scanning action as well as asking for the camera permission if needed.
 * If a scan was sucessfully completed the result is passed to the {@link ResultActivity}.
 *
 * @author Christopher Beckmann
 */
public class ScannerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PERMISSION_CAMERA_REQUEST = 0;
    private static final int PICK_IMAGE_INTENT = 1;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST = 2;

    // UI
    private DecoratedBarcodeView barcodeScannerView;
    private TextView permissionNeededExplanation;
    private MenuItem flashOnButton;
    private MenuItem flashOffButton;

    // Logic
    private BeepManager beepManager;

    private ScannerViewModel viewModel;

    private final CameraPreview.StateListener stateListener = new CameraPreview.StateListener() {
        @Override
        public void previewSized() {
        }

        @Override
        public void previewStarted() {
        }

        @Override
        public void previewStopped() {
        }

        @Override
        public void cameraError(Exception error) {
        }

        @Override
        public void cameraClosed() {
        }
    };

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            onBarcodeResult(result);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private void onBarcodeResult(BarcodeResult result) {
        String contents = result.toString();
        if (contents.isEmpty()) {
            return;
        }

        barcodeScannerView.setStatusText(result.getText());

        beepManager.playBeepSoundAndVibrate();

        ResultActivity.startResultActivity(ScannerActivity.this, result);
//            Intent resultIntent = new Intent(ScannerActivity.this, ResultActivity.class);
//            resultIntent.putExtra("QRResult", new ParcelableResultDecorator(result.getResult()), result.getBitmapWithResultPoints());
//            startActivity(resultIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.activity_scanner);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        permissionNeededExplanation = findViewById(R.id.activity_scanner_permission_needed_explanation);
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        barcodeScannerView.getBarcodeView().addStateListener(stateListener);

        beepManager = new BeepManager(this);

        viewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        viewModel.isProcessingScan().observe(this, processing -> {
            View progressView = findViewById(R.id.image_processing_progress);
            if (progressView != null) {
                progressView.setVisibility(processing ? View.VISIBLE : View.GONE);
            }
            if (processing) {
                barcodeScannerView.pauseAndWait();
            } else {
                barcodeScannerView.resume();
            }
        });

        viewModel.isScanComplete().observe(this, scanComplete -> {
            if (scanComplete) {
                BarcodeResult result = viewModel.getScanResult().getValue();
                viewModel.clearScanResult();
                if (result == null) {
                    new MaterialAlertDialogBuilder(this)
                            .setMessage(R.string.no_code_in_image_explanation)
                            .setTitle(R.string.app_name)
                            .setIcon(R.drawable.ic_baseline_qr_code_24dp)
                            .setCancelable(true)
                            .setPositiveButton(R.string.okay, null)
                            .show();
                } else {
                    onBarcodeResult(result);
                }
            }
        });

        if (!preferences.getBoolean("pref_enable_beep_on_scan", false)) {
            beepManager.setBeepEnabled(false);
        }

        if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null && intent.getType().startsWith("image/")) {
            handleSendImage(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initScanWithPermissionCheck();
            } else {
                initScan();
            }
        }
    }

    @TargetApi(23)
    private void initScanWithPermissionCheck() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (!hasCameraPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST);
            showCameraPermissionRequirement(true);
        } else {
            initScan();
        }
    }

    private void showCameraPermissionRequirement(boolean show) {
        barcodeScannerView.setVisibility(show ? View.GONE : View.VISIBLE);

        if (show) {
            barcodeScannerView.pause();
        } else {
            if (Boolean.FALSE.equals(viewModel.isProcessingScan().getValue())) {
                barcodeScannerView.resume();
            }
        }

        permissionNeededExplanation.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initScan() {
        showCameraPermissionRequirement(false);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        if (!intent.hasExtra(Intents.Scan.SCAN_TYPE)) {
            intent.putExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN);
        }

        barcodeScannerView.setTorchListener(new TorchListener(this));

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScannerView.initializeFromIntent(intent);
        barcodeScannerView.decodeSingle(callback);
        if (Boolean.FALSE.equals(viewModel.isProcessingScan().getValue())) {
            barcodeScannerView.resume();
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_scan;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showCameraPermissionRequirement(true);
        } else {
            barcodeScannerView.setStatusText(null);
            initScan();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pauseAndWait();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(SAVED_ORIENTATION_LOCK, this.orientationLock);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScan();
            } else {
                // TODO
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_image, menu);
        getMenuInflater().inflate(R.menu.flashlight, menu);
        getMenuInflater().inflate(R.menu.select_camera, menu);

        flashOnButton = menu.findItem(R.id.menu_flashlight_on);
        flashOffButton = menu.findItem(R.id.menu_flashlight_off);

        barcodeScannerView.setTorchOff();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_flashlight_on) {
            barcodeScannerView.setTorchOn();
            return true;
        } else if (itemId == R.id.menu_flashlight_off) {
            barcodeScannerView.setTorchOff();
            return true;
        } else if (itemId == R.id.select_image) {
            onOpenImagePickerClick();
            return true;
        } else if (itemId == R.id.select_camera) {
            CameraSettings cameraSettings = barcodeScannerView.getCameraSettings();
            cameraSettings.setRequestedCameraId(cameraSettings.getRequestedCameraId() + 1);
            if (cameraSettings.getRequestedCameraId() >= Camera.getNumberOfCameras()) {
                cameraSettings.setRequestedCameraId(0);
            }
            barcodeScannerView.setCameraSettings(cameraSettings);
            barcodeScannerView.pause();
            barcodeScannerView.resume();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            viewModel.getBarcodeResultFromImage(imageUri);
        }
    }

    private void onOpenImagePickerClick() {
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("image_picker_first_click", true)) {
            new MaterialAlertDialogBuilder(ScannerActivity.this)
                    .setMessage(R.string.select_image_from_gallery_explanation)
                    .setPositiveButton(R.string.proceed, (dialogInterface, i) ->
                    {
                        openImagePicker();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("image_picker_first_click", Boolean.FALSE).apply();
                    })
                    .setTitle(R.string.select_image_from_gallery)
                    .setCancelable(true)
                    .create()
                    .show();
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, getResources().getString(R.string.select_image_from_gallery));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST);
            } else {
                startActivityForResult(chooserIntent, PICK_IMAGE_INTENT);
            }
        } else {
            startActivityForResult(chooserIntent, PICK_IMAGE_INTENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_INTENT && resultCode == RESULT_OK && data.getData() != null) {
            viewModel.getBarcodeResultFromImage(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_scanner_permission_needed_container:
                initScanWithPermissionCheck();
                break;
            default:
                break;
        }
    }


    class TorchListener implements DecoratedBarcodeView.TorchListener {
        WeakReference<ScannerActivity> mParent;

        public TorchListener(@NonNull ScannerActivity parent) {
            mParent = new WeakReference<>(parent);
        }

        @Override
        public void onTorchOn() {
            ScannerActivity parent = mParent.get();
            if (parent != null) {
                parent.flashOnButton.setVisible(false);
                parent.flashOffButton.setVisible(true);
            }
        }

        @Override
        public void onTorchOff() {
            ScannerActivity parent = mParent.get();
            if (parent != null) {
                parent.flashOnButton.setVisible(true);
                parent.flashOffButton.setVisible(false);
            }
        }
    }
}
