package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.Login;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by Informatik on 23.11.2017.
 */

public class LogInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;
    private ProgressDialog dialog;
    private Login login;
    private CheckBox cbSaveLogin;
    private Gson json = new Gson();
    private final String TOPIC = "JavaSampleApproach";
    private final static String AD_BANNER_TEST = "ca-app-pub-3940256099942544/6300978111";
    private final static String AD_BANNER_ID = "ca-app-pub-4355529827581242/7588945055";
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MobileAds.initialize(this, MyUtilities.AD_APP_ID);
        adView = findViewById(R.id.adLogin);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        etEmail = findViewById(R.id.etLogEmail);
        etPassword = findViewById(R.id.etLogPassword);
        cbSaveLogin = findViewById(R.id.cbLogSaveLogin);
        login = new Login(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        cbSaveLogin.setChecked(login.isSaved());

        checkAutomaticLogin();
    }

    private void checkAutomaticLogin() {
        if(login.isSaved()) {
            email = login.getSavedEmail();
            password = login.getSavedPassword();
            if (!email.equals("email") && !password.equals("password")) {
                etEmail.setText(email);
                etPassword.setText(password);
            }
        }
    }

    public void login(View view) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        login.setEmail(email);
        login.setPassword(password);
        dialog.show();
        if (checkInput()) {
            acceptLogin();
        } else {
            Toast.makeText(this,"Check input", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void cancelLogin(View view) {
        this.finish();
    }

    private boolean checkInput() {
        if (email.equals(""))
            return false;
        if (password.equals(""))
            return false;
        return true;
    }

    private void acceptLogin() {
        StringRequest request =  new StringRequest(login.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                handleResponse(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleResponse(volleyError.getMessage());
            }
        });
        login.startLogin(request);
    }

    private void handleResponse(String string) {
        if (string!=null&&string.substring(0,3).equals("OK:")) {
            String response = string.substring(3,string.length());
            UserSessionObject sessionObject;
            Type type = new TypeToken<UserSessionObject>(){}.getType();
            sessionObject = json.fromJson(response,type);
            sessionObject.setContex(this);
            sessionObject.save();
            login.setSaved(cbSaveLogin.isChecked());
            login.setSavedEmail();
            login.setSavedPassword();
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
            Friend friend = new Friend(this);
            friend.dropAllFriends();
            this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

        } else {
            Toast.makeText(this, string, Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
