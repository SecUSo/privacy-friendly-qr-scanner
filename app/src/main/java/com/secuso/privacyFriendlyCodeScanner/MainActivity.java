package com.secuso.privacyFriendlyCodeScanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.GeneralFragments.AboutFragment;
import com.secuso.privacyFriendlyCodeScanner.GeneralFragments.HistoryFragment;
import com.secuso.privacyFriendlyCodeScanner.GeneralFragments.IntroFragment;
import com.secuso.privacyFriendlyCodeScanner.GeneralFragments.ScanFragment;
import com.secuso.privacyFriendlyCodeScanner.GeneralFragments.SettingsFragment;

import java.io.File;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {

//    private DrawerLayout mDrawerLayout;
//    private ListView mDrawerList;
//    private ActionBarDrawerToggle mDrawerToggle;
//
//    private CharSequence mDrawerTitle;
//    private CharSequence mTitle;
//    private String[] mMenuTitels;

    private LinkedList<Integer> backStack = new LinkedList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTitle = mDrawerTitle = getTitle();
//        mMenuTitels = new String[]{getResources().getString(R.string.app_name),getResources().getString(R.string.history), getResources().getString(R.string.title_activity_settings), getResources().getString(R.string.action_about)};
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//        // set a custom shadow that overlays the main content when the drawer opens
//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
//        // set up the drawer's list view with items and click listener
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mMenuTitels));
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//        // enable ActionBar app icon to behave as action to toggle nav drawer
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // ActionBarDrawerToggle ties together the the proper interactions
//        // between the sliding drawer and the action bar app icon
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        ) {
//            @Override
//            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#024265")));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(prefs.getBoolean("firstVisit", true)) {
            switchToFragment(new IntroFragment(), false);
        }
        else if (savedInstanceState == null) {
                selectItem(0, false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            selectItem(2, false);
            return true;
        }
        else if(id == R.id.action_history) {
            selectItem(1, false);
            return true;
        }
        else if (id == R.id.action_about) {
            selectItem(3, false);
            return true;
        }
        else if (id == R.id.action_home) {
            selectItem(0, false);
            return true;
        }
        else if(id == R.id.action_clear) {
            File sdcard = getFilesDir();
            final File file = new File(sdcard,"history.txt");

            if(!file.exists()) Toast.makeText(this, getResources().getString(R.string.cannot_clear), Toast.LENGTH_SHORT).show();
            else {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
                deleteDialog.setOnCancelListener(new DialogInterface.OnCancelListener () {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }

                });
                deleteDialog.setMessage(R.string.delete_history);
                deleteDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file.delete();
                        selectItem(1,false);
                    }
                });

                deleteDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteDialog.show();
            }
        }

//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void switchToFragment(Fragment frag, boolean back){
        Fragment temp = getFragmentManager().findFragmentById(R.id.container);
        if(!back)
            if(temp instanceof ScanFragment)
                backStack.push(0);
            else if(temp instanceof HistoryFragment)
                backStack.push(1);
            else if(temp instanceof SettingsFragment)
                backStack.push(2);
            else if(temp instanceof AboutFragment)
                backStack.push(3);

        getFragmentManager().beginTransaction().replace(R.id.container, frag).addToBackStack("").commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (backStack.size() < 1)
            finish();
        else {
            int back = backStack.pop();
            selectItem(back, true);
        }
    }

    public void goBack(){
        getFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment()).addToBackStack("").commit();

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position, false);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
//        mTitle = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectItem(int position, boolean back) {
        // update the main content by replacing fragments
        if(position == 0) {
            switchToFragment(new ScanFragment(), back);
            setTitle(R.string.app_name);
        }
        else if(position == 1) {
            switchToFragment(new HistoryFragment(), back);
            setTitle(R.string.history);
        }
        else if(position == 2) {
            switchToFragment(new SettingsFragment(), back);
            setTitle(R.string.title_activity_settings);
        }
        else if(position == 3) {
            switchToFragment(new AboutFragment(), back);
            setTitle(R.string.action_about);
        }

        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
