package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendRequestObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by d0272129 on 14.08.18.
 */

public class Guest {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Activity context;

    public Guest(Activity context) {
        this.context = context;
        pref = context.getSharedPreferences(SharedPrefConstants.GUEST, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setName(String name) {
        editor.putString(SharedPrefConstants.G_NAME,name);
        editor.commit();
    }

    public String getName() {
        return pref.getString(SharedPrefConstants.G_NAME,"noname");
    }

    public void setEmail(String name) {
        editor.putString(SharedPrefConstants.G_EMAIL,name+"@example.com");
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(SharedPrefConstants.G_EMAIL,"nomail");
    }

    public void setTime(int minute, int hourOfDay) {
        editor.putInt(SharedPrefConstants.SECONDS,minute);
        editor.putInt(SharedPrefConstants.MINUTES,hourOfDay);
        editor.commit();
    }

    public int getMinutes() {
        return pref.getInt(SharedPrefConstants.MINUTES,0);
    }

    public int getSeconds() {
        return pref.getInt(SharedPrefConstants.SECONDS,0);
    }

    public void setTimeInSeconds(int sec) {
        editor.putInt(SharedPrefConstants.TIME_IN_SECONDS,sec);
        editor.commit();
    }

    public List<ChooseFriendObject> getFriends() {
        String friends = pref.getString(SharedPrefConstants.F_ALL_CHECKED,"empty");
        if (friends.equals("empty")) {
            return Collections.emptyList();
        } else {
            Type listType = new TypeToken<List<ChooseFriendObject>>() {}.getType();
            Gson json = new Gson();
            return json.fromJson(friends, listType);
        }
    }

    public void setFriends(List<ChooseFriendObject> objects) {
        Gson json = new Gson();
        editor.putString(SharedPrefConstants.F_ALL_CHECKED,json.toJson(objects));
        editor.commit();
    }

    public void deleteAll() {
        editor.clear();
        editor.apply();
    }

    public float getTimeInSeconds() {
        return pref.getInt(SharedPrefConstants.TIME_IN_SECONDS,5);
    }
}
