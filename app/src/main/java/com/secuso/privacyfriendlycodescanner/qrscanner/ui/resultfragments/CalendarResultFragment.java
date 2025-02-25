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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.client.result.CalendarParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.CalendarResultAdapter;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.resultwrapper.CalendarResultWrapper;

public class CalendarResultFragment extends ResultFragment {

    CalendarResultWrapper result;
    RecyclerView resultList;

    public CalendarResultFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        CalendarParsedResult calendarParsedResult = (CalendarParsedResult) parsedResult;
        result = new CalendarResultWrapper(calendarParsedResult);

        View v = inflater.inflate(R.layout.fragment_result_calendar, container, false);

        resultList = v.findViewById(R.id.fragment_result_recycler_view);
        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setAdapter(new CalendarResultAdapter(result));

        return v;
    }

    public void onProceedPressed(Context context) {
        new MaterialAlertDialogBuilder(context).setTitle(R.string.choose_action)
                .setItems(R.array.calendar_array, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent event = new Intent(Intent.ACTION_INSERT);
                            event.setData(CalendarContract.Events.CONTENT_URI);

                            event.putExtra(CalendarContract.Events.TITLE, result.getTitle());
                            event.putExtra(CalendarContract.Events.DESCRIPTION, result.getDescription());
                            event.putExtra(CalendarContract.Events.EVENT_LOCATION, result.getLocation());
                            event.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, result.isAllDayEvent());
                            event.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, result.getStartTimeMS());
                            event.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, result.getEndTimeMS());

                            String caption = getResources().getStringArray(R.array.calendar_array)[0];
                            startActivity(Intent.createChooser(event, caption));
                            break;

                        case 1:
                        default:
                    }
                })
                .show();
    }
}
