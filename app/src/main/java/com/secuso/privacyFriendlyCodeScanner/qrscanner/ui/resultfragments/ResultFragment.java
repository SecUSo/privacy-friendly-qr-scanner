package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.zxing.client.result.ParsedResult;

public abstract class ResultFragment extends Fragment {

    protected ParsedResult parsedResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void putQRCode(@NonNull ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }

    public abstract void onProceedPressed(Context context);
}
