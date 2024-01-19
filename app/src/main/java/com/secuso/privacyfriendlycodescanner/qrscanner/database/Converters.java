package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;

import java.io.ByteArrayOutputStream;

/**
 * This class offers type converters for the room database.
 *
 * @author Christopher Beckmann
 * @see AppDatabase
 */
public final class Converters {

    @TypeConverter
    @Nullable
    public static String encodeImage(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    @TypeConverter
    @Nullable
    public static Bitmap decodeImage(String encodedImage) {
        if (encodedImage == null) return null;
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TypeConverter
    @Nullable
    public static BarcodeFormat fromText(String text) {
        try {
            return BarcodeFormat.valueOf(text);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @TypeConverter
    public static String fromBarcodeFormat(BarcodeFormat bcf) {
        return bcf.name();
    }

    @TypeConverter
    @Nullable
    public static String fromResultPoints(ResultPoint[] rp) {
        Gson gson = new Gson();
        return gson.toJson(rp);
    }

    @TypeConverter
    @Nullable
    public static ResultPoint[] convertResultPointFromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, ResultPoint[].class);
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
