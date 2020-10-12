package com.secuso.privacyfriendlycodescanner.qrscanner.generator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.WINDOW_SERVICE;

public class QRGeneratorUtils {

    private static Uri cache = null;

    public static void shareImage(AppCompatActivity context, Uri imageUri) {
        if(context == null) {
            throw new IllegalArgumentException("Context may not be null.");
        }

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static Uri cacheImage(Context context, Bitmap image) {
        File imageFilePath = new File(context.getCacheDir(), "images/");
        imageFilePath.mkdir();
        File file = writeToFile(imageFilePath, image);
        cache = FileProvider.getUriForFile(context, "org.secuso.qrscanner.fileprovider", file);
        return cache;
    }

    public static Uri getCachedUri() {
        return cache;
    }

    public static Uri createImage(Context context, String qrInputText, String qrType) {

        //Find screen size
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                qrType,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        Bitmap bitmap_ = null;
        try {
            bitmap_ = qrCodeEncoder.encodeAsBitmap();
            // return bitmap_;

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return cacheImage(context, bitmap_);
    }

    public static void saveCachedImageToExternalStorage(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), cache);
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveImageToExternalStorage(context, bitmap);
    }

    public static void saveImageToExternalStorage(Context context, Bitmap finalBitmap) {
        ContentResolver resolver = context.getContentResolver();

        // Define subfolder path in Image-MediaStorage
        final String relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "Generated_QR_Codes";
        // Define name
        StringBuffer sb = new StringBuffer();
        sb.append("QrCode_");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.format(Calendar.getInstance().getTime(), sb, new FieldPosition(SimpleDateFormat.DATE_FIELD));
        sb.append(".png");
        // Create new media entry
        ContentValues newImage = new ContentValues();
        newImage.put(MediaStore.Images.Media.DISPLAY_NAME, sb.toString());
        newImage.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        newImage.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation);

        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newImage);

        // Write media entry
        OutputStream outStream = null;
        try {
            outStream = resolver.openOutputStream(uri);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File writeToFile(File path, Bitmap image) {
        StringBuffer sb = new StringBuffer();
        sb.append("QrCode_");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.format(Calendar.getInstance().getTime(), sb, new FieldPosition(SimpleDateFormat.DATE_FIELD));
        sb.append(".png");

        File result = new File(path, sb.toString());

        // if multiple codes are generated on the same day.. name them with numbers
        for(int i = 2; result.exists(); i++) {
            sb.delete(17, sb.length());
            sb.append("_(").append(i).append(").png");
            result = new File(path, sb.toString());
        }

        try {
            FileOutputStream fOut = new FileOutputStream(result);
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

}
