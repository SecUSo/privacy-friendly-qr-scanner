package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val scanResult: MutableLiveData<BarcodeResult?> = MutableLiveData()
    private val processingScan: MutableLiveData<Boolean> = MutableLiveData(false)
    private val scanComplete: MutableLiveData<Boolean> = MutableLiveData(false)


    fun getScanResult(): LiveData<BarcodeResult?> {
        return scanResult
    }

    fun isProcessingScan(): LiveData<Boolean> {
        return processingScan
    }

    fun isScanComplete(): LiveData<Boolean> {
        return scanComplete
    }

    fun clearScanResult() {
        scanResult.value = null
        scanComplete.value = false
    }

    fun getBarcodeResultFromImage(uri: Uri) {
        viewModelScope.launch {
            processingScan.value = true
            val originalImage = loadImage(uri)
            val intArray = IntArray(originalImage.width * originalImage.height)
            originalImage.getPixels(
                intArray,
                0,
                originalImage.width,
                0,
                0,
                originalImage.width,
                originalImage.height
            )

            val result = tryDecode(originalImage)
            if (result != null) {
                scanResult.value = BarcodeResult(result, null)
            } else {
                scanResult.value = null
            }
            processingScan.value = false
            scanComplete.value = true
        }
    }

    private suspend fun loadImage(uri: Uri): Bitmap {
        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(
                    getApplication<Application>().applicationContext.contentResolver,
                    uri
                )
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
            } else {
                MediaStore.Images.Media.getBitmap(
                    getApplication<Application>().applicationContext.contentResolver,
                    uri
                )
            }
        }
    }

    private suspend fun Bitmap.scaleDown(maxSize: Int): Bitmap {
        val source = this
        return withContext(Dispatchers.Default) {
            val height: Float
            val width: Float
            if (source.width == 0 || source.width == 0) {
                height = maxSize.toFloat()
                width = maxSize.toFloat()
            } else if (source.width > source.height) {
                height = source.height.toFloat() / source.width.toFloat() * maxSize.toFloat()
                width = maxSize.toFloat()
            } else {
                width = source.width.toFloat() / source.height.toFloat() * maxSize.toFloat()
                height = maxSize.toFloat()
            }
            Bitmap.createScaledBitmap(source, width.toInt(), height.toInt(), true)
        }
    }

    private suspend fun Bitmap.rotate(angle: Float): Bitmap? {
        val source = this
        return withContext(Dispatchers.Default) {
            val matrix = Matrix()
            matrix.postRotate(angle)
            Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }
    }

    private suspend fun getBinaryBitmap(source: Bitmap): BinaryBitmap {
        return withContext(Dispatchers.Default) {
            val width = source.width
            val height = source.height
            val size = width * height

            val bitmapBuffer = IntArray(size)

            source.getPixels(bitmapBuffer, 0, width, 0, 0, width, height)
            val luminanceSource = RGBLuminanceSource(width, height, bitmapBuffer)
            BinaryBitmap(HybridBinarizer(luminanceSource))
        }
    }

    private suspend fun tryDecode(source: Bitmap): Result? {
        return withContext(Dispatchers.Default) {
            val reader = MultiFormatReader()
            val hintsMap: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)
            hintsMap[DecodeHintType.TRY_HARDER] = true
            reader.setHints(hintsMap)

            //Try different resolutions
            val resolutions = intArrayOf(1500, 600, 300, 200, 150, 100)
            for (res in resolutions) {
                val image = source.scaleDown(res)
                try {
                    val result = reader.decodeWithState(getBinaryBitmap(image))
                    Log.d(TAG, "Scan completed (Res: " + image.width + "x" + image.height + ")")
                    return@withContext result
                } catch (e: NotFoundException) {
                    Log.d(
                        TAG, "Scan failed (Res: " + image.width + "x" + image.height + ")"
                    )
                }
            }
            return@withContext null
        }
    }

    companion object {
        const val TAG = "ScannerViewModel"
    }
}