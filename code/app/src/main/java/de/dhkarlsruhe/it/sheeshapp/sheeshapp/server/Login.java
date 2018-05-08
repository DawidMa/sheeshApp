package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.LogInActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;

/**
 * Created by d0272129 on 04.05.18.
 */

public class Login {

    private String email;
    private String password;
    private Context c;
    private String url;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public Login(Context c){
        this.c = c;
        pref = c.getSharedPreferences(SharedPrefConstants.NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public String getUrl() {
        url=ServerConstants.URL;
        return url+="login?email="+email+"&password="+password;
    }

    public boolean isSaved() {
        return pref.getBoolean(SharedPrefConstants.AUTOMATIC_LOGIN,false);
    }

    public void startLogin(StringRequest request) {
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }

    public String getSavedEmail() {
        return pref.getString(SharedPrefConstants.EMAIL,"email");
    }

    public String getSavedPassword() {
        return pref.getString(SharedPrefConstants.PASSWORD,"email");
    }

    public void setSavedEmail() {
        editor.putString(SharedPrefConstants.EMAIL,email);
        editor.commit();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSaved(boolean saved) {
        editor.putBoolean(SharedPrefConstants.AUTOMATIC_LOGIN,saved);
        editor.commit();
    }

    public void setSavedPassword() {
        editor.putString(SharedPrefConstants.PASSWORD,password);
        editor.commit();
    }
}
