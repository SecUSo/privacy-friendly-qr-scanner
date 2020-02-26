package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

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
        executorService.execute(() ->
                historyItemsLiveData.addSource(AppRepository.getInstance(getApplication()).getHistoryEntriesLiveData(), data -> {
                    //Log.d("HistoryViewModel", "### \t\t ### \t Number of History Items: \t" + data.size());
                    historyItemsLiveData.setValue(data);
                }));
    }
}
