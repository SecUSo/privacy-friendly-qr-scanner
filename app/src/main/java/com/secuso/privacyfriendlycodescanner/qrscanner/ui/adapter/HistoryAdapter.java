package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.client.result.ResultParser;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.databinding.ItemHistoryCodeBinding;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
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

    private final Context context;
    private final List<HistoryItem> historyEntries;

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
        HistoryItem historyItem = historyEntries.get(i);
        ItemHistoryCodeBinding binding = historyItemViewHolder.binding;
        HistoryItemViewModel viewModel = new HistoryItemViewModel(context, historyItem);
        binding.setViewModel(viewModel);
        binding.itemView.setOnClickListener(viewModel.onClickItem());
        binding.itemView.setOnLongClickListener(viewModel.onLongClickItem());

        Contents.Type contentType = Contents.Type.parseParsedResultType(ResultParser.parseResult(historyItem.getResult()).getType());
        Glide.with(context).load(contentType.getIcon()).placeholder(R.drawable.ic_no_image_accent_24dp).into(binding.itemHistoryImage);

        @DrawableRes Integer codeTypeDrawableRes;
        switch (historyItem.getFormat()) {
            case QR_CODE:
                codeTypeDrawableRes = R.drawable.ic_baseline_qr_code_24dp;
                break;
            default:
                codeTypeDrawableRes = R.drawable.ic_barcode_24dp;
                break;
        }
        Glide.with(context).load(codeTypeDrawableRes).placeholder(R.drawable.ic_no_image_accent_24dp).into(binding.itemHistoryTypeImage);
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
