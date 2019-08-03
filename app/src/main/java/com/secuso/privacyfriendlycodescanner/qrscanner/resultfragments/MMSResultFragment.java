package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public class MMSResultFragment extends ResultFragment {

    private String number;
    private String message;

    public MMSResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        TextView numberField = (TextView) v.findViewById(R.id.textResultMMS);
        TextView contentField = (TextView) v.findViewById(R.id.textContentMMS);

        final String smsResult = qrCodeString;

        String content = smsResult.substring(smsResult.indexOf(":") + 1);
        number = content.substring(0, content.indexOf(":"));
        message = content.substring(content.indexOf(":") + 1);

        numberField.setText(number);
        contentField.setText(message);

        return v;
    }

    public void onProceedPressed(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.mms_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String caption = "";
                        switch (which) {
                            case 0:
                                Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("mmsto:" + number));
                                sms.putExtra("address", number);
                                sms.putExtra("mms_body", message);
                                caption = getResources().getStringArray(R.array.mms_array)[0];
                                startActivity(Intent.createChooser(sms, caption));

                                break;
                            case 1:
                                Intent call = new Intent("android.intent.action.DIAL");
                                call.setData(Uri.parse("tel:" + number));
                                caption = getResources().getStringArray(R.array.mms_array)[1];
                                startActivity(Intent.createChooser(call, caption));

                                break;
                            case 2:
                                Intent contact = new Intent(
                                        ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                                        Uri.parse("tel:" + number));
                                caption = getResources().getStringArray(R.array.mms_array)[2];
                                startActivity(Intent.createChooser(contact, caption));

                                break;

                            default:
                        }
                    }
                });
        builder.create().show();
    }
}
