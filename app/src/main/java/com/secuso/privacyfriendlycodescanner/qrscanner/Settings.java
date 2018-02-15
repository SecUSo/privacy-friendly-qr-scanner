package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toolbar;


public class Settings extends PreferenceActivity {

   // private int PRIVATE_MODE = 0;
    //CheckBox cb;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_settings);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

      //  getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup)findViewById(android.R.id.content));
      // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
       //getSuppor(toolbar);

       // setActionBar(toolbar);





       // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
/*
        cb=(CheckBox)findViewById(R.id.Cbdisable);
        cb.setChecked(getFromSP("cb1"));

    }
       boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("checkboxstate", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private void saveInSp(String key,boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("checkboxstate", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void onClick(View view) {
        CheckBox t=(CheckBox) view;
        if(t.isChecked())
        {
            saveInSp("cb1",true);
           // Intent intent = new Intent(getApplicationContext(),ResultActivity.class);//
           // intent.putExtra("checkBoxStatus",true);//
        }
        else {
            saveInSp("cb1",false);
          //  Intent intent = new Intent(getApplicationContext(),ResultActivity.class);//
           // intent.putExtra("checkBoxStatus",false);//
        }

    }*/
    }
}







