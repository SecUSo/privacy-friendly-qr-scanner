package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating.QrGenerator;

import static android.os.Build.VERSION.SDK_INT;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Button scan_bt;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




/*****************************************************************************/

        scan_bt = (Button) findViewById(R.id.btScan);
        final Activity activity = this;
        scan_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);




                        return;
                    }
                }

               /* IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt(getString(R.string.Scan_qr));
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                //  SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //  integrator.setBeepEnabled(prefs.getBoolean("beep", true));
                integrator.setOrientationLocked(false);
                //integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan(); */
            }
        });
/******************************************************************************************************/
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
          //  return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            /*case R.id.home1:
                Intent m= new Intent(MainActivity.this,MainActivity.class);
                startActivity(m);
                break;*/

            case R.id.nav_scan:
                Intent s= new Intent(MainActivity.this,QrScanner.class);
                startActivity(s);
                break;
            case R.id.nav_generate:
                Intent g= new Intent(MainActivity.this,QrGenerator.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent hs= new Intent(MainActivity.this,History.class);
                startActivity(hs);
                break;

            case R.id.nav_settings:
                Intent st= new Intent(MainActivity.this,Settings.class);
                startActivity(st);
                break;

            case R.id.nav_help:
                Intent h= new Intent(MainActivity.this,Help.class);
                startActivity(h);
                break;



            case R.id.nav_tutorial:
                PrefManager prefManager = new PrefManager(getBaseContext());
                prefManager.setFirstTimeLaunch(true);
                Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;



            case R.id.nav_about:
                Intent a= new Intent(MainActivity.this,About.class);
                startActivity(a);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String dataResult;
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.canncelled_scan, Toast.LENGTH_LONG).show();
            } else {
                dataResult=result.getContents();
                Intent i=new Intent(this, ResultActivity.class);
                i.putExtra("QRResult",dataResult);
                startActivity(i);


                // Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
