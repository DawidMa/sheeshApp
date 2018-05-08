package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.content.Context;
import android.content.SharedPreferences;

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
        profileEditor.putInt(SharedPrefConstants.P_TOTAL_SESSIONS, friendlistObject.getTotal_sessions());
        profileEditor.commit();
    }

    public String getProfileName() {
        return profilePref.getString(SharedPrefConstants.P_NAME,"errorName");
    }

    public int getTotalSessions() {
        return profilePref.getInt(SharedPrefConstants.P_TOTAL_SESSIONS,0);
    }
}
