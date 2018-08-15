package de.dhkarlsruhe.it.sheeshapp.sheeshapp.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
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
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
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
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView tvInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        session = new UserSessionObject(getActivity());
        preferences = getContext().getSharedPreferences(SharedPrefConstants.HISTORY, Context.MODE_PRIVATE);
        editor = preferences.edit();
        tvInfo = rootView.findViewById(R.id.tvHitoryInfo);
        histories = new ArrayList<>();
        offlineData();
        onlineData();
        return rootView;
    }

    private void offlineData() {
        List<History> offlineHistories = getHistoryOfflineData();
        if (!offlineHistories.isEmpty()) {
            histories = offlineHistories;
            tvInfo.setVisibility(View.GONE);
        } else {
            Toast.makeText(getContext(), R.string.no_saved_history,Toast.LENGTH_LONG).show();
            tvInfo.setVisibility(View.VISIBLE);
        }
        showList();
    }

    private void onlineData() {
        long id = session.getUser_id();
        long lastid;
        if (!histories.isEmpty()) {
            lastid = histories.get(histories.size()-1).getHistoryId();
        } else {
            lastid = 0;
        }
        StringRequest request = new StringRequest(ServerConstants.URL_HISTORIES + id+"&lastid="+lastid, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                List<History> onlineHistories = jsonToList(string);
                if (!onlineHistories.isEmpty()) {
                    histories.addAll(onlineHistories);
                    newAdapter.notifyDataSetChanged();
                    addNewToOffline(string);
                    Toast.makeText(getContext(), String.format(getString(R.string.added_n_new_histories), onlineHistories.size()),Toast.LENGTH_SHORT).show();
                }
                showList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), R.string.error_loading_history,Toast.LENGTH_SHORT).show();
                if (histories.size()>0) {
                    tvInfo.setVisibility(View.GONE);
                } else {
                    tvInfo.setVisibility(View.VISIBLE);
                }
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

    }

    private void addNewToOffline(String string) {
        List<History> offlineHistories = getHistoryOfflineData();
        List<History> newHistories = jsonToList(string);

        if (offlineHistories.isEmpty()) {
            editor.putString(SharedPrefConstants.H_OFFLINE_JSON,string);
        } else {
            offlineHistories.addAll(newHistories);
            editor.putString(SharedPrefConstants.H_OFFLINE_JSON,json.toJson(offlineHistories));
        }
        editor.commit();
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
        if (histories.size()>0) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setVisibility(View.VISIBLE);
        }
    }

    public List<History> getHistoryOfflineData() {
        String histories = preferences.getString(SharedPrefConstants.H_OFFLINE_JSON,null);
        if (histories == null) {
            List<History> empty = new ArrayList<>();
            return empty;
        } else {
            return jsonToList(histories);
        }
    }

    private List<History> jsonToList(String string) {
        Type listType = new TypeToken<List<History>>() {}.getType();
        return json.fromJson(string, listType);
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
