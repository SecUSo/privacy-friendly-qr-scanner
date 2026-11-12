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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.BuildConfig;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultViewModel extends AndroidViewModel {

    private BarcodeResult currentBarcodeResult = null;

    public HistoryItem currentHistoryItem = null;
    public ParsedResult mParsedResult = null;
    public Bitmap mCodeImage = null;
    public boolean mSavedToHistory = false;

    private final SharedPreferences mPreferences;
    public final MutableLiveData<Boolean> productChanged = new MutableLiveData<>();
    public final MutableLiveData<Boolean> lookupFailed = new MutableLiveData<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Call productCall;
    private boolean lookupStarted;

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

    public void lookupProduct() {
        if (lookupStarted || currentHistoryItem == null ||
                !mPreferences.getBoolean("pref_product_lookup", true)) return;
        BarcodeFormat format = currentHistoryItem.getFormat();
        if (format != BarcodeFormat.EAN_13 && format != BarcodeFormat.EAN_8 &&
                format != BarcodeFormat.UPC_A && format != BarcodeFormat.UPC_E) return;
        lookupStarted = true;
        HttpUrl base = HttpUrl.parse(BuildConfig.PRODUCT_BACKEND_URL);
        if (base == null || !base.isHttps()) {
            lookupFailed.setValue(true);
            return;
        }
        HttpUrl.Builder urlBuilder = base.newBuilder().addPathSegment("scan")
                .addQueryParameter("code", currentHistoryItem.getText());
        if (format == BarcodeFormat.UPC_E) urlBuilder.addQueryParameter("format", "UPC_E");
        HttpUrl url = urlBuilder.build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(8, TimeUnit.SECONDS)
                .callTimeout(15, TimeUnit.SECONDS)
                .followRedirects(false).followSslRedirects(false).build();
        productCall = client.newCall(new Request.Builder().url(url).build());
        productCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    Log.w("ProductLookup", "Product lookup failed");
                    lookupFailed.postValue(true);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (Response r = response) {
                    if (r.code() == 404) return;
                    if (!r.isSuccessful() || r.body() == null) {
                        Log.w("ProductLookup", "Backend returned HTTP " + r.code());
                        lookupFailed.postValue(true);
                        return;
                    }
                    JSONObject json = new JSONObject(r.body().string());
                    String name = json.getString("name").trim();
                    String category = json.getString("category").trim();
                    String ingredients = json.getString("ingredients").trim();
                    if (name.isEmpty() || json.isNull("name") ||
                            json.isNull("category") || json.isNull("ingredients")) {
                        throw new JSONException("Invalid product response");
                    }
                    mainHandler.post(() -> {
                        currentHistoryItem.setProductName(name);
                        currentHistoryItem.setProductCategory(category);
                        currentHistoryItem.setProductIngredients(ingredients);
                        if (mSavedToHistory) updateHistoryItem(currentHistoryItem);
                        productChanged.setValue(true);
                    });
                } catch (IOException | JSONException e) {
                    Log.w("ProductLookup", "Invalid product response");
                    lookupFailed.postValue(true);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        if (productCall != null) productCall.cancel();
        super.onCleared();
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
