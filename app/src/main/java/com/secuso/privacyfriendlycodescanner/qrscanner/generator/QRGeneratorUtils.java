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

package com.secuso.privacyfriendlycodescanner.qrscanner.generator;

import static android.content.Context.WINDOW_SERVICE;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QRGeneratorUtils {

    private static final String IMAGE_FOLDER = "Generated QR Codes";
    private static final String TAG = QRGeneratorUtils.class.getSimpleName();

    private static Uri cache = null;

    public static void shareImage(AppCompatActivity context, Uri imageUri) {
        if (context == null) {
            throw new IllegalArgumentException("Context may not be null.");
        }

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static void purgeCacheFolder(Context context) {
        try {
            File imageFilePath = new File(context.getCacheDir(), "images/");
            File[] files = imageFilePath.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri cacheImage(Context context, Bitmap image) {
        if (image == null) {
            throw new IllegalArgumentException("Image must not be null");
        }
        File imageFilePath = new File(context.getCacheDir(), "images/");
        imageFilePath.mkdir();
        imageFilePath = new File(imageFilePath, buildFileString());
        File file = writeToFile(imageFilePath, image);
        cache = FileProvider.getUriForFile(context, "org.secuso.qrscanner.fileprovider", file);
        return cache;
    }

    public static Uri getCachedUri() {
        return cache;
    }

    private static int getDimension(Context context) {
        //Find screen size
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        return smallerDimension * 3 / 4;
    }

    public static Uri createImage(Context context, String qrInputText, Contents.Type qrType, BarcodeFormat barcodeFormat, String errorCorrectionLevel, boolean dots) {
        int smallerDimension = getDimension(context);

        Bitmap bitmap_ = null;
        if (!dots) {
            //Encode with a QR Code image
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                    null,
                    qrType,
                    barcodeFormat.toString(),
                    smallerDimension);
            try {
                bitmap_ = qrCodeEncoder.encodeAsBitmap(errorCorrectionLevel);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            final Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            QRCode code;
            try {
                code = Encoder.encode(qrInputText, ErrorCorrectionLevel.valueOf(errorCorrectionLevel), hints);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
            bitmap_ = createDotQRCode(code, smallerDimension, smallerDimension, Color.BLACK, Color.WHITE, 1);
        }

        return cacheImage(context, bitmap_);
    }

    private static Bitmap createDotQRCode(QRCode code, int width, int height, @ColorInt int color, @ColorInt int backgroundColor, int quietZone) {
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(backgroundColor);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);


        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalArgumentException();
        }

        final int QR_WIDTH = input.getWidth() + (quietZone * 2);
        final int QR_HEIGHT = input.getHeight() + (quietZone * 2);
        final int OUTPUT_WIDTH = Math.max(width, QR_WIDTH);
        final int OUTPUT_HEIGHT = Math.max(height, QR_HEIGHT);

        final float SCALE = Math.min((float) OUTPUT_WIDTH / (float) QR_WIDTH, (float) OUTPUT_HEIGHT / (float) QR_HEIGHT); //scale from ByteMatrix to Canvas
        final int POSITION_PATTERN_SIZE = 7; //size of the position pattern inside the ByteMatrix
        final float POSITION_PATTERN_RADIUS = (SCALE * POSITION_PATTERN_SIZE) / 2f;
        final float CIRCLE_RADIUS = (SCALE * 0.35f);
        final float PADDING_LEFT = (OUTPUT_WIDTH - (input.getWidth() * SCALE)) / 2.0f + CIRCLE_RADIUS / 2.0f;
        final float PADDING_TOP = (OUTPUT_HEIGHT - (input.getHeight() * SCALE)) / 2.0f + CIRCLE_RADIUS / 2.0f;

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                if (input.get(x, y) == 1) {
                    boolean isInPositionPatternArea = //do not draw anything inside the position pattern regions
                            x <= POSITION_PATTERN_SIZE && y <= POSITION_PATTERN_SIZE || //top left position pattern
                                    x >= input.getWidth() - POSITION_PATTERN_SIZE && y <= POSITION_PATTERN_SIZE || //top right position pattern
                                    x <= POSITION_PATTERN_SIZE && y >= input.getHeight() - POSITION_PATTERN_SIZE; //bottom left position pattern
                    if (!isInPositionPatternArea) {
                        float outputX = PADDING_LEFT + x * SCALE;
                        float outputY = PADDING_TOP + y * SCALE;
                        canvas.drawCircle(outputX + CIRCLE_RADIUS, outputY + CIRCLE_RADIUS, CIRCLE_RADIUS, paint);
                    }
                }
            }
        }

        //draw position patterns
        drawPositionPattern(canvas, color, backgroundColor, PADDING_LEFT, PADDING_TOP, POSITION_PATTERN_RADIUS);
        drawPositionPattern(canvas, color, backgroundColor, PADDING_LEFT + (input.getWidth() - POSITION_PATTERN_SIZE) * SCALE, PADDING_TOP, POSITION_PATTERN_RADIUS);
        drawPositionPattern(canvas, color, backgroundColor, PADDING_LEFT, PADDING_TOP + (input.getHeight() - POSITION_PATTERN_SIZE) * SCALE, POSITION_PATTERN_RADIUS);

        return image;
    }

    private static void drawPositionPattern(Canvas canvas, @ColorInt int color, @ColorInt int backgroundColor, float x, float y, float patternRadius) {
        final float BACKGROUND_CIRCLE_RADIUS = patternRadius * 5f / 7f;
        final float MIDDLE_DOT_RADIUS = patternRadius * 3f / 7f;

        Paint paint = new Paint();
        paint.setColor(color);
        Paint bgPaint = new Paint();
        bgPaint.setColor(backgroundColor);

        canvas.drawCircle(x + patternRadius, y + patternRadius, patternRadius, paint);
        canvas.drawCircle(x + patternRadius, y + patternRadius, BACKGROUND_CIRCLE_RADIUS, bgPaint);
        canvas.drawCircle(x + patternRadius, y + patternRadius, MIDDLE_DOT_RADIUS, paint);
    }

    public static void saveCachedImageToExternalStorage(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), cache);
            saveImageToExternalStorage(context, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveImageToExternalStorage(Context context, Bitmap finalBitmap) throws IOException {
        final String fileName = buildFileString();

        OutputStream oStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            Uri imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

            final String relativePath = Environment.DIRECTORY_PICTURES + File.separator + IMAGE_FOLDER;

            ContentValues newImage = new ContentValues();
            newImage.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            newImage.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            newImage.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativePath);

            Uri imageUri = resolver.insert(imageCollection, newImage);
            oStream = resolver.openOutputStream(imageUri);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
        } else {
            File externalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File myDir = new File(externalPath, IMAGE_FOLDER);
            myDir.mkdirs();
            File file = writeToFile(new File(myDir, fileName), finalBitmap);

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
            );
        }

    }

    private static @NonNull
    String buildFileString() {
        // Define name
        StringBuffer sb = new StringBuffer();
        sb.append("QrCode_");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault());
        sdf.format(Calendar.getInstance().getTime(), sb, new FieldPosition(SimpleDateFormat.DATE_FIELD));
        sb.append(".png");
        return sb.toString();
    }

    private static @Nullable
    File writeToFile(@NonNull File file, @NonNull Bitmap image) {
        File outFile = file;
        StringBuilder sb = new StringBuilder(file.toString());

        // if multiple codes are generated on the same day.. name them with numbers
        for (int i = 2; outFile.exists(); i++) {
            sb.delete(sb.length() - 4, sb.length());
            sb.append("_(").append(i).append(").png");
            outFile = new File(sb.toString());
        }

        try (FileOutputStream fOut = new FileOutputStream(outFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outFile;
    }

    /**
     * Escapes all occurrences of the characters ",", ";", ":" and "\" with a backslash "\".
     *
     * @param value
     * @return
     */
    public static String escapeQRPropertyValue(String value) {
        return value.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace(":", "\\:")
                .replace(";", "\\;");
    }
}
