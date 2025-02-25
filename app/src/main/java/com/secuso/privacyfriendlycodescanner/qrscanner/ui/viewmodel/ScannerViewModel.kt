/*
    Privacy Friendly QR Scanner
    Copyright (C) 2022-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.EnumMap

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val scanResult: MutableLiveData<BarcodeResult?> = MutableLiveData()
    private val processingScan: MutableLiveData<Boolean> = MutableLiveData(false)
    private val scanComplete: MutableLiveData<Boolean> = MutableLiveData(false)

    val onScaleGestureListener = CustomOnScaleGestureListener(this)
    private var _cameraZoomLevel: MutableLiveData<Float> = MutableLiveData(0.0f);

    val cameraZoomLevel: LiveData<Float>
        get() = _cameraZoomLevel

    class CustomOnScaleGestureListener(private val viewModel: ScannerViewModel) : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val timeDelta = detector.timeDelta.toFloat()
            viewModel._cameraZoomLevel.value = 1f.coerceAtMost(0f.coerceAtLeast(viewModel._cameraZoomLevel.value!! + (scaleFactor - 1.0f) * 0.03f * timeDelta))
            return true
        }
    }

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