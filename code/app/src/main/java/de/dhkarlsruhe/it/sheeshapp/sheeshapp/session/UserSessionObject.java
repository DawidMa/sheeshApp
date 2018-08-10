package de.dhkarlsruhe.it.sheeshapp.sheeshapp.session;

import android.content.Context;
import android.content.SharedPreferences;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;

/**
 * Created by d0272129 on 05.05.18.
 */

public class UserSessionObject {

    private long user_id;
    private String name;
    private String email;
    private String fav_tobacco;
    private boolean has_icon;
    private String last_changed_icon_id;
    private Context c;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSessionObject(Context c){
        this.c = c;
        pref = c.getSharedPreferences(SharedPrefConstants.SESSION, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setContex(Context c) {
        this.c = c;
    }

    public void save() {
        pref = c.getSharedPreferences(SharedPrefConstants.SESSION, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putLong(SharedPrefConstants.S_USER_ID,user_id);
        editor.putString(SharedPrefConstants.S_NAME,name);
        editor.putString(SharedPrefConstants.S_EMAIL,email);
        editor.putString(SharedPrefConstants.S_FAV_TOBACCO,fav_tobacco);
        editor.putBoolean(SharedPrefConstants.S_HAS_ICON,has_icon);
        editor.putString(SharedPrefConstants.S_LAST_CHANGED_ICON_ID,last_changed_icon_id);
        editor.commit();
    }

    public long getUser_id() {
        return pref.getLong(SharedPrefConstants.S_USER_ID,000L);
    }

    public String getName() {
        return pref.getString(SharedPrefConstants.S_NAME,"errorName");
    }

    public String getEmail() {
        return pref.getString(SharedPrefConstants.S_EMAIL,"errorEmail");
    }

    public String getFav_tobacco() {
        return pref.getString(SharedPrefConstants.S_FAV_TOBACCO,"errorFavTobacco");
    }

    public boolean isHas_icon() {
        return pref.getBoolean(SharedPrefConstants.S_HAS_ICON,false);
    }

    public String getImage() {
        return pref.getString(SharedPrefConstants.S_IMAGE,"empty");
    }

    public void setImage(String image) {
        editor.putString(SharedPrefConstants.S_IMAGE,image);
        editor.commit();
    }

    public String getLast_changed_icon_id() {
        return pref.getString(SharedPrefConstants.S_LAST_CHANGED_ICON_ID,"noid");
    }

    public void setLast_changed_icon_id(String last_changed_icon_id) {
        editor.putString(SharedPrefConstants.S_LAST_CHANGED_ICON_ID,last_changed_icon_id);
        editor.commit();
    }
}
