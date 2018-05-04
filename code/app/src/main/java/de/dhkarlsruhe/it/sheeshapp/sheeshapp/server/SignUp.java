package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.ServerActivity;

/**
 * Created by d0272129 on 04.05.18.
 */

public class SignUp {

    private Context c;
    private String name;
    private String email;
    private String password;
    private String url = ServerConstants.URL;
    //private String signupRequest = "signup?name=%s&email=%s&password=%s";
    private StringRequest request;
    private String response;
    private ProgressDialog dialog;

    public SignUp(String name, String email, String password, Context c) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.c = c;
    }

    public boolean startSignup() {
        url+="signup?name="+name+"&email="+email+"&password="+password;
        //String.format(signupRequest,name,email,password);
        //System.out.println(signupRequest);
        //url+=signupRequest;

        dialog = new ProgressDialog(c);
        dialog.setMessage("Loading....");
        dialog.show();

         request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                Toast.makeText(c, string,Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText((c), "Unable to reach Server!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);

        return true;
    }



}
