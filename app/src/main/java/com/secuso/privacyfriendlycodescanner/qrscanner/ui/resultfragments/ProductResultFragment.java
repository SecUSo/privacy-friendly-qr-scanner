package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.product_array, (dialog, which) -> {
                    if (which == 0) {
                        // TODO: ?! I don't see this working..
                        String url = getProductID(parsedResult);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        String caption = getResources().getStringArray(R.array.product_array)[0];
                        startActivity(Intent.createChooser(intent, caption));
                    }
                });
        builder.create().show();
    }
}
