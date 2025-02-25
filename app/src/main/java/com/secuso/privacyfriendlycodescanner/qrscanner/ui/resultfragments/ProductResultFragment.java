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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.util.WebSearchUtil;

public class ProductResultFragment extends ResultFragment {

    public ProductResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_product, container, false);

        TextView resultText = v.findViewById(R.id.fragment_result_product_content);

        resultText.setText(getProductID(parsedResult));

        return v;
    }

    private String getProductID(ParsedResult parsedResult) {
        if (parsedResult instanceof ProductParsedResult) {
            ProductParsedResult result = (ProductParsedResult) parsedResult;
            return result.getProductID();
        } else if (parsedResult instanceof ExpandedProductParsedResult) {
            ExpandedProductParsedResult result = (ExpandedProductParsedResult) parsedResult;
            return result.getProductID();
        } else {
            return parsedResult.getDisplayResult();
        }
    }

    public void onProceedPressed(Context context) {
        WebSearchUtil.openWebSearchDialog(context, getProductID(parsedResult));
    }

    @Override
    public String getProceedButtonTitle(Context context) {
        return context.getString(R.string.action_search);
    }
}
