package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Informatik on 29.11.2017.
 */

public class HistoryFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int numOfSavedEntries;
    int[] numOfSwitchedCoal;
    String[] friendsAsString, dateStart, dateEnd, totalTime;
    String allDataAsString[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        init();
        // preparing list data
        if(numOfSavedEntries>0) {
            expListView = (ExpandableListView)rootView.findViewById(R.id.stExpListView);
            createAllData();
            prepareListData();
            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
        }
        listAdapter.notifyDataSetChanged();
        return rootView;
    }

    private void init() {
        pref = this.getActivity().getSharedPreferences("com.preferences.sheeshapp", 0);
        editor = pref.edit();
        numOfSavedEntries = pref.getInt("NUM_OF_SAVED_ENTRIES",0);
        numOfSwitchedCoal = new int[numOfSavedEntries+1];
        friendsAsString = new String[numOfSavedEntries+1];
        dateStart = new String[numOfSavedEntries+1];
        dateEnd = new String[numOfSavedEntries+1];
        totalTime = new String[numOfSavedEntries+1];
        allDataAsString = new String[numOfSavedEntries+1];
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding header data
        for(int i=1; i<=numOfSavedEntries; i++) {
            listDataHeader.add(dateStart[i]);
        }
        // Adding child data
        for(int i=1; i<=numOfSavedEntries; i++) {
            List<String> list = new ArrayList<String>();
            list.add("Mit: "+friendsAsString[i]);
            list.add("Gesamtzeit: "+totalTime[i]);
            list.add("Gedrehte Kohle: " + numOfSwitchedCoal[i]);
            list.add("Endzeitpunkt: " + dateEnd[i]);
            listDataChild.put(listDataHeader.get(i-1), list);
        }
        /*
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        List<String> dawidsData = new ArrayList<String>();
        dawidsData.add("Jahre: 20");
        dawidsData.add("Nachname: Maruszczyk");
        dawidsData.add("Hobbys: keine");
        dawidsData.add("Geburtstag: 05.04.1997");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), dawidsData);
        */

    }


    private void createAllData() {
        for(int i=1; i<=numOfSavedEntries; i++) {
            allDataAsString[i] = pref.getString("STATISTIC_ENTRY_"+i,"FehlerBeiCreateData");
            String[] cache= allDataAsString[i].split("#");
            dateStart[i] = cache[0];
            friendsAsString[i] = cache[1];
            totalTime[i] = cache[2];
            numOfSwitchedCoal[i] = Integer.parseInt(cache[3]);
            dateEnd[i] = cache[4];
        }
    }

    @Override
    public void onResume() {
        createAllData();
        prepareListData();
        listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onPause() {
        listAdapter.notifyDataSetChanged();
        super.onPause();
    }
}
