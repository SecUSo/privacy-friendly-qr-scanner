package com.secuso.privacyfriendlycodescanner.qrscanner.backup

import android.content.Context
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer
import java.io.InputStream

class BackupRestorer : IBackupRestorer {
    override fun restoreBackup(context: Context, restoreData: InputStream): Boolean {
        TODO("Not yet implemented")
    }
}