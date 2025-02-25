/*
    Privacy Friendly QR Scanner
    Copyright (C) 2022-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.helpers;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultMetadataType;

import java.lang.ref.WeakReference;
import java.util.Map;

public class GenerateCodeTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private final int imageWidth;
    private final int imageHeight;
    private final BarcodeFormat format;
    private final Map<ResultMetadataType, Object> metadata;

    public GenerateCodeTask(ImageView imageView, ProgressBar progressBar, int imageWidth, int imageHeight, BarcodeFormat format, Map<ResultMetadataType, Object> metadata) {
        imageViewWeakReference = new WeakReference<>(imageView);
        progressBarWeakReference = new WeakReference<>(progressBar);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.format = format;
        this.metadata = metadata;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        if (strings != null && strings[0] != null) {
            return Utils.generateCode(strings[0], format, imageWidth, imageHeight, null, metadata);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        final ProgressBar progressBar = progressBarWeakReference.get();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
