package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

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
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.history.ExpandableListAdapter;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.history.History;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by Informatik on 29.11.2017.
 */

public class HistoryFragmentGuest extends Fragment {

    private TextView tvInfo;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        tvInfo = rootView.findViewById(R.id.tvHitoryInfo);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(getString(R.string.no_saved_history));
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {
        //listAdapter.notifyDataSetChanged();
        super.onPause();
    }
}
