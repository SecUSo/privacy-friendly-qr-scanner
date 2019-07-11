package com.secuso.privacyFriendlyCodeScanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

public class WifiResultFragment extends ResultFragment {

    private String pw;
    private String ssid;

    public WifiResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView resultText = (TextView) v.findViewById(R.id.result_field_wifi);

        final String wifiResult = qrCodeString;


        String[] content = wifiResult.substring(wifiResult.indexOf(":") + 1).split(";");
        int ssid_id = 0;
        int encryption_id = 0;
        int pw_id = 0;
        for(int i=0; i < content.length; i++) {
            if(content[i].startsWith("S:")) ssid_id = i;
            if(content[i].startsWith("T:")) encryption_id = i;
            if(content[i].startsWith("P:")) pw_id = i;
        }

        ssid = content[ssid_id].substring(2);
        String encryption = content[encryption_id].substring(2);
        pw = content[pw_id].substring(2);

        TextView resultField = (TextView) v.findViewById(R.id.result_field_wifi);
        TextView resultFieldEncryption = (TextView) v.findViewById(R.id.result_field_wifi_encryption);
        TextView resultFieldPassword = (TextView) v.findViewById(R.id.result_field_wifi_pw);
        resultField.setText("SSID: " + ssid);
        resultFieldEncryption.setText(getString(R.string.encryption) + ": " + encryption);
        resultFieldPassword.setText("PW: " + pw);


        return v;
    }

    public void onProceedPressed(final Context context, final String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.wifi_array, new DialogInterface.OnClickListener() {
                    @Override
                    @SuppressWarnings("deprecation")
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(pw);

                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
