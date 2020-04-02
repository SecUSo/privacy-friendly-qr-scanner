package com.secuso.privacyfriendlycodescanner.qrscanner.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Karola Marky
 * @version 20161214
 * Class structure taken from tutorial at http://www.androidhive.info/2016/05/android-build-intro-slider-app/
 */
public class PrefManager {
    public static final String PREF_SAVE_REAL_IMAGE_TO_HISTORY = "pref_save_real_image_to_history";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "privacy_friendly_apps";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}

