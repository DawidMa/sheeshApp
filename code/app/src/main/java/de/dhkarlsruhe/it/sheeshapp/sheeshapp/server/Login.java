package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.LogInActivity;

/**
 * Created by d0272129 on 04.05.18.
 */

public class Login {

    private String email;
    private String password;
    private Context c;
    private String url = ServerConstants.URL;


    public Login(String email, String password, Context c) {
        this.email = email;
        this.password = password;
        this.c = c;
    }

    public String getUrl() {
        return url+="login?email="+email+"&password="+password;
    }

    public void startLogin(StringRequest request) {

        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }



}
