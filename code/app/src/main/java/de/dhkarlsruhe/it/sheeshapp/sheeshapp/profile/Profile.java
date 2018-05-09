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

    public Profile(Context c) {
        this.c = c;
        profilePref = c.getSharedPreferences(SharedPrefConstants.PROFILE,Context.MODE_PRIVATE);
        profileEditor = profilePref.edit();
    }

    public void setProfile(FriendlistObject friendlistObject) {
        profileEditor.putString(SharedPrefConstants.P_NAME,friendlistObject.getName());
        profileEditor.putLong(SharedPrefConstants.P_TOTAL_SESSIONS, friendlistObject.getTotal_sessions());
        profileEditor.putLong(SharedPrefConstants.P_ID, friendlistObject.getFriend_id());
        profileEditor.commit();
    }

    public long getId() {
        return profilePref.getLong(SharedPrefConstants.P_ID,0);
    }
    public String getProfileName() {
        return profilePref.getString(SharedPrefConstants.P_NAME,"errorName");
    }

    public long getTotalSessions() {
        return profilePref.getLong(SharedPrefConstants.P_TOTAL_SESSIONS,0);
    }


}
