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

import android.os.Parcel;
import android.os.Parcelable;

import com.journeyapps.barcodescanner.BarcodeResult;

public class ParcelableBarcodeResultDecorator implements Parcelable {
    private final BarcodeResult barcodeResult;

    public ParcelableBarcodeResultDecorator(final BarcodeResult barcodeResult) {
        this.barcodeResult = barcodeResult;
    }

    protected ParcelableBarcodeResultDecorator(Parcel in) {
        this.barcodeResult = new BarcodeResult(
                ((ParcelableResultDecorator) in.readParcelable(ParcelableResultDecorator.class.getClassLoader())).getResult(),
                ((ParcelableSourceDataDecorator) in.readParcelable(ParcelableSourceDataDecorator.class.getClassLoader())).getSourceData()
        );
    }

    public BarcodeResult getResult() {
        return barcodeResult;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new ParcelableResultDecorator(barcodeResult.getResult()), 0);
        //dest.writeParcelable(new ParcelableSourceDataDecorator(barcodeResult.get), 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableBarcodeResultDecorator> CREATOR = new Creator<ParcelableBarcodeResultDecorator>() {
        @Override
        public ParcelableBarcodeResultDecorator createFromParcel(Parcel in) {
            return new ParcelableBarcodeResultDecorator(in);
        }

        @Override
        public ParcelableBarcodeResultDecorator[] newArray(int size) {
            return new ParcelableBarcodeResultDecorator[size];
        }
    };
}
