package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.ResultViewModel;

public abstract class ResultFragment extends Fragment {

    protected ParsedResult parsedResult;
    protected ResultViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ResultViewModel.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(getActivity()).get(ResultViewModel.class);
        parsedResult = viewModel.mParsedResult;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(ResultViewModel.class);
        parsedResult = viewModel.mParsedResult;
    }

    public void putQRCode(@NonNull ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }

    public abstract void onProceedPressed(Context context);
}
