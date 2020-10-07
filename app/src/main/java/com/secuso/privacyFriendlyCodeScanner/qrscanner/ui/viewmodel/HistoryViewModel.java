package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryViewModel extends AndroidViewModel {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private MediatorLiveData<List<HistoryItem>> historyItemsLiveData = new MediatorLiveData<>();

    public HistoryViewModel(@NonNull final Application application) {
        super(application);

        loadHistories();
    }

    public LiveData<List<HistoryItem>> getHistoryItems() {
        return historyItemsLiveData;
    }

    private void loadHistories() {
        executorService.execute(() -> {
                LiveData<List<HistoryItem>> historyEntries = AppRepository.getInstance(getApplication()).getHistoryEntriesLiveData();

                //TODO: this has to be done on the ui thread
                historyItemsLiveData.addSource(historyEntries, data -> {
                    //Log.d("HistoryViewModel", "### \t\t ### \t Number of History Items: \t" + data.size());
                    historyItemsLiveData.setValue((List<HistoryItem>) data);
                });
        });
    }
}
