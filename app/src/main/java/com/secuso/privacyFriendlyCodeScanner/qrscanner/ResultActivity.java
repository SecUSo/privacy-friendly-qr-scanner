package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.DBHandler;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.ScanedData;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.BizCardActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.ContactActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.EmailActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.GeoInfoActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.MeCardActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.MmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.ProductActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.SendEmailActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.SmsActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.TelActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.TextActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.URLActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.WifiActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class ResultActivity extends AppCompatActivity {

    DBHandler dbHandler;/////////DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        dbHandler = new DBHandler(this, null, null, 1);//DB

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ResultActivity.this);
        boolean checked = sharedPref.getBoolean("bool_history", true);
        Bundle QRData = getIntent().getExtras();
        String QRCode = QRData.getString("QRResult");

        Bundle QR = getIntent().getExtras();
        String QrHistory = QRData.getString("QrHistory");

       if(checked && QRCode!=null) {
            // addContent();
            ScanedData content=new ScanedData(QRCode);
            dbHandler.addContent(content);
            checkResult(QRCode);
       } else if(!checked && QRCode!=null) {
            checkResult(QRCode);
       } else if (QrHistory!=null) {
            checkResult(QrHistory);
       }

    }


    public void checkResult(String result)
    {
        Intent i;
        if(isValidEmail(result)) {
            i = new Intent(this, EmailActivity.class);
        } else if(isValidCellPhone(result)) {
            i = new Intent(this, TelActivity.class);
        } else if (isValidSendEmail(result)) {
            i=new Intent(this, SendEmailActivity.class);
        } else if(isValidURL(result)) {
            i=new Intent(this, URLActivity.class);
        } else if (isValidSms(result)|| isValidSmsTo(result)) {
            i=new Intent(this, SmsActivity.class);
        } else if (isValidMms(result)|| isValidMmsTo(result)) {
            i=new Intent(this, MmsActivity.class);
        } else if(isValidContact(result)) {
            i=new Intent(this, ContactActivity.class);
        } else if(isValidMeCard(result)) {
            i=new Intent(this, MeCardActivity.class);
        } else if(isValidBizCard(result)) {
            i=new Intent(this, BizCardActivity.class);
        } else if(isValidWifi(result)) {
            i=new Intent(this, WifiActivity.class);
        } else if(isValidGeoInfo(result)) {
            i=new Intent(this, GeoInfoActivity.class);
        } else if(isValidProduct(result)) {
            i=new Intent(this, ProductActivity.class);
        } else {
            i=new Intent(this, TextActivity.class);
        }

        i.putExtra("Rst",result);
        startActivity(i);
        finish();
    }

       // *********************************************************************** //

    public static boolean isValidEmail(CharSequence target) {

       String expression = "^MAILTO:+[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
        //return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }
    public static boolean isValidSendEmail(String target)
    {
        Pattern pattern = Pattern.compile("MATMSG:TO:(.+?);SUB:(.+?);BODY:(.+?)", CASE_INSENSITIVE);

        Matcher matcher =pattern.matcher(target);
        return matcher.matches();

       // return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }
    public static boolean isValidCellPhone(String number)
    {
        // return android.util.Patterns.PHONE.matcher(number).matches();
        if (number.startsWith("tel:"))
            return true;
        else return false;

    }

    public static boolean isValidProduct(String target)
    {

        if (target.startsWith("market://"))
            return true;
        else return false;

    }
    public static boolean isValidURL(String target)
    {
        return Patterns.WEB_URL.matcher(target).matches();
    }

    public static boolean isValidSms(String target)
    {
        Pattern pattern = Pattern.compile("sms:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(target);
        return matcher.matches();
    }
    public static boolean isValidSmsTo(String target)
    {
        Pattern pattern = Pattern.compile("SMSTO:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(target);
        return matcher.matches();
    }

    public static boolean isValidMms(String target)
    {
        Pattern pattern = Pattern.compile("mms:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(target);
        return matcher.matches();
    }
    public static boolean isValidMmsTo(String target)
    {
        Pattern pattern = Pattern.compile("MMSTO:(.+?):(.+?)", CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(target);
        return matcher.matches();
    }
    public boolean isValidContact(String target)
    {
       /* Pattern pattern = Pattern.compile("([\\n|;|:](FN:|N:)[0-9a-zA-Z-\\säöüÄÖÜß,]*[\\n|;])");
         Matcher matcher = pattern.matcher(target);
        return matcher.matches();*/
        return target.startsWith("BEGIN:VCARD");
    }
    public boolean isValidMeCard(String target)
    {

        return target.startsWith("MECARD");
    }
    public boolean isValidBizCard(String target)
    {

        return target.startsWith("BIZCARD");
    }
    public boolean isValidWifi(String target)
    {
        return target.startsWith("WIFI:");
    }
    public boolean isValidGeoInfo(String target)
    {
        return target.startsWith("geo:");
    }

}
