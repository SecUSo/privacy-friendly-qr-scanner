package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.URIParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLResultFragment extends ResultFragment {

    URIParsedResult result;

    private boolean checked = false;
    private final boolean trust = false;
    private String qrurl;

    public URLResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_result_url, container, false);

        result = (URIParsedResult) parsedResult;

        qrurl = result.getURI();

        TextView resultText = (TextView) v.findViewById(R.id.textDomain);
        TextView furtherInfo = (TextView) v.findViewById(R.id.textLink);
        furtherInfo.setMovementMethod(LinkMovementMethod.getInstance());

        Uri uri = Uri.parse(qrurl);
        String host = uri.getHost();
        if(host == null) {
            host = qrurl;
        }

        Pattern pattern = Pattern.compile("([0-9a-zA-ZäöüÄÖÜß-]*\\.(co.uk|com.de|de.com|co.at|[a-zA-Z]{2,}))$");

        Matcher m = pattern.matcher(host);
        if(m.find()) host = m.group(1);

        int start = qrurl.indexOf(host);
        int end = start + host.length();

        Spannable WordtoSpan = new SpannableString(qrurl);
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        resultText.setText(WordtoSpan);

        // checked = trust = getBoolean("trust", false);

        final CheckBox knowDomain = (CheckBox) v.findViewById(R.id.checkBoxKnowRisks);

        // wenn bereits vertraut wurde, checkbox setzen
        if(trust)
            knowDomain.setChecked(true);

        knowDomain.setOnClickListener(v1 -> checked = knowDomain.isChecked());

        return v;
    }

    public void onProceedPressed(Context context) {
        if(!checked) {
            Toast.makeText(context,R.string.conform_url,Toast.LENGTH_LONG).show();
        } else {
            String caption = "";
            String qrurl3="";
            final String lowercase_qrurl = qrurl.toLowerCase();
            if(!lowercase_qrurl.startsWith("http://") && !lowercase_qrurl.startsWith("https://"))
            {
                qrurl3 = "http://" + qrurl;

                Intent url = new Intent(Intent.ACTION_VIEW);/// !!!!
                url.setData(Uri.parse(qrurl3));
                caption = getResources().getStringArray(R.array.url_array)[0];
                startActivity(Intent.createChooser(url, caption));}
            else {
                Intent url = new Intent(Intent.ACTION_VIEW);/// !!!!
                url.setData(Uri.parse(qrurl));
                caption = getResources().getStringArray(R.array.url_array)[0];
                startActivity(Intent.createChooser(url, caption));

            }
        }
    }
}
