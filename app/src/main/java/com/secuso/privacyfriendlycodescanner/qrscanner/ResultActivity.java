package com.secuso.privacyfriendlycodescanner.qrscanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.secuso.privacyfriendlycodescanner.qrscanner.database.DBHandler;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.ScannedData;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.BizCardResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.ContactResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.EmailResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.GeoResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.MMSResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.MeCardResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.ProductResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.QRResultType;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.ResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.SMSResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.SendEmailResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.TelResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.TextResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.URLResultFragment;
import com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments.WifiResultFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.secuso.privacyfriendlycodescanner.qrscanner.R.string.content_copied;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class ResultActivity extends AppCompatActivity implements TextResultFragment.OnFragmentInteractionListener {

    private QRResultType currentResultType;
    private String currentQrCode;
    private ResultFragment currentResultFragment;

    @Override
    public void onFragmentInteraction(Uri uri) {
        //getSupportFragmentManager()
    }

    DBHandler dbHandler;/////////DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dbHandler = new DBHandler(this, null);//DB

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checked = sharedPref.getBoolean("bool_history", true);
        Bundle intentExtras = getIntent().getExtras();
        String QRCode = intentExtras.getString("QRResult");
        this.currentQrCode = QRCode;

        String QrHistory = intentExtras.getString("QrHistory");

        QRResultType resultType = QRResultType.TEXT;

        if (checked && QRCode != null) {
            // addContent();
            ScannedData content = new ScannedData(QRCode);
            dbHandler.addContent(content);
            resultType = checkResult(QRCode);
        } else if (!checked && QRCode != null) {
            resultType = checkResult(QRCode);
        } else if (QrHistory != null) {
            resultType = checkResult(QrHistory);
        }

        loadFragment(resultType, QRCode);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnProceed) {
            if (currentResultFragment != null) {
                currentResultFragment.onProceedPressed(this, currentQrCode);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle intentExtras = getIntent().getExtras();
        String qrCode = intentExtras.getString("QRResult");

        switch (item.getItemId()) {
            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, qrCode);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                return true;

            case R.id.copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", qrCode);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), content_copied, Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(QRResultType resultType, String qrCodeString) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        currentResultType = resultType;

        ResultFragment resultFragment;

        switch (resultType) {
            case EMAIL:
                resultFragment = new EmailResultFragment();
                break;
            case PHONE:
                resultFragment = new TelResultFragment();
                break;
            case SEND_EMAIL:
                resultFragment = new SendEmailResultFragment();
                break;
            case URL:
                resultFragment = new URLResultFragment();
                break;
            case SMS:
                resultFragment = new SMSResultFragment();
                break;
            case MMS:
                resultFragment = new MMSResultFragment();
                break;
            case CONTACT:
                resultFragment = new ContactResultFragment();
                break;
            case ME_CARD:
                resultFragment = new MeCardResultFragment();
                break;
            case BIZ_CARD:
                resultFragment = new BizCardResultFragment();
                break;
            case WIFI:
                resultFragment = new WifiResultFragment();
                break;
            case GEO:
                resultFragment = new GeoResultFragment();
                break;
            case PRODUCT:
                resultFragment = new ProductResultFragment();
                break;
            case TEXT:
            default:
                resultFragment = new TextResultFragment();
        }

        currentResultFragment = resultFragment;

        resultFragment.putQRCode(qrCodeString, resultType);

        ft.replace(R.id.activity_result_frame_layout, resultFragment);
        ft.commit();
    }

    public QRResultType checkResult(String result) {
        if (isValidEmail(result)) {
            return QRResultType.EMAIL;
        } else if (isValidCellPhone(result)) {
            return QRResultType.PHONE;
        } else if (isValidSendEmail(result)) {
            return QRResultType.SEND_EMAIL;
        } else if (isValidURL(result)) {
            return QRResultType.URL;
        } else if (isValidSms(result) || isValidSmsTo(result)) {
            return QRResultType.SMS;
        } else if (isValidMms(result) || isValidMmsTo(result)) {
            return QRResultType.MMS;
        } else if (isValidContact(result)) {
            return QRResultType.CONTACT;
        } else if (isValidMeCard(result)) {
            return QRResultType.ME_CARD;
        } else if (isValidBizCard(result)) {
            return QRResultType.BIZ_CARD;
        } else if (isValidWifi(result)) {
            return QRResultType.WIFI;
        } else if (isValidGeoInfo(result)) {
            return QRResultType.GEO;
        } else if (isValidProduct(result)) {
            return QRResultType.PRODUCT;
        } else {
            return QRResultType.TEXT;
        }
    }

    // *********************************************************************** //

    public static boolean isValidEmail(CharSequence target) {

        String expression = "^MAILTO:+[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
        //return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    public static boolean isValidSendEmail(String target) {
        Pattern pattern = Pattern.compile("MATMSG:TO:(.+?);SUB:(.+?);BODY:(.+?)", CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(target);
        return matcher.matches();

        // return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    public static boolean isValidCellPhone(String number) {
        // return android.util.Patterns.PHONE.matcher(number).matches();
        return number.startsWith("tel:");

    }

    public static boolean isValidProduct(String target) {
        return target.startsWith("market://");

    }

    public static boolean isValidURL(String target) {
        return Patterns.WEB_URL.matcher(target).matches();
    }

    public static boolean isValidSms(String target) {
        Pattern pattern = Pattern.compile("sms:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public static boolean isValidSmsTo(String target) {
        Pattern pattern = Pattern.compile("SMSTO:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public static boolean isValidMms(String target) {
        Pattern pattern = Pattern.compile("mms:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public static boolean isValidMmsTo(String target) {
        Pattern pattern = Pattern.compile("MMSTO:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public boolean isValidContact(String target) {
       /* Pattern pattern = Pattern.compile("([\\n|;|:](FN:|N:)[0-9a-zA-Z-\\säöüÄÖÜß,]*[\\n|;])");
         Matcher matcher = pattern.matcher(target);
        return matcher.matches();*/
        return target.startsWith("BEGIN:VCARD");
    }

    public boolean isValidMeCard(String target) {

        return target.startsWith("MECARD");
    }

    public boolean isValidBizCard(String target) {

        return target.startsWith("BIZCARD");
    }

    public boolean isValidWifi(String target) {
        return target.startsWith("WIFI:");
    }

    public boolean isValidGeoInfo(String target) {
        return target.startsWith("geo:");
    }

}
