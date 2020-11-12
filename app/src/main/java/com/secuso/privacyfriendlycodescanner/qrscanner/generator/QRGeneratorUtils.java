package com.secuso.privacyfriendlycodescanner.qrscanner.generator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private static final String IMAGE_FOLDER = "Generated QR Codes";
    private static final String TAG = QRGeneratorUtils.class.getSimpleName();

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
        imageFilePath = new File(imageFilePath, buildFileString());
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
            MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null,
                (path, uri) -> {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            );
        }

    }

    private static @NonNull String buildFileString() {
        // Define name
        StringBuffer sb = new StringBuffer();
        sb.append("QrCode_");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.format(Calendar.getInstance().getTime(), sb, new FieldPosition(SimpleDateFormat.DATE_FIELD));
        sb.append(".png");
        return sb.toString();
    }

    private static @Nullable File writeToFile(@NonNull File file, @NonNull Bitmap image) {
        File outFile = file;
        StringBuilder sb = new StringBuilder(file.toString());

        // if multiple codes are generated on the same day.. name them with numbers
        for(int i = 2; outFile.exists(); i++) {
            sb.delete(sb.length() - 4, sb.length());
            sb.append("_(").append(i).append(").png");
            outFile = new File(sb.toString());
        }

        try(FileOutputStream fOut = new FileOutputStream(outFile)){
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outFile;
    }

}
