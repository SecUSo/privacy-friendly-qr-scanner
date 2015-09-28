package com.secuso.privacyFriendlyCodeScanner.Utility;

import android.app.Fragment;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentResult;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.ContactFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.EmailFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.ProductFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.SendEmailFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.SmsFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.TelFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.TextFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.UrlFragment;
import com.secuso.privacyFriendlyCodeScanner.ResultFragments.WifiFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Philipp on 14/09/2015.
 */
public class FragmentGenerator {
    public static Fragment getFragment(IntentResult result) {
        Fragment fragment;
        String format = result.getFormatName();
        String content = result.getContents();

        if(!format.contains("QR_CODE"))
            fragment = new ProductFragment();
        else {
            if (content.startsWith("WIFI:"))
                fragment = new WifiFragment();
            else if (content.startsWith("tel:"))
                fragment = new TelFragment();
            else if (content.startsWith("SMSTO:"))
                fragment = new SmsFragment();
            else if (content.startsWith("mailto:"))
                fragment = new EmailFragment();
            else if (content.startsWith("MATMSG:"))
                fragment = new SendEmailFragment();
            else if (content.startsWith("BEGIN:VCARD"))
                fragment = new ContactFragment();
            else if (content.startsWith("http://") || content.startsWith("https://") || content.startsWith("www."))
                fragment = new UrlFragment();
            else
                fragment = new TextFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("result_content", result.getContents());
        fragment.setArguments(bundle);

        return fragment;
    }

    public static Fragment getFragmentFromContent(String content) {
        Fragment fragment;
        boolean isProduct = false;

        Pattern r = Pattern.compile("^[0-9]\\*\\$");
        Matcher m = r.matcher(content);
        if(m.find()) isProduct = true;

        if (content.startsWith("WIFI:"))
            fragment = new WifiFragment();
        else if (content.startsWith("tel:"))
            fragment = new TelFragment();
        else if (content.startsWith("SMSTO:"))
            fragment = new SmsFragment();
        else if (content.startsWith("mailto:"))
            fragment = new EmailFragment();
        else if (content.startsWith("MATMSG:"))
            fragment = new SendEmailFragment();
        else if (content.startsWith("BEGIN:VCARD"))
            fragment = new ContactFragment();
        else if (content.startsWith("http://") || content.startsWith("https://") || content.startsWith("www."))
            fragment = new UrlFragment();
        else if(isProduct)
            fragment = new ProductFragment();
        else
            fragment = new TextFragment();

        Bundle bundle = new Bundle();
        bundle.putString("result_content", content);
        fragment.setArguments(bundle);

        return fragment;
    }
}
