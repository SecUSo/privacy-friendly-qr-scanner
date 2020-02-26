package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.result.ProductParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class ProductResultFragment extends ResultFragment {

    ProductParsedResult result;

    public ProductResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_product, container, false);

        result = (ProductParsedResult) parsedResult;

        TextView resultText = v.findViewById(R.id.fragment_result_product_content);

        resultText.setText(result.getProductID());

        return v;
    }

    public void onProceedPressed(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.product_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // TODO: ?! I don't see this working..
                            String url = result.getProductID();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));

                            String caption = getResources().getStringArray(R.array.product_array)[0];
                            startActivity(Intent.createChooser(intent, caption));
                            break;

                        default:
                    }
                });
        builder.create().show();
    }
}
