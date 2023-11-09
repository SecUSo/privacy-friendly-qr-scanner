package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import static android.provider.Settings.ACTION_WIFI_ADD_NETWORKS;
import static android.provider.Settings.EXTRA_WIFI_NETWORK_LIST;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.WifiParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.util.ArrayList;

public class WifiResultFragment extends ResultFragment {

    WifiParsedResult result;

    private String pw;
    private String ssid;

    public WifiResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_wifi, container, false);
        result = (WifiParsedResult) parsedResult;

        ssid = result.getSsid();
        String encryption = result.getNetworkEncryption();
        pw = result.getPassword();

        TextView resultField = (TextView) v.findViewById(R.id.result_field_wifi);
        TextView resultFieldEncryption = (TextView) v.findViewById(R.id.result_field_wifi_encryption);
        TextView resultFieldPassword = (TextView) v.findViewById(R.id.result_field_wifi_pw);
        resultField.setText(getString(R.string.ssid_value, ssid));
        resultFieldEncryption.setText(getString(R.string.encryption_value, encryption));
        resultFieldPassword.setText(getString(R.string.password_value, pw));

        return v;
    }

    public void onProceedPressed(final Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(R.array.wifi_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(pw);
                            break;
                        case 1:
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                if ((!result.getNetworkEncryption().equalsIgnoreCase("NOPASS") && !result.getNetworkEncryption().equalsIgnoreCase(""))
                                        && pw == null) {
                                    new MaterialAlertDialogBuilder(context)
                                            .setTitle(android.R.string.dialog_alert_title)
                                            .setMessage(R.string.cannot_connect_to_encrypted_wifi_without_password)
                                            .setNegativeButton(android.R.string.ok, null)
                                            .show();
                                    return;
                                }
                                WifiNetworkSuggestion suggestion;
                                switch (result.getNetworkEncryption().toUpperCase()) {
                                    case "NOPASS":
                                    case "":
                                        suggestion = new WifiNetworkSuggestion.Builder().setSsid(ssid).build();
                                        break;
                                    case "WPA":
                                    case "WPA2":
                                        suggestion = new WifiNetworkSuggestion.Builder().setSsid(ssid).setWpa2Passphrase(pw).build();
                                        break;
                                    case "WPA3":
                                        suggestion = new WifiNetworkSuggestion.Builder().setSsid(ssid).setWpa3Passphrase(pw).build();
                                        break;
                                    default:
                                        new MaterialAlertDialogBuilder(context)
                                                .setTitle(android.R.string.dialog_alert_title)
                                                .setMessage(getString(R.string.unsupported_wifi_encryption, result.getNetworkEncryption()))
                                                .setNegativeButton(android.R.string.ok, null)
                                                .show();
                                        return;
                                }
                                Intent intent = new Intent(ACTION_WIFI_ADD_NETWORKS);
                                ArrayList<WifiNetworkSuggestion> networks = new ArrayList<>();
                                networks.add(suggestion);
                                intent.putExtra(EXTRA_WIFI_NETWORK_LIST, networks);
                                startActivity(intent);
                            } else {
                                new MaterialAlertDialogBuilder(context)
                                        .setTitle(android.R.string.dialog_alert_title)
                                        .setMessage(R.string.android_version_does_not_support_feature)
                                        .setNegativeButton(android.R.string.ok, null)
                                        .show();
                            }
                            break;

                        default:
                    }
                })
                .show();
    }
}
