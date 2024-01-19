package com.secuso.privacyfriendlycodescanner.qrscanner

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ScannerActivity

@RequiresApi(Build.VERSION_CODES.N)
class QuickTileService : TileService() {
    override fun onClick() {
        super.onClick()

        val intent = Intent(this, ScannerActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= 34) {
            startActivityAndCollapse(pendingIntent)
        } else {
            startActivityAndCollapse(intent)
        }
    }
}