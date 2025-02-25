/*
    Privacy Friendly QR Scanner
    Copyright (C) 2023-2025 Privacy Friendly QR Scanner authors and SECUSO

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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter

import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.secuso.privacyfriendlycodescanner.qrscanner.R
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.HistoryActivity
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.HistoryAdapter.HistoryItemViewHolder
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.HistoryViewModel

class DeleteActionMode(private val historyActivity: HistoryActivity) : ActionMode.Callback {
    var isDeleteModeActive = false
        private set
    var isSelectAll = false
        private set
    var selectList = ArrayList<HistoryItem>()
        private set

    override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
        val menuInflater = mode.menuInflater
        menuInflater.inflate(R.menu.multi_delete_menu, menu)
        mode.title = historyActivity.resources.getQuantityString(R.plurals.entries_selected, 0, 0)
        ViewModelProvider(historyActivity)[HistoryViewModel::class.java].selectedItemCount.observe(historyActivity) { count: Int ->
            mode.title = historyActivity.resources.getQuantityString(R.plurals.entries_selected, count, count)
        }
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu?): Boolean {
        isDeleteModeActive = true
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_delete) {
            MaterialAlertDialogBuilder(historyActivity)
                .setTitle(R.string.delete_items)
                .setMessage(R.string.Del)
                .setPositiveButton(R.string.delete) { _, _ ->
                    for (s in selectList) {
                        historyActivity.historyAdapter.deleteEntry(s)
                    }
                    mode.finish()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        } else if (id == R.id.menu_select_all) {
            if (selectList.size == historyActivity.historyAdapter.itemCount) {
                //unselect all
                isSelectAll = false
                selectList.clear()
            } else {
                isSelectAll = true
                selectList.clear()
                selectList.addAll(historyActivity.historyAdapter.historyEntries)
            }
            ViewModelProvider(historyActivity)[HistoryViewModel::class.java].setSelectedItemCount(selectList.size)
            historyActivity.historyAdapter.notifyDataSetChanged()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        isDeleteModeActive = false
        isSelectAll = false
        selectList.clear()
        ViewModelProvider(historyActivity)[HistoryViewModel::class.java].setSelectedItemCount(0)
        historyActivity.historyAdapter.notifyDataSetChanged()
    }

    fun selectItem(holder: HistoryItemViewHolder) {
        val s: HistoryItem = historyActivity.historyAdapter.historyEntries.get(holder.adapterPosition)
        if (!selectList.contains(s)) {
            holder.binding.itemView.isChecked = true
            selectList.add(s)
        } else {
            holder.binding.itemView.isChecked = false
            selectList.remove(s)
        }
        ViewModelProvider(historyActivity)[HistoryViewModel::class.java].setSelectedItemCount(selectList.size)
    }
}