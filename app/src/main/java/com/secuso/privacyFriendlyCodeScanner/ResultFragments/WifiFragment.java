package com.secuso.privacyFriendlyCodeScanner.ResultFragments;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiFragment extends ResultFragment {
    private static boolean checked = false;
    private static boolean trust = false;

    public WifiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        rootView = super.setResult(rootView);

        TextView resultText = (TextView) rootView.findViewById(R.id.result_field_wifi);
        resultText.setText(this.result);

        String[] content = result.substring(result.indexOf(":") + 1).split(";");
        int ssid_id = 0;
        int encryption_id = 0;
        int pw_id = 0;
        for(int i=0; i < content.length; i++) {
            if(content[i].startsWith("S:")) ssid_id = i;
            if(content[i].startsWith("T:")) encryption_id = i;
            if(content[i].startsWith("P:")) pw_id = i;
        }

        final String ssid = content[ssid_id].substring(2);
        String encryption = content[encryption_id].substring(2);
        String pw = content[pw_id].substring(2);

        TextView resultField = (TextView) rootView.findViewById(R.id.result_field_wifi);
        TextView resultFieldEncryption = (TextView) rootView.findViewById(R.id.result_field_wifi_encryption);
        TextView resultFieldPassword = (TextView) rootView.findViewById(R.id.result_field_wifi_pw);
        resultField.setText("SSID: " + ssid);
        resultFieldEncryption.setText(getString(R.string.encryption) + ": " + encryption);
        resultFieldPassword.setText("PW: " + pw);

        Button chooseActionButton = (Button) rootView.findViewById(R.id.btnProceed);
        chooseActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_action)
                        .setItems(R.array.wifi_array, new DialogInterface.OnClickListener() {
                            @Override
                            @SuppressWarnings("deprecation")
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        saveScanned(true);
                                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setText(ssid);
                                        saveScanned(true);
                                        break;
                                    case 1:
                                        saveScanned(true);
                                        break;
                                    default:
                                }
                            }
                        });
                builder.create().show();
            }
        });

        return rootView;
    }
}