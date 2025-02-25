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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.dialogfragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.secuso.privacyfriendlycodescanner.qrscanner.R
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.QRGeneratorUtils
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.GenerateCodeTask
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.ResultViewModel

/**
 * Dialog fragment to display a QR code image based on the data in the ResultViewModel held by the current activity.
 */
class QRCodeImageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(requireContext())
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_image_view, null)
            val imageView = dialogView.findViewById<ImageView>(R.id.imageView)
            val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
            val btnClose = dialogView.findViewById<FloatingActionButton>(R.id.btnClose)
            val btnStore = dialogView.findViewById<Button>(R.id.btnStore)

            builder.setView(dialogView)
            builder.setCancelable(true)
            val dialog = builder.create()

            val viewModel = ViewModelProvider(it)[ResultViewModel::class.java]
            val result = viewModel.currentHistoryItem.result

            // generate image in async task
            val generateCodeTask = GenerateCodeTask(
                imageView,
                progressBar,
                500,
                500,
                result.barcodeFormat,
                result.resultMetadata
            )
            generateCodeTask.execute(result.text)

            // set listeners for dialog buttons
            btnClose.setOnClickListener { dialog.dismiss() }
            btnStore.setOnClickListener {
                val image = imageView.drawable
                if (image != null) {
                    QRGeneratorUtils.cacheImage(
                        context,
                        (imageView.drawable as BitmapDrawable).bitmap
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            saveImageToStorage()
                        }
                    } else {
                        saveImageToStorage()
                    }
                }
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            saveImageToStorage()
        } else {
            Toast.makeText(context, "storage permission denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveImageToStorage() {
        QRGeneratorUtils.saveCachedImageToExternalStorage(context)
        Toast.makeText(
            context,
            R.string.image_stored_in_gallery,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val TAG = "QRCodeImageDialog"
    }
}