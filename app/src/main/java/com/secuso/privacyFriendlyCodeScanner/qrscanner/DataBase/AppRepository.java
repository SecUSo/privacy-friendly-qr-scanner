package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import androidx.lifecycle.LiveData;
import android.content.Context;

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

    private AppDatabase appDatabase;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

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
