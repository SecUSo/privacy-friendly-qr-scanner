package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.*;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.qrcode.encoder.QRCode;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.DBHandler;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.ScanedData;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.EmailActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.SendEmailActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.TelActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.ResultsActivities.URLActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



      /*  Bundle QRData = getIntent().getExtras();
        String DataFromHistory = QRData.getString("Result");
        Toast.makeText(this, DataFromHistory, Toast.LENGTH_LONG).show(); */ // print items from History

       if(checked && QRCode!=null) {

           // addContent();
           ScanedData content=new ScanedData(QRCode);
           dbHandler.addContent(content);

          checkResult(QRCode);

                   }
        else if(!checked && QRCode!=null) {


         checkResult(QRCode);
           //Toast.makeText(this, "no Scan", Toast.LENGTH_LONG).show();
             }
        else if (QrHistory!=null)
       {
           checkResult(QrHistory);
       }

    }


   /* public void addContent()/////////DB
    {   Bundle QRData = getIntent().getExtras();
        String QRCode = QRData.getString("QRResult");
        ScanedData content=new ScanedData(QRCode);
        dbHandler.addContent(content);
    } */

    public  void checkResult(String result)
        {  //CharSequence charSequence=
          if(isValidEmail(result))
          {
              Intent i=new Intent(this, EmailActivity.class);
              i.putExtra("Rst",result);
              startActivity(i);
          }


          else if(validCellPhone(result))
          {
              Intent i=new Intent(this, TelActivity.class);
              i.putExtra("Rst",result);
              startActivity(i);

          }
          else if (isValidSendEmail(result))
          {
              Intent i=new Intent(this, SendEmailActivity.class);
              i.putExtra("Rst",result);
              startActivity(i);
          }
          else if(isValidURL(result))
          {
              Intent i=new Intent(this, URLActivity.class);
              i.putExtra("Rst",result);
              startActivity(i);
          }

          else {
              Toast.makeText(this, "Unkown", Toast.LENGTH_LONG).show();
          }

        }

    public final static boolean isValidEmail(CharSequence target) {

       String expression = "^MAILTO:+[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
        //return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }
    public final static boolean isValidSendEmail(String target)
    {
        Pattern pattern = Pattern.compile("MATMSG:TO:(.+?);SUB:(.+?);BODY:(.+?)",Pattern.CASE_INSENSITIVE);



        Matcher matcher =pattern.matcher(target);
        return matcher.matches();

       // return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }
    public final static boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();



    }
    public final static boolean isValidURL(String target)
    {
        return Patterns.WEB_URL.matcher(target).matches();
    }

}
