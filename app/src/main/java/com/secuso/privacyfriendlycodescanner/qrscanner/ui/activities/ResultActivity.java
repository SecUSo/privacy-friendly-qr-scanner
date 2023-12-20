package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.zxing.client.result.ParsedResult;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.dialogfragments.QRCodeImageDialogFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.dialogfragments.RawDataDialogFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.CalendarResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ContactResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.EmailResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.GeoResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ProductResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.SMSResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.TelResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.TextResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.URLResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.WifiResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.ResultViewModel;

import java.text.DateFormat;
import java.util.Date;

/**
 * This activity displays the results of scan. Either from the history or from a scan directly.<br>
 * <p>
 * Use the method {@link #startResultActivity(Context, BarcodeResult)} if called from a scan.<br>
 * Use the method {@link #startResultActivity(Context, HistoryItem)} if called from the history.
 * </p>
 *
 * @author Christopher Beckmann
 * @see HistoryActivity
 * @see ScannerActivity
 */
public class ResultActivity extends AppCompatActivity {

    private static final String HISTORY_DATA = "ResultActivity.HISTORY_DATA";

    private static BarcodeResult barcodeResult = null;
    private static HistoryItem historyItem = null;

    private Button chooseActionButton = null;

    private ResultViewModel viewModel;

    private ResultFragment currentResultFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        chooseActionButton = findViewById(R.id.btnChooseAction);

        viewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        initStateIfNecessary(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (isFinishing()) {
            return;
        }

        loadFragment(viewModel.mParsedResult);

        if (isFinishing()) {
            return;
        }

        displayGeneralData();

        findViewById(R.id.btnChooseAction).setOnClickListener(this::onChooseActionButtonClick);

        findViewById(R.id.btnRawData).setOnClickListener(this::onRawDataButtonClick);

        findViewById(R.id.activity_result_qr_image).setOnClickListener(this::onQRImageClick);
    }

    /**
     * After this function is called the the following values will not be null:
     * <ul>
     *     <li>currentHistoryItem</li>
     *     <li>mCodeImage</li>
     *     <li>mSavedToHistory</li>
     *     <li>mParsedResult</li>
     * </ul>
     * If the state can not be created the activity will call {@link AppCompatActivity#finish()}
     * This method will also update the {@link HistoryItem} in the database with a recreation of the QR Code if the image is missing.
     *
     * @param savedInstanceState is the bundle that is given to the {@link #onCreate(Bundle)} or {@link #onRestoreInstanceState(Bundle)} Methods
     */
    private void initStateIfNecessary(Bundle savedInstanceState) {
        boolean hasHistoryItem = getIntent().getBooleanExtra(HISTORY_DATA, false);

        if (savedInstanceState == null) {
            if (hasHistoryItem && historyItem != null) {
                viewModel.initFromHistoryItem(historyItem);
            } else if (barcodeResult != null) {
                viewModel.initFromScan(barcodeResult);
            } else {
                // no data to display -> exit
                Toast.makeText(this, R.string.activity_result_toast_error_cant_load, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        getMenuInflater().inflate(R.menu.copy, menu);
        getMenuInflater().inflate(R.menu.save, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            MenuItem saveMi = menu.findItem(R.id.save);
            if (saveMi != null) {
                saveMi.setVisible(!viewModel.mSavedToHistory);
            }
        }

        return true;
    }

    private void displayGeneralData() {
        ImageView qrImageView = findViewById(R.id.activity_result_qr_image);
        TextView qrTypeText = findViewById(R.id.textView);
        TextView timestampTextView = findViewById(R.id.textViewTimestamp);
        TextView codeTypeText = findViewById(R.id.textViewCodeType);
        ImageView codeTypeImageView = findViewById(R.id.item_history_type_image);

        @DrawableRes int codeTypeDrawableRes = Utils.getBarcodeFormatIcon(viewModel.currentHistoryItem.getFormat());
        Glide.with(this).load(AppCompatResources.getDrawable(this, codeTypeDrawableRes)).placeholder(AppCompatResources.getDrawable(this, R.drawable.ic_no_image_accent_24dp)).into(codeTypeImageView);

        codeTypeText.setText(viewModel.currentHistoryItem.getFormat().toString());

        Glide.with(this).load(viewModel.mCodeImage).into(qrImageView);
        qrTypeText.setText(Contents.Type.parseParsedResultType(viewModel.mParsedResult.getType()).toLocalizedString(getApplicationContext()));

        long timestamp = viewModel.currentHistoryItem.getTimestamp();
        if (timestamp != 0) {
            DateFormat df = DateFormat.getDateTimeInstance();
            timestampTextView.setText(df.format(new Date(timestamp)));
        }
    }

    private void onQRImageClick(View view) {
        QRCodeImageDialogFragment fragment = new QRCodeImageDialogFragment();
        fragment.show(getSupportFragmentManager(), QRCodeImageDialogFragment.TAG);
    }

    private void onChooseActionButtonClick(View view) {
        if (currentResultFragment != null) {
            currentResultFragment.onProceedPressed(this);
        }
    }

    private void onRawDataButtonClick(View view) {
        RawDataDialogFragment fragment = RawDataDialogFragment.Companion.newInstance(viewModel.currentHistoryItem.getText());
        fragment.show(getSupportFragmentManager(), RawDataDialogFragment.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, viewModel.mParsedResult.getDisplayResult());
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
            return true;
        } else if (itemId == R.id.save) {
            viewModel.saveHistoryItem(viewModel.currentHistoryItem);
            invalidateOptionsMenu();
            Toast.makeText(this, R.string.activity_result_toast_saved, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.copy) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Text", viewModel.mParsedResult.getDisplayResult());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), R.string.content_copied, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(ParsedResult parsedResult) {
        if (parsedResult == null) {
            Toast.makeText(this.getBaseContext(), R.string.activity_result_toast_error_cant_load, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ResultFragment resultFragment;
        switch (parsedResult.getType()) {
            case ADDRESSBOOK:
                resultFragment = new ContactResultFragment();
                break;
            case CALENDAR:
                resultFragment = new CalendarResultFragment();
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
            case TEXT:
            default:
                resultFragment = new TextResultFragment();

                // hide "search" button if search engines are disabled
                if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_search_engine_enabled", true)) {
                    chooseActionButton.setVisibility(View.GONE);
                }

                break;
        }

        currentResultFragment = resultFragment;

        resultFragment.putQRCode(parsedResult);

        ft.replace(R.id.activity_result_frame_layout, resultFragment);
        ft.commit();

        chooseActionButton.setText(resultFragment.getProceedButtonTitle(this));
    }
}
