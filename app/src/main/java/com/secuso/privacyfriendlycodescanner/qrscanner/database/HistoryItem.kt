package com.secuso.privacyfriendlycodescanner.qrscanner.database

import android.graphics.Bitmap
import android.os.Parcel
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.secuso.privacyfriendlycodescanner.qrscanner.database.Converters.decodeImage
import com.secuso.privacyfriendlycodescanner.qrscanner.database.Converters.encodeImage

/**
 * Data class for the Histories table. The saved [Result] does not include
 * the ResultMetadata. The Bitmap is saved as a Base64 String.
 *
 * @see Result
 *
 * @see Converters
 *
 *
 * @author Christopher Beckmann
 */
@Entity(tableName = "Histories")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    private var _id: Int = 0,
    var image: Bitmap? = null,
    var text: String = "",
    var rawBytes: ByteArray? = null,
    var numBits: Int = 0,
    var resultPoints: Array<ResultPoint?>? = null,
    var format: BarcodeFormat? = null,
    var timestamp: Long = 0
) {
    fun get_id(): Int {
        return _id
    }

    fun set_id(_id: Int) {
        this._id = _id
    }

    @Ignore
    constructor() : this(
        _id = 0
    )

    @Ignore
    constructor(`in`: Parcel) : this() {
        _id = `in`.readInt()
        image = decodeImage(`in`.readString())
        text = `in`.readString()!!
        rawBytes = `in`.createByteArray()
        numBits = `in`.readInt()
        timestamp = `in`.readLong()
        format = BarcodeFormat.values()[`in`.readInt()]
        resultPoints = arrayOfNulls(`in`.readInt())
        for (i in resultPoints!!.indices) {
            resultPoints!![i] = ResultPoint(`in`.readFloat(), `in`.readFloat())
        }
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(_id)
        dest.writeString(encodeImage(image))
        dest.writeString(text)
        dest.writeByteArray(rawBytes)
        dest.writeInt(numBits)
        dest.writeLong(timestamp)
        dest.writeInt(format!!.ordinal)
        dest.writeInt(if (resultPoints != null) resultPoints!!.size else 0)
        for (rp in resultPoints!!) {
            dest.writeFloat(rp!!.x)
            dest.writeFloat(rp.y)
        }
    }

    fun describeContents(): Int {
        return 0
    }

    val result: Result
        //    public static final Creator<HistoryItem> CREATOR = new Creator<HistoryItem>() {
        get() = Result(text, rawBytes, numBits, resultPoints, format, timestamp)
}