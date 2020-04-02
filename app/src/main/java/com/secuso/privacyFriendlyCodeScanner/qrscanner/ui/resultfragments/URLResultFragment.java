package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.WifiParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class WifiResultFragment extends ResultFragment {

    WifiParsedResult result;

    private String pw;
    private String ssid;

    public WifiResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_wifi, container, false);
        result = (WifiParsedResult) parsedResult;

        ssid = result.getSsid();
        String encryption = result.getNetworkEncryption();
        pw = result.getPassword();

        TextView resultField = (TextView) v.findViewById(R.id.result_field_wifi);
        TextView resultFieldEncryption = (TextView) v.findViewById(R.id.result_field_wifi_encryption);
        TextView resultFieldPassword = (TextView) v.findViewById(R.id.result_field_wifi_pw);
        resultField.setText("SSID: " + ssid);
        resultFieldEncryption.setText(getString(R.string.encryption) + ": " + encryption);
        resultFieldPassword.setText("PW: " + pw);

        return v;
    }

    public void onProceedPressed(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.wifi_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(pw);
                            break;

                        default:
                    }
                });
        builder.create().show();
    }
}

