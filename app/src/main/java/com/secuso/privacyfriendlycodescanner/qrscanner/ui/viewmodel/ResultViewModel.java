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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import static com.secuso.privacyfriendlycodescanner.qrscanner.helpers.PrefManager.PREF_SAVE_REAL_IMAGE_TO_HISTORY;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;

public class ResultViewModel extends AndroidViewModel {

    private BarcodeResult currentBarcodeResult = null;

    public HistoryItem currentHistoryItem = null;
    public ParsedResult mParsedResult = null;
    public Bitmap mCodeImage = null;
    public boolean mSavedToHistory = false;

    private final SharedPreferences mPreferences;

    public ResultViewModel(@NonNull Application application) {
        super(application);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    /**
     * After this function is called the the following values will not be null:
     * <ul>
     *     <li>currentHistoryItem</li>
     *     <li>mCodeImage</li>
     *     <li>mSavedToHistory</li>
     *     <li>mParsedResult</li>
     * </ul>
     * If the state can not be created the activity will call {@link AppCompatActivity#finish()}
     * This method will also update the {@link HistoryItem} in the database with a recreation of the QR Code if the image is missing.
     */
    public void initFromHistoryItem(HistoryItem historyItem) {
        currentHistoryItem = historyItem;
        mParsedResult = ResultParser.parseResult(currentHistoryItem.getResult());
        mCodeImage = currentHistoryItem.getImage();
        if (mCodeImage == null) {
            mCodeImage = Utils.generateCode(currentHistoryItem.getText(), BarcodeFormat.QR_CODE, null);
            currentHistoryItem.setImage(mCodeImage);
            currentHistoryItem.setFormat(BarcodeFormat.QR_CODE);
            updateHistoryItem(currentHistoryItem);
        }
        mSavedToHistory = true;
    }

    public void initFromScan(BarcodeResult barcodeResult) {
        currentBarcodeResult = barcodeResult;
        mParsedResult = ResultParser.parseResult(currentBarcodeResult.getResult());
        fillMissingResultPoints();
        try {
            mCodeImage = currentBarcodeResult.getBitmapWithResultPoints(ContextCompat.getColor(getApplication(), R.color.colorAccent));
        } catch (NullPointerException e) {
            mCodeImage = Utils.generateCode(currentBarcodeResult.getText(), currentBarcodeResult.getBarcodeFormat(), null, currentBarcodeResult.getResult().getResultMetadata());
        }

        currentHistoryItem = Utils.createHistoryItem(mCodeImage, barcodeResult, mPreferences.getBoolean(PREF_SAVE_REAL_IMAGE_TO_HISTORY, false));
        if (mPreferences.getBoolean("bool_history", true)) {
            this.saveHistoryItem(currentHistoryItem);
        }
    }

    public void saveHistoryItem(HistoryItem item) {
        AppRepository.getInstance(getApplication()).insertHistoryEntry(item);
        mSavedToHistory = true;
    }


    public void updateHistoryItem(HistoryItem item) {
        AppRepository.getInstance(getApplication()).updateHistoryEntry(item);
    }

    /**
     * Sometimes the result points array of currentBarcodeResult includes null objects, what might lead to crashes.
     * This function fills the array with duplicates from a valid ResultPoint or with a new ResultPoint with coordinates (0.0,0.0).
     */
    private void fillMissingResultPoints() {
        if (currentBarcodeResult != null && currentBarcodeResult.getResultPoints() != null) {
            ResultPoint[] orig = currentBarcodeResult.getResultPoints();
            ResultPoint valid = new ResultPoint(0, 0);
            for (ResultPoint point : orig) {
                if (point != null) {
                    valid = point;
                    break;
                }
            }
            for (int i = 0; i < orig.length; i++) {
                if (orig[i] == null) {
                    orig[i] = valid;
                }
            }
        }
    }

}
