package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ContactResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.GeoResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ProductResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.SMSResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.EmailResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.TelResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.TextResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.URLResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.WifiResultFragment;

import static com.secuso.privacyfriendlycodescanner.qrscanner.helpers.PrefManager.PREF_SAVE_REAL_IMAGE_TO_HISTORY;

public class ResultActivity extends AppCompatActivity {

    private static final String HISTORY_DATA = "ResultActivity.HISTORY_DATA";

    private static BarcodeResult barcodeResult = null;
    private static HistoryItem historyItem = null;

    public static void startResultActivity(@NonNull Context context, @NonNull BarcodeResult barcodeResult) {
        ResultActivity.barcodeResult = barcodeResult;
        ResultActivity.historyItem = null;
        Intent resultIntent = new Intent(context, ResultActivity.class);
        context.startActivity(resultIntent);
    }

    public static void startResultActivity(@NonNull Context context, @NonNull HistoryItem historyItem) {
        ResultActivity.barcodeResult = null;
        ResultActivity.historyItem = historyItem;
        Intent resultIntent = new Intent(context, ResultActivity.class);
        resultIntent.putExtra(HISTORY_DATA, true);
        context.startActivity(resultIntent);
    }

    private SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    private BarcodeResult currentBarcodeResult = null;
    private ResultFragment currentResultFragment = null;
    private HistoryItem currentHistoryItem = null;
    private ParsedResult mParsedResult = null;
    private Bitmap mCodeImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        boolean hasHistoryItem = getIntent().getBooleanExtra(HISTORY_DATA, false);

        if(savedInstanceState == null) {
            if(hasHistoryItem) {
                currentHistoryItem = historyItem;
                mParsedResult = ResultParser.parseResult(historyItem.getResult());
                mCodeImage = historyItem.getImage();

            } else if(barcodeResult != null) {
                currentBarcodeResult = barcodeResult;
                mParsedResult = ResultParser.parseResult(currentBarcodeResult.getResult());
                mCodeImage = currentBarcodeResult.getBitmapWithResultPoints(ContextCompat.getColor(this, R.color.colorAccent));

                saveToHistory();
            } else {
                // no data to display -> exit
                finish();
                return;
            }
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        loadFragment(mParsedResult);
        displayGeneralData();
    }

    private void displayGeneralData() {
        ImageView qrImageView = findViewById(R.id.activity_result_qr_image);
        TextView qrTypeText = findViewById(R.id.textView);

        Glide.with(this).load(mCodeImage).into(qrImageView);
        qrTypeText.setText(mParsedResult.getType().name());
    }

    private void saveToHistory() {
        currentHistoryItem = new HistoryItem();

        Bitmap image;
        boolean prefSaveRealImage = mPreferences.getBoolean(PREF_SAVE_REAL_IMAGE_TO_HISTORY, false);
        if(prefSaveRealImage) {
            image = mCodeImage;
        } else {
            image = Utils.generateCode(currentBarcodeResult.getText(), currentBarcodeResult.getBarcodeFormat(), null, currentBarcodeResult.getResult().getResultMetadata());
        }
        currentHistoryItem.setImage(image);

        currentHistoryItem.setFormat(currentBarcodeResult.getResult().getBarcodeFormat());
        currentHistoryItem.setNumBits(currentBarcodeResult.getResult().getNumBits());
        currentHistoryItem.setRawBytes(currentBarcodeResult.getResult().getRawBytes());
        currentHistoryItem.setResultPoints(currentBarcodeResult.getResult().getResultPoints());
        currentHistoryItem.setText(currentBarcodeResult.getResult().getText());
        currentHistoryItem.setTimestamp(currentBarcodeResult.getResult().getTimestamp());

        AppRepository.getInstance(this).insertHistoryEntry(currentHistoryItem);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnProceed) {
            if(currentResultFragment != null) {
                currentResultFragment.onProceedPressed(this);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String displayResult = mParsedResult.getDisplayResult();

        switch (item.getItemId()){
            case R.id.share:
                Intent sharingIntent= new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, displayResult);
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_via)));
                return true;

            case R.id.copy:
                ClipboardManager clipboardManager =(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", displayResult);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), R.string.content_copied, Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(ParsedResult parsedResult) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ResultFragment resultFragment;

        switch (parsedResult.getType()) {
            case ADDRESSBOOK:
                resultFragment = new ContactResultFragment();
                break;
            case EMAIL_ADDRESS:
                resultFragment = new EmailResultFragment();
                break;
            case PRODUCT:
                resultFragment = new ProductResultFragment();
                break;
            case URI:
                resultFragment = new URLResultFragment();
                break;
            case GEO:
                resultFragment = new GeoResultFragment();
                break;
            case TEL:
                resultFragment = new TelResultFragment();
                break;
            case SMS:
                resultFragment = new SMSResultFragment();
                break;
            case WIFI:
                resultFragment = new WifiResultFragment();
                break;
            case ISBN:
                // TODO: add isbn fragment
            case VIN:
                // TODO: add vin fragment
            case CALENDAR:
                // TODO: add calendar fragment
            case TEXT:
            default:
                resultFragment = new TextResultFragment();
                break;
        }

        currentResultFragment = resultFragment;

        resultFragment.putQRCode(parsedResult);

        ft.replace(R.id.activity_result_frame_layout, resultFragment);
        ft.commit();
    }
}
