package com.secuso.privacyfriendlycodescanner.qrscanner

import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import com.secuso.privacyfriendlycodescanner.qrscanner.backup.BackupCreator
import com.secuso.privacyfriendlycodescanner.qrscanner.backup.BackupRestorer
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.PreferenceKeys
import org.secuso.privacyfriendlybackup.api.pfa.BackupManager

class PFACodeScannerApplication : MultiDexApplication(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        BackupManager.backupCreator = BackupCreator()
        BackupManager.backupRestorer = BackupRestorer()


        when (PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(PreferenceKeys.APP_THEME, getString(R.string.pref_app_theme_default))) {
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setMinimumLoggingLevel(Log.INFO).build()
    }

}