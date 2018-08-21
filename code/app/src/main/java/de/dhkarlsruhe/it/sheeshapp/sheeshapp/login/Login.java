package de.dhkarlsruhe.it.sheeshapp.sheeshapp.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MainActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.Userdata;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by d0272129 on 04.05.18.
 */

public class Login {

    private Activity c;
    private Userdata userdata;
    private ProgressDialog dialog;
    private String token;
    private View view;
    private String email, password;
    private boolean checked;
    private final String TOPIC = "JavaSampleApproach";

    public Login(Activity c, View view) {
        this.c = c;
        token = FirebaseInstanceId.getInstance().getToken();
        userdata = new Userdata(c);
        this.view = view;
    }

    public boolean isSaved() {
        return userdata.isSaved();
    }

    public void startLogin(String email, String password, boolean checked) {
        manageDialog();
        this.email = email;
        this.password = password;
        this.checked = checked;
        if (checkInput()) {
            continueLogin();
        } else {
            MyUtilities.getColoredSnackbar(view,c.getString(R.string.check_your_inout), c.getResources().getColor(R.color.redError)).show();
            dialog.dismiss();
        }
    }

    private void continueLogin() {
        StringRequest request = new StringRequest(ServerConstants.URL_LOGIN +email+ "&password="+password+"&token="+token, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                handleResponse(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyUtilities.getColoredSnackbar(view,c.getString(R.string.no_internet_connection), c.getResources().getColor(R.color.redError)).show();
                dialog.dismiss();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }

    private void handleResponse(String string) {
        if (string != null && string.substring(0, 3).equals("OK:")) {
            String response = string.substring(3, string.length());
            Type type = new TypeToken<UserSessionObject>() {}.getType();
            Gson json = new Gson();
            UserSessionObject sessionObject = json.fromJson(response, type);
            sessionObject.setContex(c);
            sessionObject.save();
            userdata.save(checked,email,password);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
            Friend friend = new Friend(c);
            friend.dropAllFriends();
            Intent intent = new Intent(c, MainActivity.class);
            c.startActivity(intent);
            c.finish();
        } else {
            MyUtilities.getColoredSnackbar(view,string, c.getResources().getColor(R.color.redError)).show();
        }
        dialog.dismiss();
    }

    private boolean checkInput() {
        if (email.equals("") || (password.equals("")))
            return false;
        return true;
    }

    public String getSavedEmail() {
        return userdata.getSavedEmail();
    }

    public String getSavedPassword() {
        return userdata.getSavedPassword();
    }

    private void manageDialog() {
        dialog = new ProgressDialog(c);
        dialog.setMessage(c.getString(R.string.loading_text));
        dialog.setTitle(c.getString(R.string.please_wait_text));
        dialog.show();
    }
}
