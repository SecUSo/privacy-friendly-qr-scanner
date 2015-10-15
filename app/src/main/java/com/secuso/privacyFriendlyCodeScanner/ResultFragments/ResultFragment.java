package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.secuso.privacyFriendlyCodeScanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.R;
import com.secuso.privacyFriendlyCodeScanner.Utility.History;

public abstract class ResultFragment extends Fragment {
    protected String result;
    protected Bitmap bitmap;
    protected String toast = "erfolgreich";
    protected boolean fromHistory = false;

    public ResultFragment (){}

    /*
        To be implemented
     */
    protected void createBitmap(){}

    protected View setResult(View view) {
        result  = getArguments().getString("result_content");
        fromHistory = getArguments().getBoolean("history", false);
        if(fromHistory) {
            ((Button) view.findViewById(R.id.btnProceed)).setText(R.string.proceed);
            ((Button) view.findViewById(R.id.btnCancel)).setText(R.string.back);
        }

        Button abortActionButton = (Button) view.findViewById(R.id.btnCancel);
        abortActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            ((MainActivity)getActivity()).selectItem(0,false);
            }
        });

        return view;
    }

    protected void saveScanned(boolean trust) {
        if(!fromHistory)
            History.saveScan(result, trust, getActivity());
    }

    protected void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
        }
    }
}
