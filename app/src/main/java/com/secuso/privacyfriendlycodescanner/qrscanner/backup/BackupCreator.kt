package com.secuso.privacyfriendlycodescanner.qrscanner.backup

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.JsonWriter
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppDatabase
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil.writeDatabase
import org.secuso.privacyfriendlybackup.api.backup.PreferenceUtil.writePreferences
import org.secuso.privacyfriendlybackup.api.pfa.IBackupCreator
import java.io.OutputStream
import java.io.OutputStreamWriter
import kotlin.text.Charsets.UTF_8


class BackupCreator : IBackupCreator {
    override fun writeBackup(context: Context, outputStream: OutputStream) {
        Log.d("PFA BackupCreator", "createBackup() started")
        val outputStreamWriter = OutputStreamWriter(outputStream, UTF_8)
        val writer = JsonWriter(outputStreamWriter)
        writer.setIndent("")

        try {
            writer.beginObject()

            val callback: SupportSQLiteOpenHelper.Callback =
                object : SupportSQLiteOpenHelper.Callback(AppDatabase.VERSION) {
                    override fun onCreate(db: SupportSQLiteDatabase) {}
                    override fun onUpgrade(
                        db: SupportSQLiteDatabase,
                        oldVersion: Int,
                        newVersion: Int
                    ) {
                    }
                }

            val helper = FrameworkSQLiteOpenHelperFactory().create(
                SupportSQLiteOpenHelper.Configuration.builder(context).name(AppDatabase.DB_NAME)
                    .callback(callback).build()
            )

            val dataBase = helper.writableDatabase

            Log.d("PFA BackupCreator", "Writing database")
            writer.name("database")
            writeDatabase(writer, dataBase)
            dataBase.close()
            Log.d("PFA BackupCreator", "Writing preferences")
            writer.name("preferences")
            val pref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            writePreferences(writer, pref)
            Log.d("PFA BackupCreator", "Writing files")
            writer.endObject()
            writer.close()
        } catch (e: Exception) {
            Log.e("PFA BackupCreator", "Error occurred", e)
            e.printStackTrace()
        }

        Log.d("PFA BackupCreator", "Backup created successfully")
    }
}