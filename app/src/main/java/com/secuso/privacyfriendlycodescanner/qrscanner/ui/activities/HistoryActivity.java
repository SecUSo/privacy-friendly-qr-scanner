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

