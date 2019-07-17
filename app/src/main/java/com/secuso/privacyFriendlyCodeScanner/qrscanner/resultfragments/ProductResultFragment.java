package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class ProductResultFragment extends ResultFragment {

    private String productResult;

    public ProductResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView resultText = (TextView) v.findViewById(R.id.result_field_product);
        productResult = qrCodeString;
        resultText.setText(productResult);

        return v;
    }

    public void onProceedPressed(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.product_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                String url = productResult;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));

                                String caption = getResources().getStringArray(R.array.product_array)[0];
                                startActivity(Intent.createChooser(intent, caption));
                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
