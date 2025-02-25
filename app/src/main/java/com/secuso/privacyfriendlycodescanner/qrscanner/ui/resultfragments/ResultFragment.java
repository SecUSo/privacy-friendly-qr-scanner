/*
    Privacy Friendly QR Scanner
    Copyright (C) 2020-2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.client.result.ParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
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

    public String getProceedButtonTitle(Context context) {
        return context.getString(R.string.choose_action);
    }
}
