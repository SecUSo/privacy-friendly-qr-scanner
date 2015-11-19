package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.MyCaptureActivity;
import com.secuso.privacyFriendlyCodeScanner.R;
import com.secuso.privacyFriendlyCodeScanner.Utility.FragmentGenerator;

/**
 * Created by Philipp on 12.09.2015.
 */
public class ScanFragment extends Fragment {
    private String toast;

    public ScanFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ImageView image = (ImageView)view.findViewById(R.id.imageView1);
        image.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
        image.setMinimumHeight(getResources().getDisplayMetrics().widthPixels);

        Button scan = (Button) view.findViewById(R.id.btnScan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        CheckBox cbOrientation = (CheckBox) view.findViewById(R.id.cbOrientation);
        cbOrientation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("lock_orientation", isChecked).commit();
            }
        });
        cbOrientation.setChecked(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("lock_orientation",false));

        return view;
    }

    public void scanFromFragment() {
        IntentIntegrator integrator = IntentIntegrator.forFragment(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        integrator.setBeepEnabled(prefs.getBoolean("beep", true));
        integrator.setOrientationLocked(false);
        integrator.setPrompt(getResources().getString(R.string.scan_text));
        integrator.initiateScan();
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = getResources().getString(R.string.scan_aborted);
            } else {
                ((MainActivity)getActivity()).switchToFragment(FragmentGenerator.getFragment(result),false);
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }
}
