package com.secuso.privacyfriendlycodescanner.qrscanner.ui.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
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
            val builder = AlertDialog.Builder(this.context)
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
                    QRGeneratorUtils.saveCachedImageToExternalStorage(context)
                    Toast.makeText(
                        context,
                        R.string.image_stored_in_gallery,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "QRCodeImageDialog"
    }
}