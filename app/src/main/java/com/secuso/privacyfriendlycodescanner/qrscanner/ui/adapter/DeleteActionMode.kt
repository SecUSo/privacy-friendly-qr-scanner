package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.secuso.privacyfriendlycodescanner.qrscanner.R
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem
import com.secuso.privacyfriendlycodescanner.qrscanner.databinding.ItemHistoryCodeBinding
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.AttributeHelper
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
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu?): Boolean {
        isDeleteModeActive = true
        ViewModelProvider(historyActivity)[HistoryViewModel::class.java].selectedItemCount.observe(historyActivity) { count: Int? ->
            mode.title = String.format(historyActivity.resources.getString(R.string.entries_selected), count)
        }
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
        historyActivity.historyAdapter.notifyDataSetChanged()
    }

    fun selectItem(holder: HistoryItemViewHolder) {
        val s: HistoryItem = historyActivity.historyAdapter.historyEntries.get(holder.adapterPosition)
        if (!selectList.contains(s)) {
            setSelected(holder.binding, true)
            selectList.add(s)
        } else {
            setSelected(holder.binding, false)
            selectList.remove(s)
        }
        ViewModelProvider(historyActivity).get<HistoryViewModel>(HistoryViewModel::class.java).setSelectedItemCount(selectList.size)
    }

    private var defaultColor: ColorStateList? = null

    fun setSelected(binding: ItemHistoryCodeBinding, select: Boolean) {
        if (defaultColor == null) {
            defaultColor = binding.itemView.cardForegroundColor
        }
        if (select) {
            binding.checkbox.isChecked = true
            binding.itemView.setCardForegroundColor(ColorStateList.valueOf(AttributeHelper.getColor(historyActivity, R.attr.colorItemSelected, Color.LTGRAY)))
        } else {
            binding.checkbox.isChecked = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.itemView.setCardForegroundColor(defaultColor)
            }
        }
    }
}