package com.secuso.privacyFriendlyCodeScanner.GeneralFragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.R;
import com.secuso.privacyFriendlyCodeScanner.Utility.FragmentGenerator;

import java.util.List;

public class MyCaptureFragment extends Fragment {

    private CompoundBarcodeView barcodeView;
    private BeepManager beepManager;
    private boolean torchOn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View v;
        v = inflater.inflate(R.layout.fragment_capture, container, false);

        barcodeView = (CompoundBarcodeView) v.findViewById(R.id.barcode_scanner);

        barcodeView.setTorchListener(new CompoundBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                torchOn = true;
            }

            @Override
            public void onTorchOff() {
                torchOn = false;
            }
        });

        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(getActivity());

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.my_capture, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_light) {
            toggleLight();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleLight() {
        if(hasFlash())
            if(torchOn)
                barcodeView.setTorchOff();
            else
                barcodeView.setTorchOn();
    }

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        torchOn = false;
        super.onPause();
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            //Do something with code result
            if (result.getText() != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                boolean shouldBeep = prefs.getBoolean("beep", false);
                beepManager.setBeepEnabled(shouldBeep);
                beepManager.setVibrateEnabled(false);
                if(shouldBeep)
                    beepManager.playBeepSoundAndVibrate();
                ((MainActivity)getActivity()).switchToFragment(FragmentGenerator.getFragment(result),false);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private boolean hasFlash(){
        return getActivity().getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}