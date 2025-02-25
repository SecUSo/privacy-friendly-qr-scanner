/*
    Privacy Friendly QR Scanner
    Copyright (C) 2024-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import java.io.ByteArrayOutputStream

/**
 * This class offers type converters for the room database.
 *
 * @author Christopher Beckmann
 * @see AppDatabase
 */
object Converters {
    @JvmStatic
    @TypeConverter
    fun encodeImage(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    @JvmStatic
    @TypeConverter
    fun decodeImage(encodedImage: String?): Bitmap? {
        if (encodedImage == null) return null
        val decodedBytes = Base64.decode(encodedImage, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @JvmStatic
    @TypeConverter
    fun fromText(text: String?): BarcodeFormat? {
        return try {
            BarcodeFormat.valueOf(text!!)
        } catch (e: IllegalArgumentException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    @JvmStatic
    @TypeConverter
    fun fromBarcodeFormat(bcf: BarcodeFormat): String {
        return bcf.name
    }

    @JvmStatic
    @TypeConverter
    fun fromResultPoints(rp: Array<ResultPoint?>?): String? {
        val gson = Gson()
        return gson.toJson(rp)
    }

    @JvmStatic
    @TypeConverter
    fun convertResultPointFromJson(jsonString: String?): Array<ResultPoint?>? {
        val gson = Gson()
        return gson.fromJson(jsonString, Array<ResultPoint?>::class.java)
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}