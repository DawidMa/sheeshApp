package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;

/**
 * Created by d0272129 on 04.05.18.
 */

public class SignUp {

    private Activity c;
    private String name;
    private String email;
    private String password;
    private String url = ServerConstants.URL;
    private StringRequest request;
    private ProgressDialog dialog;

    public SignUp(String name, String email, String password, Activity c) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.c = c;
    }

    public boolean startSignup(final View layout) {
        url+="signup?name="+name+"&email="+email+"&password="+password;

        dialog = new ProgressDialog(c);
        dialog.setMessage("Loading....");
        dialog.setTitle("Please Wait");
        dialog.show();
        request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                String response;
                if (string.equals("OK")) {
                    response = c.getString(R.string.check_your_emails);
                    dialog.setMessage(response);
                    dialog.setTitle(c.getString(R.string.success));
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            c.finish();
                        }
                    });
                    return;
                } else if (string.equals("")) {
                    response = "Error connecting to server";
                } else {
                    response = string;
                }
                dialog.dismiss();
                Snackbar.make(layout, response,Snackbar.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                Snackbar.make(layout, "No internet Connection",Snackbar.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);

        return true;
    }
}
