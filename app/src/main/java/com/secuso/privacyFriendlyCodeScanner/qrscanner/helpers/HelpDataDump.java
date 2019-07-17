/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package com.secuso.privacyfriendlycodescanner.qrscanner.helpers;

import android.content.Context;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Karola Marky
 * @version 20171016
 * Class structure taken from tutorial at http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 * last access 27th October 2016
 */

public class HelpDataDump {

    private Context context;

    public HelpDataDump(Context context) {
        this.context = context;
    }

    public LinkedHashMap<String, List<String>> getDataGeneral() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> general = new ArrayList<String>();
        general.add(context.getResources().getString(R.string.help_whatis_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_whatis), general);

        List<String> features = new ArrayList<String>();
        features.add(context.getResources().getString(R.string.help_usability_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_usability), features);

        List<String> privacy = new ArrayList<String>();
       // privacy.add(context.getResources().getString(R.string.help_privacy_answer));

       // expandableListDetail.put(context.getResources().getString(R.string.help_privacy), privacy);

        List<String> permissions = new ArrayList<String>();
        permissions.add(context.getResources().getString(R.string.help_permission_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_permission), permissions);

        return expandableListDetail;
    }

}
