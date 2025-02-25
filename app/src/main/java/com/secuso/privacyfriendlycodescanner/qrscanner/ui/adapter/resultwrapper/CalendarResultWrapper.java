/*
    Privacy Friendly QR Scanner
    Copyright (C) 2020-2025 Privacy Friendly QR Scanner authors and SECUSO

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
