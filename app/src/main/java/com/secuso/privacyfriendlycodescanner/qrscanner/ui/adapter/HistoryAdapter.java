package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.client.result.ResultParser;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.databinding.ItemHistoryCodeBinding;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.HistoryActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ResultActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.HistoryItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the RecyclerView in {@link HistoryActivity}.
 *
 * @author Christopher Beckmann
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {

    private final HistoryActivity activity;
    private final List<HistoryItem> historyEntries;
    private final DeleteActionMode deleteActionMode;

    public HistoryAdapter(HistoryActivity activity, DeleteActionMode deleteActionMode) {
        this.activity = activity;
        this.historyEntries = new ArrayList<>();
        this.deleteActionMode = deleteActionMode;

        setHasStableIds(true);
    }

    public void setHistoryEntries(List<HistoryItem> entries) {
        historyEntries.clear();
        historyEntries.addAll(entries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemHistoryCodeBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_history_code,
                viewGroup,
                false
        );
        return new HistoryItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder historyItemViewHolder, int i) {
        HistoryItem historyItem = historyEntries.get(i);
        ItemHistoryCodeBinding binding = historyItemViewHolder.binding;
        HistoryItemViewModel viewModel = new HistoryItemViewModel(activity, historyItem);
        binding.setViewModel(viewModel);

        binding.itemView.setOnLongClickListener(v -> {
            if (!deleteActionMode.isDeleteModeActive()) {
                ((AppCompatActivity) v.getContext()).startActionMode(deleteActionMode);
                deleteActionMode.selectItem(historyItemViewHolder);
            } else {
                deleteActionMode.selectItem(historyItemViewHolder);
            }
            return true;
        });

        binding.itemView.setOnClickListener(v -> {
            if (deleteActionMode.isDeleteModeActive()) {
                deleteActionMode.selectItem(historyItemViewHolder);
            } else {
                ResultActivity.startResultActivity(activity, historyItem);
            }
        });

        if (deleteActionMode.isSelectAll()) {
            binding.itemView.setChecked(true);
        } else {
            binding.itemView.setChecked(deleteActionMode.getSelectList().contains(historyItem));
        }

        Contents.Type contentType = Contents.Type.parseParsedResultType(ResultParser.parseResult(historyItem.getResult()).getType());
        Glide.with(activity).load(AppCompatResources.getDrawable(activity, contentType.getIcon())).placeholder(AppCompatResources.getDrawable(activity, R.drawable.ic_no_image_accent_24dp)).into(binding.itemHistoryImage);

        @DrawableRes int codeTypeDrawableRes = Utils.getBarcodeFormatIcon(historyItem.getFormat());
        Glide.with(activity).load(AppCompatResources.getDrawable(activity, codeTypeDrawableRes)).placeholder(AppCompatResources.getDrawable(activity, R.drawable.ic_no_image_accent_24dp)).into(binding.itemHistoryTypeImage);
    }

    @Override
    public int getItemCount() {
        return historyEntries.size();
    }

    public void deleteEntry(HistoryItem item) {
        AppRepository.getInstance(activity).deleteHistoryEntry(item);
    }

    @Override
    public long getItemId(int i) {
        return historyEntries.get(i).get_id();
    }

    public List<HistoryItem> getHistoryEntries() {
        return historyEntries;
    }

    public static class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryCodeBinding binding;

        public HistoryItemViewHolder(@NonNull ItemHistoryCodeBinding binding) {
            super(binding.itemView);
            this.binding = binding;
        }
    }
}
