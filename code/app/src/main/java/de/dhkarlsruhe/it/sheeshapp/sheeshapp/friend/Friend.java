package de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    public Friend(Context context) {
        pref = context.getSharedPreferences(SharedPrefConstants.FRIEND,0);
        editor = pref.edit();
        sorted = pref.getBoolean("SORTED_FRIEND_LIST",false);
        this.c = context;
        session = new UserSessionObject(c);
    }

    public void addFriend(String newFriend, final String friendName) {
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

    public boolean getChecked(long id) {
        return pref.getBoolean("FRIEND_CHOOSEN_"+id,false);
    }
    public void setChecked(long id, boolean b) {
        editor.putBoolean("FRIEND_CHOOSEN_"+id,b);
        editor.commit();
    }

    public int getNumberOfFriends() {
        return pref.getInt(SharedPrefConstants.F_NUMBER_ALL, 0);
    }

    private void incNumberOfFriends() {
        int num = getNumberOfFriends();
        num++;
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL,num);
        editor.commit();
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

    public String getFriendName(long id) {
        return pref.getString("FRIEND_"+id,"FEHLER");
    }

    public void setFriendName(long id, String name) {
        editor.putString("FRIEND_"+id,name);
        String allFriends = pref.getString(SharedPrefConstants.F_ALL,"");
        allFriends+=id+";";
        editor.putString(SharedPrefConstants.F_ALL,allFriends);
        incNumberOfFriends();
        editor.commit();
    }

    public void dropAllFriends() {
        editor.putString(SharedPrefConstants.F_ALL,"");
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL,0);
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL_CHECKED,0);
        editor.putString(SharedPrefConstants.F_ALL_CHECKED,"");
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL_UNCHECKED,0);
        editor.putString(SharedPrefConstants.F_ALL_UNCHECKED,"");
        editor.commit();
    }

    public List<ChooseFriendObject> getAllCheckedFriends() {
        List<ChooseFriendObject> objects = new ArrayList<>();
        int numOfAll = getNumberOfAllCheckedFriends();
        String[] allIds;
        if (numOfAll>0) {
            allIds = pref.getString(SharedPrefConstants.F_ALL_CHECKED,null).split(";");
            for (int i=0; i<numOfAll; i++) {
                long friendId = Long.parseLong(allIds[i]);
                String friendName = getFriendName(friendId);
                objects.add(new ChooseFriendObject(friendName,friendId));
            }
        }
        return objects;
    }

    public List<ChooseFriendObject> getAllUncheckedFriends() {
        List<ChooseFriendObject> objects = new ArrayList<>();
        int numOfAll = getNumberOfAllUncheckedFriends();
        String[] allIds;
        if (numOfAll>0) {
            allIds = pref.getString(SharedPrefConstants.F_ALL_UNCHECKED,null).split(";");
            for (int i=0; i<numOfAll; i++) {
                long friendId = Long.parseLong(allIds[i]);
                String friendName = getFriendName(friendId);
                objects.add(new ChooseFriendObject(friendName,friendId));
            }
        }
        return objects;
    }

    public void setAllCheckedFriends(List<Long> checkedFriendIds) {
        String all = "";
        int num = 0;
        for (Long i:checkedFriendIds) {
            all+=i+";";
            num++;
        }
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL_CHECKED,num);
        editor.putString(SharedPrefConstants.F_ALL_CHECKED,all);
        editor.commit();
    }

    public int getNumberOfAllCheckedFriends() {
       return pref.getInt(SharedPrefConstants.F_NUMBER_ALL_CHECKED,0);
    }

    public void setAllUnchekedFriends(List<Long> uncheckedFriendIds) {
        String all = "";
        int num = 0;
        for (Long i:uncheckedFriendIds) {
            all+=i+";";
            num++;
        }
        editor.putInt(SharedPrefConstants.F_NUMBER_ALL_UNCHECKED,num);
        editor.putString(SharedPrefConstants.F_ALL_UNCHECKED,all);
        editor.commit();
    }

    public int getNumberOfAllUncheckedFriends() {
        return pref.getInt(SharedPrefConstants.F_NUMBER_ALL_UNCHECKED,0);
    }
}
