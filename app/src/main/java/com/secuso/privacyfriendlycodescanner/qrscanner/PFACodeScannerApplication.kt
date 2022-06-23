package com.secuso.privacyfriendlycodescanner.qrscanner

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.secuso.privacyfriendlycodescanner.qrscanner.backup.BackupCreator
import com.secuso.privacyfriendlycodescanner.qrscanner.backup.BackupRestorer
import org.secuso.privacyfriendlybackup.api.pfa.BackupManager

class PFACodeScannerApplication : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        BackupManager.backupCreator = BackupCreator()
        BackupManager.backupRestorer = BackupRestorer()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setMinimumLoggingLevel(Log.INFO).build();
    }

}