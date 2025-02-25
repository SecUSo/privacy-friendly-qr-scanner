/*
    Privacy Friendly QR Scanner
    Copyright (C) 2017-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.generator;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.zxing.client.result.ParsedResultType;
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
        LOCATION,

        ADDRESSBOOK,
        PRODUCT,
        URI,
        CALENDAR,
        ISBN,
        VIN;


        /**
         * Returns a localized string representation of this {@link Type}.
         *
         * @param context
         * @return
         */
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
                case ADDRESSBOOK:
                    return context.getResources().getStringArray(R.array.content_types)[14];
                case PRODUCT:
                    return context.getResources().getStringArray(R.array.content_types)[15];
                case URI:
                    return context.getResources().getStringArray(R.array.content_types)[6];
                case CALENDAR:
                    return context.getResources().getStringArray(R.array.content_types)[16];
                case ISBN:
                    return context.getResources().getStringArray(R.array.content_types)[17];
                case VIN:
                    return context.getResources().getStringArray(R.array.content_types)[18];
                default:
                    return context.getResources().getStringArray(R.array.content_types)[0];
            }
        }

        /**
         * Returns the resource reference of a icon representing this {@link Type}.
         *
         * @return
         */
        public @DrawableRes
        Integer getIcon() {
            switch (this) {
                case TEXT:
                    return R.drawable.ic_baseline_subject_24dp;
                case EMAIL:
                    return R.drawable.ic_email_accent_24dp;
                case PHONE:
                    return R.drawable.ic_phone_accent_24dp;
                case SMS:
                    return R.drawable.ic_baseline_sms_24dp;
                case WEB_URL:
                case URI:
                    return R.drawable.ic_baseline_public_24dp;
                case WIFI:
                    return R.drawable.ic_baseline_wifi_24dp;
                case V_CARD:
                case ME_CARD:
                case BIZ_CARD:
                case CONTACT:
                case ADDRESSBOOK:
                    return R.drawable.ic_person_accent_24dp;
                case MARKET:
                    return R.drawable.ic_baseline_shop_24dp;
                case LOCATION:
                    return R.drawable.ic_baseline_place_24dp;
                case PRODUCT:
                    return R.drawable.ic_baseline_shopping_cart_24dp;
                case CALENDAR:
                    return R.drawable.ic_baseline_event_24dp;
                case ISBN:
                case VIN:
                    return R.drawable.ic_barcode_24dp;
                default:
                    return R.drawable.ic_no_image_accent_24dp;
            }
        }

        /**
         * Returns a {@link Type} based on the given {@link ParsedResultType}.
         *
         * @param type
         * @return
         */
        public static Type parseParsedResultType(@NonNull ParsedResultType type) {
            switch (type) {
                case ADDRESSBOOK:
                    return ADDRESSBOOK;
                case EMAIL_ADDRESS:
                    return EMAIL;
                case PRODUCT:
                    return PRODUCT;
                case URI:
                    return URI;
                case TEXT:
                    return TEXT;
                case GEO:
                    return LOCATION;
                case TEL:
                    return PHONE;
                case SMS:
                    return SMS;
                case CALENDAR:
                    return CALENDAR;
                case WIFI:
                    return WIFI;
                case ISBN:
                    return ISBN;
                case VIN:
                    return VIN;
                default:
                    return Type.UNDEFINED;
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
