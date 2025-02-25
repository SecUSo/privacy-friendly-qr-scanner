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

package com.secuso.privacyfriendlycodescanner.qrscanner.backup

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.JsonReader
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppDatabase
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.PreferenceKeys
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
    private fun readDatabase(reader: JsonReader, context: Context) {
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
        db.version = version

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
    private fun readPreferences(reader: JsonReader, context: Context) {
        reader.beginObject()
        val pref: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            when (name) {
                "bool_history", "pref_save_real_image_to_history", "pref_search_engine_enabled", "pref_enable_beep_on_scan", "image_picker_first_click" -> pref
                    .putBoolean(name, reader.nextBoolean())

                PreferenceKeys.SEARCH_ENGINE, PreferenceKeys.APP_THEME -> pref.putString(name, reader.nextString())
                else -> throw RuntimeException("Unknown preference $name")
            }
        }
        pref.commit()
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
            e.printStackTrace()
            false
        }
    }
}