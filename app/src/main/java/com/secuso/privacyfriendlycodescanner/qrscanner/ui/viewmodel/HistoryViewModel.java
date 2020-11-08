package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryViewModel extends AndroidViewModel {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final MediatorLiveData<List<HistoryItem>> historyItemsLiveData = new MediatorLiveData<>();

    public HistoryViewModel(@NonNull final Application application) {
        super(application);

        loadHistories();
    }

    public LiveData<List<HistoryItem>> getHistoryItems() {
        return historyItemsLiveData;
    }

    private void loadHistories() {
        LiveData<List<HistoryItem>> historyEntries = AppRepository.getInstance(getApplication()).getHistoryEntriesLiveData();
        historyItemsLiveData.addSource(historyEntries, historyItemsLiveData::setValue);
    }
}
