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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM Histories ORDER BY _id DESC, timestamp DESC")
    List<HistoryItem> getAll();

    @Query("SELECT * FROM Histories ORDER BY _id DESC, timestamp DESC")
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
