/*
    Privacy Friendly QR Scanner
    Copyright (C) 2020-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class serves as an abstraction layer between the database and the app logic.
 * <br>
 * These methods will access the database in a different thread:
 * <ul>
 *     <li>{@link #insertHistoryEntry(HistoryItem)}</li>
 *     <li>{@link #deleteHistoryEntry(HistoryItem)}</li>
 *     <li>{@link #deleteAllHistoryEntries()}</li>
 * </ul>
 * The method {@link #getHistoryEntriesLiveData()} has to be called on a background thread.
 *
 * @author Christopher Beckmann
 */
public class AppRepository {

    private static AppRepository INSTANCE = null;

    private final AppDatabase appDatabase;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public synchronized static AppRepository getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new AppRepository(context);
        }
        return INSTANCE;
    }

    private AppRepository(Context context) {
        this.appDatabase = AppDatabase.getInstance(context);
    }

    public void deleteHistoryEntry(HistoryItem entry) {
        executor.execute(() -> appDatabase.historyDao().delete(entry));
    }

    public void insertHistoryEntry(HistoryItem entry) {
        executor.execute(() -> appDatabase.historyDao().insert(entry));
    }

    public void updateHistoryEntry(HistoryItem entry) {
        executor.execute(() -> appDatabase.historyDao().update(entry));
    }

    public LiveData<List<HistoryItem>> getHistoryEntriesLiveData() {
        return appDatabase.historyDao().getAllLiveData();
    }

    public void deleteAllHistoryEntries() {
        executor.execute(() -> appDatabase.historyDao().deleteAll());
    }
}
