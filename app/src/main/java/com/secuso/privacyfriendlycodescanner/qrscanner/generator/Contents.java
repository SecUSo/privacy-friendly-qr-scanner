package com.secuso.privacyfriendlycodescanner.qrscanner.generator;

import android.content.Context;
import android.provider.ContactsContract;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

/**
 * Created by bassel on 12/17/2017.
 */

public final class Contents {
    private Contents() {
    }

    public enum Type {
        UNDEFINED,
        /**
         * Plain text. Use Intent.putExtra(DATA, string). This can be used for URLs too, but string
         * must include "http://" or "https://"
         */
        TEXT,
        /**
         * An email type. Use Intent.putExtra(DATA, string) where string is the email address.
         */
        EMAIL,
        /**
         * Use Intent.putExtra(DATA, string) where string is the phone number to call.
         */
        PHONE,
        /**
         * An SMS type. Use Intent.putExtra(DATA, string) where string is the number to SMS.
         */
        SMS,
        MMS,
        WEB_URL,
        WIFI,
        V_CARD,
        ME_CARD,
        BIZ_CARD,
        MARKET,
        /**
         * A contact. Send a request to encode it as follows:
         * <p/>
         * import android.provider.Contacts;
         * <p/>
         * Intent intent = new Intent(Intents.Encode.ACTION); intent.putExtra(Intents.Encode.TYPE,
         * CONTACT); Bundle bundle = new Bundle(); bundle.putString(Contacts.Intents.Insert.NAME,
         * "Jenny"); bundle.putString(Contacts.Intents.Insert.PHONE, "8675309");
         * bundle.putString(Contacts.Intents.Insert.EMAIL, "jenny@the80s.com");
         * bundle.putString(Contacts.Intents.Insert.POSTAL, "123 Fake St. San Francisco, CA 94102");
         * intent.putExtra(Intents.Encode.DATA, bundle);
         */
        CONTACT,
        /**
         * A geographic location. Use as follows:
         * Bundle bundle = new Bundle();
         * bundle.putFloat("LAT", latitude);
         * bundle.putFloat("LONG", longitude);
         * intent.putExtra(Intents.Encode.DATA, bundle);
         */
        LOCATION;

        public String toLocalizedString(Context context) {
            switch (this) {
                case TEXT:
                    return context.getResources().getStringArray(R.array.content_types)[1];
                case EMAIL:
                    return context.getResources().getStringArray(R.array.content_types)[2];
                case PHONE:
                    return context.getResources().getStringArray(R.array.content_types)[3];
                case SMS:
                    return context.getResources().getStringArray(R.array.content_types)[4];
                case MMS:
                    return context.getResources().getStringArray(R.array.content_types)[5];
                case WEB_URL:
                    return context.getResources().getStringArray(R.array.content_types)[6];
                case WIFI:
                    return context.getResources().getStringArray(R.array.content_types)[7];
                case V_CARD:
                    return context.getResources().getStringArray(R.array.content_types)[8];
                case ME_CARD:
                    return context.getResources().getStringArray(R.array.content_types)[9];
                case BIZ_CARD:
                    return context.getResources().getStringArray(R.array.content_types)[10];
                case MARKET:
                    return context.getResources().getStringArray(R.array.content_types)[11];
                case CONTACT:
                    return context.getResources().getStringArray(R.array.content_types)[12];
                case LOCATION:
                    return context.getResources().getStringArray(R.array.content_types)[13];
                default:
                    return context.getResources().getStringArray(R.array.content_types)[0];
            }
        }
    }

    public static final String URL_KEY = "URL_KEY";

    public static final String NOTE_KEY = "NOTE_KEY";

    // When using Type.CONTACT, these arrays provide the keys for adding or retrieving multiple
    // phone numbers and addresses.
    public static final String[] PHONE_KEYS = {
            ContactsContract.Intents.Insert.PHONE, ContactsContract.Intents.Insert.SECONDARY_PHONE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE
    };

    public static final String[] PHONE_TYPE_KEYS = {
            ContactsContract.Intents.Insert.PHONE_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
    };

    public static final String[] EMAIL_KEYS = {
            ContactsContract.Intents.Insert.EMAIL, ContactsContract.Intents.Insert.SECONDARY_EMAIL,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL
    };

    public static final String[] EMAIL_TYPE_KEYS = {
            ContactsContract.Intents.Insert.EMAIL_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE
    };
}
