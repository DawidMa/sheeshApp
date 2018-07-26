package de.dhkarlsruhe.it.sheeshapp.sheeshapp.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by Informatik on 29.11.2017.
 */

public class HistoryFragment extends Fragment {

    private ExpandableListAdapter newAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;


    private View rootView;

    private List<History> histories;

    private UserSessionObject session;
    private Gson json = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        session = new UserSessionObject(getActivity());
        // preparing list data
        getOnlineData();

       //listAdapter.notifyDataSetChanged();
        return rootView;
    }

    private void getOnlineData() {
        long id = session.getUser_id();
        StringRequest request = new StringRequest(ServerConstants.URL_HISTORIES + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                positiveResponse(string);
                showList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),"Error loading History",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    private void positiveResponse(String string) {
        Type listType = new TypeToken<List<History>>() {}.getType();
        histories = json.fromJson(string, listType);
    }

    private void showList() {
        expListView = rootView.findViewById(R.id.stExpListView);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        prepareListData();
        newAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listDataChild);
        // setting list adapter
        expListView.setAdapter(newAdapter);
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding header data
        // Adding child data
        for(int i=0; i<histories.size(); i++) {
            listDataHeader.add(histories.get(i).getDate());
            List<String> pimbloktos = new ArrayList<>();
            pimbloktos.add(histories.get(i).getLocation());
            pimbloktos.add(histories.get(i).getParticipants());
            pimbloktos.add(histories.get(i).getSessionName());
            pimbloktos.add(histories.get(i).getDuration()+"");
            pimbloktos.add(histories.get(i).getTotalShishas()+"");
            listDataChild.put(listDataHeader.get(i), pimbloktos);
        }

    }

    @Override
    public void onResume() {
        if (histories!=null) {
            //prepareListData();
        }
       // listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onPause() {
        //listAdapter.notifyDataSetChanged();
        super.onPause();
    }
}
