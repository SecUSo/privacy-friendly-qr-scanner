package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Data Access Object for HistoryItems
 *
 * @see HistoryItem
 *
 * @author Christopher Beckmann
 */
@Dao
public interface HistoryDao {
    @Delete
    void delete(HistoryItem entry);

    @Query("SELECT * FROM Histories")
    List<HistoryItem> getAll();

    @Query("SELECT * FROM Histories")
    LiveData<List<HistoryItem>> getAllLiveData();

    @Insert
    void insert(HistoryItem entry);

    @Query("DELETE FROM Histories")
    void deleteAll();

    @Update
    void update(HistoryItem item);

    @Query("SELECT * FROM Histories WHERE image IS null")
    List<HistoryItem> getAllWithoutImage();
}
