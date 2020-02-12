package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.databinding.ItemHistoryCodeBinding;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.HistoryActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel.HistoryItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the RecyclerView in {@link HistoryActivity}.
 *
 * @author Christopher Beckmann
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {

    private Context context;
    private List<HistoryItem> historyEntries;

    public HistoryAdapter(Context context) {
        this.context = context;
        this.historyEntries = new ArrayList<>();

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
        ItemHistoryCodeBinding binding = historyItemViewHolder.binding;
        HistoryItemViewModel viewModel = new HistoryItemViewModel(context, historyEntries.get(i));
        binding.setViewModel(viewModel);
        binding.itemView.setOnClickListener(viewModel.onClickItem());
        binding.itemView.setOnLongClickListener(viewModel.onLongClickItem());

        Bitmap image = historyEntries.get(i).getImage();
        if(image != null) {
            Glide.with(context).load(image).placeholder(R.drawable.ic_no_image_accent_24dp).into(binding.itemHistoryImage);
        } else {
            Glide.with(context).load(R.drawable.ic_no_image_accent_24dp).into(binding.itemHistoryImage);
        }
    }

    @Override
    public int getItemCount() {
        return historyEntries.size();
    }

    @Override
    public long getItemId(int i) {
        return historyEntries.get(i).get_id();
    }

    public static class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryCodeBinding binding;

        public HistoryItemViewHolder(@NonNull ItemHistoryCodeBinding binding) {
            super(binding.itemView);
            this.binding = binding;
        }
    }
}
