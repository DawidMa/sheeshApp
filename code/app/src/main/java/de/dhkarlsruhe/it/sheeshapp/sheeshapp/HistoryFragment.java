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

    int numOfSavedEntries;

    private History history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        init();
        // preparing list data
        if(numOfSavedEntries>0) {
            expListView = (ExpandableListView)rootView.findViewById(R.id.stExpListView);
            prepareListData();
            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
        }
       //listAdapter.notifyDataSetChanged();
        return rootView;
    }

    private void init() {
        history = new History(getContext());
        numOfSavedEntries = history.getNumOfSavedEntries();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding header data
        // Adding child data
        for(int i=0; i<numOfSavedEntries; i++) {
            listDataHeader.add(history.getHeaderOfEntry(i));
            // List<String> list = new ArrayList<String>();
            List<String> childs = history.getAllChildsOfEntry(i);
           /* for (int j=0; j<childs.size(); j++) {
                list.add(childs.get(j));
            }*/
            listDataChild.put(listDataHeader.get(i), childs);
        }

    }

    @Override
    public void onResume() {
        prepareListData();
       // listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onPause() {
        //listAdapter.notifyDataSetChanged();
        super.onPause();
    }
}
