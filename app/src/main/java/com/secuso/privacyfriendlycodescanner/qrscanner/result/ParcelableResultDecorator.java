package com.secuso.privacyfriendlycodescanner.qrscanner.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

/**
 * This is a wrapper for the Result Class that adds Parcelable functionality to it. Restored
 * Results will lose the ResultMetadata.
 *
 * @author Christopher Beckmann
 */
public final class ParcelableResultDecorator implements Parcelable {
    private Result result = null;

    public ParcelableResultDecorator(Result result) {
        this.result = result;
    }

    ParcelableResultDecorator(Parcel in) {
        try {
            int numBits = in.readInt();
            long timestamp = in.readLong();
            String text = in.readString();
            BarcodeFormat barcodeFormat = BarcodeFormat.values()[in.readInt()];

            byte[] rawBytes = new byte[in.readInt()];
            in.readByteArray(rawBytes);

            ResultPoint[] resultPoints = new ResultPoint[in.readInt()];
            for(int i = 0; i < resultPoints.length; i++) {
                resultPoints[i] = new ResultPoint(in.readFloat(), in.readFloat());
            }

            this.result = new Result(text, rawBytes, numBits, resultPoints, barcodeFormat, timestamp);
        } catch (Exception e) {
            // result will be null if reading fails
        }
    }

    public Result getResult() {
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(result.getNumBits());
        dest.writeLong(result.getTimestamp());
        dest.writeString(result.getText());

        dest.writeInt(result.getBarcodeFormat().ordinal());

        dest.writeInt(result.getRawBytes().length);
        dest.writeByteArray(result.getRawBytes());

        dest.writeInt(result.getResultPoints().length);
        for(ResultPoint rp : result.getResultPoints()) {
            dest.writeFloat(rp.getX());
            dest.writeFloat(rp.getY());
        }

        //dest.writeMap(result.getResultMetadata());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableResultDecorator> CREATOR = new Creator<ParcelableResultDecorator>() {
        @Override
        public ParcelableResultDecorator createFromParcel(Parcel in) {
            return new ParcelableResultDecorator(in);
        }

        @Override
        public ParcelableResultDecorator[] newArray(int size) {
            return new ParcelableResultDecorator[size];
        }
    };
}
