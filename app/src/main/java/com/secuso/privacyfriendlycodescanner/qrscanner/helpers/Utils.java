package com.secuso.privacyfriendlycodescanner.qrscanner.helpers;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL;

public class Utils {

    public static Bitmap generateCode(String data, BarcodeFormat format, Map<EncodeHintType, Object> hints, Map<ResultMetadataType,Object> metadata) {
        if(hints == null) {
            hints = new EnumMap<>(EncodeHintType.class);
        }

        if(!hints.containsKey(ERROR_CORRECTION) && metadata != null && metadata.containsKey(ERROR_CORRECTION_LEVEL)) {
            Object ec = metadata.get(ERROR_CORRECTION_LEVEL);
            if(ec != null) {
                hints.put(ERROR_CORRECTION, ec);
            }
        }
        if(!hints.containsKey(ERROR_CORRECTION) && format!=BarcodeFormat.AZTEC) {
            hints.put(ERROR_CORRECTION, ErrorCorrectionLevel.L.name());
        }

        return generateCode(data, getFormat(format), hints); // only reshow as QR Codes
    }

    private static BarcodeFormat getFormat(BarcodeFormat format) {
        switch (format) {
            case EAN_8:
            case UPC_E:
            case EAN_13:
            case UPC_A:
            case QR_CODE:
            case CODE_39:
            case CODE_93:
            case CODE_128:
            case ITF:
            case PDF_417:
            case CODABAR:
            case DATA_MATRIX:
            case AZTEC:
                return format;
            default:
                return BarcodeFormat.QR_CODE;
        }
    }

    public static Bitmap generateCode(String data, BarcodeFormat format, Map<EncodeHintType,?> hints) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result = writer.encode(data, format, 100, 100, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            return bitmap;
        } catch (WriterException e) {
            return null;
        }
    }
}
