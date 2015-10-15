package com.secuso.privacyFriendlyCodeScanner.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Philipp on 14/09/2015.
 */
public class History {

    private static String path = "history.txt";

    public static ArrayList<HistoryEntry> getHistory(Context context) {
        File sdcard = context.getFilesDir();
        File file = new File(sdcard, path);

        ArrayList<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            int id = 0;

            while (true) {
                String content = null;
                boolean trust = false;
                String test_for_trust = br.readLine();

                if(test_for_trust == null) {
                    break;
                }

                if(test_for_trust.startsWith("#####TRUST:")) {
                    test_for_trust = test_for_trust.replaceAll("#","");
                    test_for_trust = test_for_trust.replace("TRUST:","");
                    trust = Boolean.parseBoolean(test_for_trust);
                    test_for_trust = br.readLine();

                }

                if(test_for_trust.equals("#####CONTENT#####")) {
                    content =  "";
                    String temp = br.readLine();
                    while(!temp.equals("#####END#####")) {
                        if(!content.endsWith(""+'\0'))
                            content = content + temp + '\n';
                        else
                            content = content + temp;
                        temp = br.readLine();
                    }
                }

                if(content != null) {
                    HistoryEntry newHistoryEntry = new HistoryEntry(id, content, trust);
                    historyEntries.add(newHistoryEntry);
                }

                id++;
            }
            br.close();
        }
        catch(Exception e) {

        }

        return historyEntries;
    }

    public static void saveScan(String content, boolean trust, Context context) {

        ArrayList<HistoryEntry> historyEntries = History.getHistory(context);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean keepDuplicates = sharedPref.getBoolean("bool_keep_duplicates", true);

        if(!keepDuplicates)
            for(HistoryEntry entry: historyEntries) {
                if(entry.content.contains(content)){
                    historyEntries.remove(entry.id);
                    break;
                }

            }

        for(int i=0; i < historyEntries.size(); i++) {
            historyEntries.get(i).id = i+1;
        }

        HistoryEntry newHistoryEntry = new HistoryEntry(0, content, trust);
        historyEntries.add(0,newHistoryEntry);

        saveHistory(context, historyEntries, false);
    }

    public static void saveHistory(Context context, ArrayList<HistoryEntry> historyEntries, boolean append) {

        File sdcard = context.getFilesDir();
        File file = new File(sdcard, path);

        try {
            if(!file.canWrite()) {
                file.createNewFile();
                Log.e("Problem", "neue Datei erzeugt.");
            }
            else
                Log.e("Problem", "keine neue Datei erzeugt.");

            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, append)));

            for(HistoryEntry entry: historyEntries) {

                if(entry.trust==true || entry.trust==false) {
                    out.append("#####TRUST:"+entry.trust+ "#####" + '\n');
                }
                out.append("#####CONTENT#####" + '\n' + entry.content + '\n');
                out.append("#####END#####" + '\n');
            }
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Problem", "keine neue Datei erzeugt.");
        }
    }

    public static void printHistory(Context context) {
        File sdcard = context.getFilesDir();
        File file = new File(sdcard, path);

        if(!file.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Log.e("TEST", "File found: " + Boolean.toString(br.ready()));
            int id = 0;

            while (true) {
                String start = br.readLine();
                if(start == null) {
                    Log.e("TEST", "File end - lines (" + id + ")");
                    break;
                }
                Log.e("TEST", start);

                id++;
            }
            br.close();
        }
        catch(Exception e) {
            Log.e("ERROR", e.toString());
        }
    }
}
