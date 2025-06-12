package com.secuso.privacyfriendlycodescanner.qrscanner

import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.preference.PreferenceManager
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

fun checkGoodbyeGoogle(context: Context, layoutInflater: LayoutInflater) {

    val showNotice = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_goodbye_google_notice", true);

    if (showNotice) {
        val view = layoutInflater.inflate(R.layout.dialog_goodbye_google, null, false)
        view.findViewById<CheckBox>(R.id.show_notice_checkbox).apply {
            setOnClickListener {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("show_goodbye_google_notice", !isChecked).apply()
            }
        }
        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .setNeutralButton(android.R.string.ok) { _, _ -> }
            .setCancelable(false)
            .create()

        dialog.show()
        dialog.findViewById<TextView>(R.id.text)?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }
    }
}