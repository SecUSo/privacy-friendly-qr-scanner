package com.secuso.privacyfriendlycodescanner.qrscanner.helpers;

import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL;

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

public class Utils {

    public static final int DEFAULT_CODE_WIDTH = 100;
    public static final int DEFAULT_CODE_HEIGHT = 100;

    private static BarcodeFormat getFormat(BarcodeFormat format) {
        switch (format) {
            //These are the formats supported by MultiFormatWriter. We encode as QR-Code for everything else.
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

    public static Bitmap generateCode(String data, BarcodeFormat format, Map<EncodeHintType, Object> hints, Map<ResultMetadataType, Object> metadata) {
        return generateCode(data, format, DEFAULT_CODE_WIDTH, DEFAULT_CODE_HEIGHT, hints, metadata);
    }

    public static Bitmap generateCode(String data, BarcodeFormat format, Map<EncodeHintType, Object> hints) {
        return generateCode(data, format, DEFAULT_CODE_WIDTH, DEFAULT_CODE_HEIGHT, hints, null);
    }

    public static Bitmap generateCode(String data, BarcodeFormat original_format, int imgWidth, int imgHeight, Map<EncodeHintType, Object> hints, Map<ResultMetadataType, Object> metadata) {
        BarcodeFormat format = getFormat(original_format);
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            if (hints == null || !format.equals(original_format)) {
                hints = new EnumMap<>(EncodeHintType.class);
            }
            if (!hints.containsKey(ERROR_CORRECTION) && metadata != null && metadata.containsKey(ERROR_CORRECTION_LEVEL) && format.equals(original_format)) {
                Object ec = metadata.get(ERROR_CORRECTION_LEVEL);
                if (ec != null) {
                    hints.put(ERROR_CORRECTION, ec);
                }
            }
            if (!hints.containsKey(ERROR_CORRECTION) && format != BarcodeFormat.AZTEC && format != BarcodeFormat.PDF_417) {
                hints.put(ERROR_CORRECTION, ErrorCorrectionLevel.L.name());
            }
            if (!hints.containsKey(EncodeHintType.CHARACTER_SET)) {
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            }
            BitMatrix result = writer.encode(data, format, imgWidth, imgHeight, hints);
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
