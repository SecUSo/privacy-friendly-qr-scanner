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

package com.secuso.privacyfriendlycodescanner.qrscanner.result;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import com.journeyapps.barcodescanner.SourceData;

/**
 * This is a wrapper for the SourceData Class that adds Parcelable functionality to it. Restored
 * SourceData will lose the rotation attribute.
 *
 * @author Christopher Beckmann
 */
public class ParcelableSourceDataDecorator implements Parcelable {
    private SourceData sourceData = null;

    public ParcelableSourceDataDecorator(SourceData sourceData) {
        this.sourceData = sourceData;
    }

    ParcelableSourceDataDecorator(Parcel in) {
        try {
            byte[] data = new byte[in.readInt()];
            in.readByteArray(data);

            int dataWidth = in.readInt();
            int dataHeight = in.readInt();
            int imageFormat = in.readInt();
            int rotation = in.readInt();
            Rect cropRect = in.readParcelable(Rect.class.getClassLoader());

            this.sourceData = new SourceData(data, dataWidth, dataHeight, imageFormat, rotation);
            this.sourceData.setCropRect(cropRect);
        } catch (Exception e) {
            // sourceData will be null if reading fails
        }
    }

    public SourceData getSourceData() {
        return sourceData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sourceData.getData().length);
        dest.writeByteArray(sourceData.getData());

        dest.writeInt(sourceData.getDataWidth());
        dest.writeInt(sourceData.getDataHeight());
        dest.writeInt(sourceData.getImageFormat());
        dest.writeInt(sourceData.isRotated() ? 90 : 0);
        dest.writeParcelable(sourceData.getCropRect(), 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ParcelableSourceDataDecorator> CREATOR = new Parcelable.Creator<ParcelableSourceDataDecorator>() {
        @Override
        public ParcelableSourceDataDecorator createFromParcel(Parcel in) {
            return new ParcelableSourceDataDecorator(in);
        }

        @Override
        public ParcelableSourceDataDecorator[] newArray(int size) {
            return new ParcelableSourceDataDecorator[size];
        }
    };
}
