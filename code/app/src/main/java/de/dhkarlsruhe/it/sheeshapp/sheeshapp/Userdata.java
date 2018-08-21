package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Userdata {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public Userdata(Activity c) {
        pref = c.getSharedPreferences(SharedPrefConstants.USERDATA, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void save(boolean automatic, String email, String password) {
        editor.putBoolean(SharedPrefConstants.AUTOMATIC_LOGIN,automatic);
        if (automatic) {
            editor.putString(SharedPrefConstants.EMAIL, email);
            editor.putString(SharedPrefConstants.PASSWORD, password);
        } else {
            editor.remove(SharedPrefConstants.EMAIL);
            editor.remove(SharedPrefConstants.PASSWORD);
        }
        editor.commit();
    }

    public boolean isSaved() {
        return pref.getBoolean(SharedPrefConstants.AUTOMATIC_LOGIN,false);
    }

    public String getSavedEmail() {
        return pref.getString(SharedPrefConstants.EMAIL,"empty");
    }
    public String getSavedPassword() {
        return pref.getString(SharedPrefConstants.PASSWORD,"empty");
    }
}
