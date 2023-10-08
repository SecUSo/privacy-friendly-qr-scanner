package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;

import androidx.databinding.BaseObservable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ResultActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.HistoryAdapter;

import java.util.Date;

/**
 * ViewModel that holds the data for one HistoryItem. This class is used by {@link HistoryAdapter}.
 *
 * @author Christopher Beckmann
 */
public class HistoryItemViewModel extends BaseObservable {

    private final Context context;
    private final HistoryItem entry;
    private final ParsedResult parsed;
    private final String type;
    private boolean disabled = false;

    public HistoryItemViewModel(Context context, HistoryItem entry) {
        this.context = context;
        this.entry = entry;
        this.parsed = ResultParser.parseResult(entry.getResult());
        this.type = Contents.Type.parseParsedResultType(parsed.getType()).toLocalizedString(context);
    }

    public View.OnClickListener onClickItem() {
        return v -> {
            if (!isDisabled()) {
                ResultActivity.startResultActivity(context, entry);
            }
        };
    }

    public View.OnLongClickListener onLongClickItem() {
        return v -> {
            if (!isDisabled()) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.dialog_history_delete_title)
                        .setMessage(R.string.dialog_history_delete_message)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            disabled = true;
                            AppRepository.getInstance(context).deleteHistoryEntry(entry);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            }
            return false;
        };
    }

    public String getTimestamp() {
        if (entry.getTimestamp() != 0) {
            Date now = new Date();
            return DateUtils.getRelativeTimeSpanString(entry.getTimestamp(), now.getTime(), 0, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        }
        return "";
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return parsed.getDisplayResult();
    }


}
