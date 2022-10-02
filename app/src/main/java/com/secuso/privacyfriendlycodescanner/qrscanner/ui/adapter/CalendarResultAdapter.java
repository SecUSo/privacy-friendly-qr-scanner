package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.resultwrapper.CalendarResultWrapper;

import java.text.DateFormat;
import java.util.Date;

public class CalendarResultAdapter extends RecyclerView.Adapter<CalendarResultAdapter.TextViewHolder>{

    private final CalendarResultWrapper data;

    private enum CalendarResultItemType {
        TYPE_TITLE,
        TYPE_DESCRIPTION,
        TYPE_LOCATION,
        TYPE_START,
        TYPE_END
    }

    private class CalendarResultItem {
        final CalendarResultItemType type;
        final @StringRes int caption;
        final boolean isAvailable;
        final String content;

        CalendarResultItem(CalendarResultItemType type) {
            this.type = type;
            switch (type) {
                case TYPE_TITLE:
                    this.caption = R.string.item_result_calendar_title;
                    this.content = data.getTitle();
                    this.isAvailable = data.hasTitle();
                    break;
                case TYPE_DESCRIPTION:
                    this.caption = R.string.item_result_calendar_description;
                    this.content = data.getDescription();
                    this.isAvailable = data.hasDescription();
                    break;
                case TYPE_LOCATION:
                    this.caption = R.string.item_result_calendar_location;
                    this.content = data.getLocation();
                    this.isAvailable = data.hasLocation();
                    break;
                case TYPE_START:
                    this.caption = R.string.item_result_calendar_start;
                    this.content = formatDateTime(data.getStartTimeMS(), data.isAllDayEvent());
                    this.isAvailable = data.hasStart();
                    break;
                case TYPE_END:
                    this.caption = R.string.item_result_calendar_end;
                    this.content = formatDateTime(data.getEndTimeMS(), data.isAllDayEvent());
                    this.isAvailable = data.hasEnd();
                    break;
                default:
                    this.caption = 0;
                    this.content = "";
                    this.isAvailable = false;
            }
        }

        private String formatDateTime(long timestampMS, boolean isAllDayEvent) {
            Date datetime = new Date(timestampMS);
            if (isAllDayEvent) {
                return DateFormat.getDateInstance().format(datetime);
            }
            return DateFormat.getDateTimeInstance().format(datetime);
        }
    }

    public CalendarResultAdapter(CalendarResultWrapper result) {
        this.data = result;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.item_result_calendar, viewGroup, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder viewHolder, int i) {
        CalendarResultItem item = new CalendarResultItem(CalendarResultItemType.values()[viewHolder.getItemViewType()]);
        viewHolder.content.setText(item.content);
        viewHolder.caption.setText(item.caption);
    }

    @Override
    public int getItemCount() {
        return data.getDataCount();
    }

    @Override
    public int getItemViewType(int pos) {
        int currentItemCount = 0;

        for (CalendarResultItemType type : CalendarResultItemType.values()) {
            CalendarResultItem item = new CalendarResultItem(type);
            if (item.isAvailable) {
                currentItemCount ++;
            }
            if (pos < currentItemCount) {
                return type.ordinal();
            }
        }

        return 0;
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView caption;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_result_calendar_content);
            caption = itemView.findViewById(R.id.item_result_calendar_caption);
        }
    }
}
