package com.secuso.privacyfriendlycodescanner.qrscanner.ui.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.secuso.privacyfriendlycodescanner.qrscanner.R

class RawDataDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val rawData: String = arguments!!.getString("rawDataString") ?: ""
            val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_raw_data, null)
            val textView = dialogView.findViewById<TextView>(R.id.textView)
            textView.text = rawData
            builder.setView(dialogView)
            builder.setTitle(R.string.raw_data)
            builder.setIcon(R.drawable.ic_baseline_qr_code_24dp)
            builder.setCancelable(true)
            builder.setNegativeButton(R.string.okay, null)
            builder.setPositiveButton(R.string.copy_to_clipboard) { _, _ ->
                (it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                    setPrimaryClip(ClipData.newPlainText("Text", rawData))
                }
                Toast.makeText(context, R.string.content_copied, Toast.LENGTH_SHORT)
                    .show()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    companion object {
        const val TAG = "RawDataDialog"
        fun newInstance(rawData: String): RawDataDialogFragment {
            val args = Bundle()
            args.putString("rawDataString", rawData)
            val fragment = RawDataDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}