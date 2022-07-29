package com.secuso.privacyfriendlycodescanner.qrscanner.backup

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.JsonReader
import android.util.Log
import androidx.annotation.NonNull
import androidx.room.Room
import androidx.room.RoomDatabase
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppDatabase
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil.deleteRoomDatabase
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil.deleteTables
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil.readDatabaseContent
import org.secuso.privacyfriendlybackup.api.backup.FileUtil.copyFile
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.exitProcess


class BackupRestorer : IBackupRestorer {

    @Throws(IOException::class)
    private fun readDatabase(@NonNull reader: JsonReader, @NonNull context: Context) {
        reader.beginObject()
        val n1: String = reader.nextName()
        if (n1 != "version") {
            throw RuntimeException("Unknown value $n1")
        }
        val version: Int = reader.nextInt()
        val n2: String = reader.nextName()
        if (n2 != "content") {
            throw RuntimeException("Unknown value $n2")
        }

        val restoreDatabaseName = "restoreDatabase"

        // delete if file already exists
        val restoreDatabaseFile = context.getDatabasePath(restoreDatabaseName)
        if (restoreDatabaseFile.exists()) {
            deleteRoomDatabase(context, restoreDatabaseName)
        }

        // create new restore database
        val restoreDatabase: RoomDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, restoreDatabaseName
        ).build()
        val db = restoreDatabase.openHelper.writableDatabase

        db.beginTransaction()
        db.setVersion(version)

        // make sure no tables are in the database
        deleteTables(db)

        readDatabaseContent(reader, db)

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()

        reader.endObject()

        // copy file to correct location
        val actualDatabaseFile = context.getDatabasePath(AppDatabase.DB_NAME)

        deleteRoomDatabase(context, AppDatabase.DB_NAME)

        copyFile(restoreDatabaseFile, actualDatabaseFile)
        Log.d("PFA BackupRestorer", "Database Restored")

        // delete restore database
        deleteRoomDatabase(context, restoreDatabaseName)
    }

    @Throws(IOException::class)
    private fun readPreferences(@NonNull reader: JsonReader, @NonNull context: Context) {
        reader.beginObject()
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            when (name) {
                "bool_history", "pref_save_real_image_to_history", "pref_search_engine_enabled", "pref_enable_beep_on_scan", "image_picker_first_click" -> pref.edit()
                    .putBoolean(name, reader.nextBoolean()).apply()
                "pref_search_engine" -> pref.edit().putString(name, reader.nextString()).apply()
                else -> throw RuntimeException("Unknown preference $name")
            }
        }
        reader.endObject()
    }

    override fun restoreBackup(context: Context, restoreData: InputStream): Boolean {
        return try {
            val isReader = InputStreamReader(restoreData)
            val reader = JsonReader(isReader)

            // START
            reader.beginObject()
            while (reader.hasNext()) {
                val type: String = reader.nextName()
                when (type) {
                    "database" -> readDatabase(reader, context)
                    "preferences" -> readPreferences(reader, context)
                    else -> throw RuntimeException("Can not parse type $type")
                }
            }
            reader.endObject()
            exitProcess(0)
        } catch (e: Exception) {
            false
        }
    }
}