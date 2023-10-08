package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.resultwrapper;

import com.google.zxing.client.result.CalendarParsedResult;

public class CalendarResultWrapper {

    private final CalendarParsedResult result;

    public CalendarResultWrapper(CalendarParsedResult result) {
        this.result = result;
    }

    public CalendarParsedResult getResult() {
        return result;
    }

    public int getDataCount() {
        int itemCount = 0;

        if(hasTitle())
            itemCount ++;
        if(hasDescription())
            itemCount ++;
        if(hasLocation())
            itemCount ++;
        if (hasStart())
            itemCount ++;
        if (hasEnd())
            itemCount ++;

        return itemCount;
    }

    public String getTitle() {
        return result.getSummary();
    }

    public String getDescription() {
        return result.getDescription();
    }

    public String getLocation() {
        return result.getLocation();
    }

    public boolean isAllDayEvent() {
        return result.isStartAllDay();
    }

    public long getStartTimeMS() {
        return result.getStartTimestamp();
    }

    public long getEndTimeMS() {
        return result.getEndTimestamp();
    }

    public boolean hasTitle() {
        return getTitle() != null;
    }
    public boolean hasDescription() {
        return getDescription() != null;
    }
    public boolean hasLocation() {
        return getLocation() != null;
    }
    public boolean hasStart() {
        return true;
    }
    public boolean hasEnd() {
        return true;
    }
}
