/*
    Privacy Friendly QR Scanner
    Copyright (C) 2019-2025 Privacy Friendly QR Scanner authors and SECUSO

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.result.TextParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.util.WebSearchUtil;

public class TextResultFragment extends ResultFragment {

    TextParsedResult result;

    public TextResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_text, container, false);
        // TODO: v3.0.2 - 1.3.2020 This can currently be more than just Text.
        // - ISBN
        // - VIN
        // - CALENDAR
        // - TEXT
        // result = (TextParsedResult) parsedResult;

        TextView tv = v.findViewById(R.id.result_field_text);
        // TODO: v3.0.2 - 1.3.2020 this is to temporary fix crashes
        tv.setText(parsedResult.getDisplayResult());

        return v;
    }

    public void onProceedPressed(Context context) {
        // TODO: parsedResult used here .. should be result
        WebSearchUtil.openWebSearchDialog(context, parsedResult.getDisplayResult());
    }

    @Override
    public String getProceedButtonTitle(Context context) {
        return context.getString(R.string.action_search);
    }
}
