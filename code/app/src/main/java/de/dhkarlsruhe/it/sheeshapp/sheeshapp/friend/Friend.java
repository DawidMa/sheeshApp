package de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.widget.PopupWindow;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.ChooseFriendActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by Informatik on 01.12.2017.
 */

public class Friend  {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean sorted;
    private Context c;
    private UserSessionObject session;
    private AlertDialog dialog;

    public Friend(Context context) {
        pref = context.getSharedPreferences(SharedPrefConstants.FRIEND,0);
        editor = pref.edit();
        sorted = pref.getBoolean("SORTED_FRIEND_LIST",false);
        this.c = context;
        session = new UserSessionObject(c);
    }

    public void addFriend(String newFriend, final String friendName, AlertDialog dialog) {
        this.dialog = dialog;
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_ADD_FRIEND+id+"&friendmail="+newFriend, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                positiveResponse(string,friendName);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }

    private void negativeResponse(String message) {
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
    }

    private void positiveResponse(String string, String friendName) {
        Toast.makeText(c, string,Toast.LENGTH_SHORT).show();
        if (string.equals("OK") && dialog!=null) {
            dialog.dismiss();
        }
    }

    public void deleteFriendOnline(final long friendid) {
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_DELETE_FRIEND+id+"&friendid="+friendid, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                String response;
                if (string.equals("OK")) {
                    response = "Friend deleted";
                } else {
                    response = string;
                }
                Toast.makeText(c,response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }

    public boolean actualInformationAvailable() {
        return pref.contains(SharedPrefConstants.F_OFFILNE_JSON);
    }

    public void saveOfflineInformation(String string) {
        editor.putString(SharedPrefConstants.F_OFFILNE_JSON,string);
        editor.putString(SharedPrefConstants.F_LAST_CHANGED, Calendar.getInstance().toString());
        editor.commit();
    }

    public String getOfflineData() {
        return pref.getString(SharedPrefConstants.F_OFFILNE_JSON,"empty");
    }

    public void dropAllFriends() {
        editor.putString(SharedPrefConstants.F_ALL_CHECKED,"empty");
        editor.putString(SharedPrefConstants.F_ALL_UNCHECKED,"empty");
        editor.commit();
    }

    public List<ChooseFriendObject> getAllCheckedFriends() {
        Type listType = new TypeToken<List<ChooseFriendObject>>() {}.getType();
        Gson json = new Gson();
        String friends = pref.getString(SharedPrefConstants.F_ALL_CHECKED,"empty");
        if (friends.equals("empty")) {
            return Collections.emptyList();
        } else {
            return json.fromJson(friends, listType);
        }
    }

    public List<ChooseFriendObject> getAllUncheckedFriends() {
        Type listType = new TypeToken<List<ChooseFriendObject>>() {}.getType();
        Gson json = new Gson();
        String friends = pref.getString(SharedPrefConstants.F_ALL_UNCHECKED,"empty");
        if (friends.equals("empty")) {
            return Collections.emptyList();
        } else {
            return json.fromJson(friends, listType);
        }
    }

    public void setAllCheckedFriends(String checkedFriends) {
        editor.putString(SharedPrefConstants.F_ALL_CHECKED,checkedFriends);
        editor.commit();
    }

    public void setAllUncheckedFriends(String s) {
        editor.putString(SharedPrefConstants.F_ALL_UNCHECKED,s);
        editor.commit();
    }
}
