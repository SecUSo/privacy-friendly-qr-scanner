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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.DeleteActionMode;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.HistoryAdapter;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.HistoryViewModel;

/**
 * History Overview that shows a list of scanned codes.
 *
 * @author Christopher Beckmann
 */
public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter mHistoryAdapter;
    private DeleteActionMode mDeleteActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryViewModel mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        mDeleteActionMode = new DeleteActionMode(this);

        mHistoryAdapter = new HistoryAdapter(this, mDeleteActionMode);

        RecyclerView mHistoryRecyclerView = findViewById(R.id.activity_history_list);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        mViewModel.getHistoryItems().observe(this, (list) -> mHistoryAdapter.setHistoryEntries(list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            this.startActionMode(mDeleteActionMode);
            return true;
        }
        return false;
    }

    public HistoryAdapter getHistoryAdapter() {
        return mHistoryAdapter;
    }
}

