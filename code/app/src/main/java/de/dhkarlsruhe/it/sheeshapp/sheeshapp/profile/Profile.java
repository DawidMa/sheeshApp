package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;

/**
 * Created by d0272129 on 08.05.18.
 */

public class Profile {

    private Context c;
    private SharedPreferences profilePref;
    private SharedPreferences.Editor profileEditor;
    private boolean load;
    private Thread thread;

    public Profile(Context c, boolean load) {
        this.c = c;
        profilePref = c.getSharedPreferences(SharedPrefConstants.PROFILE,Context.MODE_PRIVATE);
        profileEditor = profilePref.edit();
        this.load = load;
    }

    public boolean setProfile(FriendlistObject friendlistObject) {
        profileEditor.putString(SharedPrefConstants.P_NAME,friendlistObject.getName());
        profileEditor.putLong(SharedPrefConstants.P_TOTAL_SESSIONS, friendlistObject.getTotal_sessions());
        profileEditor.putLong(SharedPrefConstants.P_ID, friendlistObject.getFriend_id());
        profileEditor.commit();
        return onlineLoader();

    }

    public String getProfileName() {
        return profilePref.getString(SharedPrefConstants.P_NAME,"errorName");
    }

    public long getTotalSessions() {
        return profilePref.getLong(SharedPrefConstants.P_TOTAL_SESSIONS,0);
    }

    public String getTobacco() {
        return profilePref.getString(SharedPrefConstants.P_RESPONSE,"errorResponse");
    }

    public boolean onlineLoader() {
        profileEditor.putBoolean(SharedPrefConstants.P_READY,false);
        profileEditor.commit();
        int user_id = (int)profilePref.getLong(SharedPrefConstants.P_ID,0);
        final StringRequest request = new StringRequest(ServerConstants.URL+"user/info?user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                profileEditor.putString(SharedPrefConstants.P_RESPONSE,string);
                profileEditor.putBoolean(SharedPrefConstants.P_READY,true);
                profileEditor.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                profileEditor.putString(SharedPrefConstants.P_RESPONSE,volleyError.getMessage());
                profileEditor.putBoolean(SharedPrefConstants.P_READY,true);
                profileEditor.commit();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
        new Thread(new Runnable() {
            public void run() {
                while (!isReady()) {
                    System.out.println("OKOKOK");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return true;
    }

    public boolean isReady() {
        return profilePref.getBoolean(SharedPrefConstants.P_READY,false);
    }
}
