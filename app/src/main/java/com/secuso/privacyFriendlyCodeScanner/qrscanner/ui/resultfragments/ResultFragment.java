package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public abstract class ResultFragment extends Fragment {

    protected ParsedResult parsedResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.share,menu);
        inflater.inflate(R.menu.copy,menu);
    }

    public void putQRCode(@NonNull ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }

    public abstract void onProceedPressed(Context context);
}
