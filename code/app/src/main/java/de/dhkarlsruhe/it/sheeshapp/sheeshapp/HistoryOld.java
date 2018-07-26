package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by d0272129 on 24.01.18.
 */
//TODO etwas wird falsch übermittelt
public class HistoryOld {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int numOfSavedEntries;
    private String[] dataAsString;

    public HistoryOld(Context context) {
        pref = context.getSharedPreferences("com.preferences.sheeshapp",0);
        editor = pref.edit();
        loadData();
    }

    private void loadData() {
        numOfSavedEntries = pref.getInt("NUM_OF_SAVED_ENTRIES",0);
        if (numOfSavedEntries>0) {
            dataAsString = new String[numOfSavedEntries];
            for (int i=0; i<dataAsString.length; i++) {
                dataAsString[i] = pref.getString("STATISTIC_ENTRY_"+(i+1),"FehlerBeiCreateData");
            }
        } else {
            dataAsString = new String[1];
            dataAsString[0] = "No HistoryOld available";
        }
    }

    public int getNumOfSavedEntries() {
        return numOfSavedEntries;
    }


    public List<String> getAllChildsOfEntry(int i) {
        String[] data = dataAsString[i].split("#");
        List<String> childs = new ArrayList<>();
        for (int j=1; j<data.length; j++) {
            childs.add(data[j]);
        }
        return childs;
    }

    public String getHeaderOfEntry(int i) {
        if (dataAsString[i] != null) {
            String[] cache = dataAsString[i].split("#");
            return cache[0];
        } else {
            return "Fehler bei Header";
        }
    }

    public String getChildOfEntry(int i, int j) {
        String[] cache = dataAsString[i].split("#");
        return cache[j+1];

    }

    public int getChildValueOfEntry(int i) {
        if (dataAsString[i]!=null) {
            String[] cache = dataAsString[i].split("#");
            return cache.length-1;
        } else {
            return 0;
        }
    }
}
