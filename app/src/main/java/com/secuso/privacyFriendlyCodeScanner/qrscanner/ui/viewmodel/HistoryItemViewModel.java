package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppRepository;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.ResultActivity;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.HistoryAdapter;

import java.text.DateFormat;
import java.util.Date;

/**
 * ViewModel that holds the data for one HistoryItem. This class is used by {@link HistoryAdapter}.
 *
 * @author Christopher Beckmann
 */
public class HistoryItemViewModel extends BaseObservable {

    private Context context;
    private HistoryItem entry;
    private ParsedResult parsed;
    private boolean disabled = false;

    public HistoryItemViewModel(Context context, HistoryItem entry) {
        this.context = context;
        this.entry = entry;
        this.parsed = ResultParser.parseResult(entry.getResult());
    }

    public View.OnClickListener onClickItem() {
        return v -> {
            if(!isDisabled()) {
                ResultActivity.startResultActivity(context, entry);
            }
        };
    }

    public View.OnLongClickListener onLongClickItem() {
        return v -> {
            if(!isDisabled()) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle(R.string.dialog_history_delete_title);
                dialogBuilder.setMessage(R.string.dialog_history_delete_message);
                dialogBuilder.setPositiveButton(R.string.delete, (dialog, which) -> {
                    disabled = true;
                    AppRepository.getInstance(context).deleteHistoryEntry(entry);
                });
                dialogBuilder.setNegativeButton(android.R.string.cancel, null);
                dialogBuilder.create().show();
                return true;
            }
            return false;
        };
    }

    public String getTimestamp() {
        if(entry.getTimestamp() != 0) {
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.format(new Date(entry.getTimestamp()));
        }
        return "";
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public String getType() {
        if(parsed.getType().name() != null) {
            return parsed.getType().name();
        }
        return "";
    }

    public String getText() {
        return parsed.getDisplayResult();
    }


}
